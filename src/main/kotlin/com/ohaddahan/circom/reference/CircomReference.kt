package com.ohaddahan.circom.reference

import com.ohaddahan.circom.psi.CircomNamedElement
import com.ohaddahan.circom.psi.CircomPsiFile
import com.ohaddahan.circom.psi.impl.*
import com.ohaddahan.circom.stubs.CircomNameIndex
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil

class CircomReference(
    element: PsiElement,
    textRange: TextRange
) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    private val name: String = element.text.substring(textRange.startOffset, textRange.endOffset)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val results = mutableListOf<ResolveResult>()
        val project = myElement.project

        // First search in local scope (same file)
        val file = myElement.containingFile as? CircomPsiFile
        if (file != null) {
            // Check templates
            file.findTemplateByName(name)?.let {
                results.add(PsiElementResolveResult(it))
            }

            // Check functions
            file.findFunctionByName(name)?.let {
                results.add(PsiElementResolveResult(it))
            }

            // Check local declarations in the containing template/function
            val container = PsiTreeUtil.getParentOfType(
                myElement,
                CircomTemplateDefinitionImpl::class.java,
                CircomFunctionDefinitionImpl::class.java
            )

            if (container != null) {
                // Search for signals, variables, and components
                PsiTreeUtil.findChildrenOfType(container, CircomNamedElement::class.java)
                    .filter { it.name == name && it != myElement && it.textOffset < myElement.textOffset }
                    .forEach {
                        results.add(PsiElementResolveResult(it))
                    }
            }
        }

        // If not found locally, search in project scope
        if (results.isEmpty()) {
            CircomNameIndex.findTemplates(project, name).forEach {
                results.add(PsiElementResolveResult(it))
            }
            CircomNameIndex.findFunctions(project, name).forEach {
                results.add(PsiElementResolveResult(it))
            }
        }

        return results.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val project = myElement.project
        val variants = mutableListOf<Any>()

        // Add all templates and functions from project
        CircomNameIndex.findAllTemplates(project).mapNotNull { it.name }.forEach { variants.add(it) }
        CircomNameIndex.findAllFunctions(project).mapNotNull { it.name }.forEach { variants.add(it) }

        // Add local declarations
        val file = myElement.containingFile as? CircomPsiFile
        if (file != null) {
            val container = PsiTreeUtil.getParentOfType(
                myElement,
                CircomTemplateDefinitionImpl::class.java,
                CircomFunctionDefinitionImpl::class.java
            )
            if (container != null) {
                PsiTreeUtil.findChildrenOfType(container, CircomNamedElement::class.java)
                    .mapNotNull { it.name }
                    .forEach { variants.add(it) }
            }
        }

        return variants.toTypedArray()
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val newIdElement = CircomElementFactory.createIdentifier(myElement.project, newElementName)

        // Find the ID node at the reference range and replace only that
        val rangeInElement = rangeInElement
        val startOffset = myElement.textRange.startOffset + rangeInElement.startOffset
        val idNode = myElement.containingFile.findElementAt(startOffset)

        if (idNode != null && idNode.text == name) {
            idNode.replace(newIdElement)
            return myElement
        }

        // Fallback: replace whole element (for simple cases)
        return myElement.replace(newIdElement)
    }
}
