package com.ohaddahan.circom.psi.impl

import com.ohaddahan.circom.psi.CircomNamedElement
import com.ohaddahan.circom.parser.CircomLexer
import com.ohaddahan.circom.lang.CircomLanguage
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

open class CircomNamedElementImpl(node: ASTNode) : ANTLRPsiNode(node), CircomNamedElement {

    companion object {
        private val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(CircomLanguage)
        val ID_TYPE = tokenTypes[CircomLexer.ID]
    }

    override fun getNameIdentifier(): PsiElement? {
        val directChild = node.findChildByType(ID_TYPE)?.psi
        if (directChild != null) return directChild

        return findFirstIdRecursively(node)?.psi
    }

    private fun findFirstIdRecursively(node: ASTNode): ASTNode? {
        for (child in node.getChildren(null)) {
            if (child.elementType == ID_TYPE) {
                return child
            }
            val found = findFirstIdRecursively(child)
            if (found != null) return found
        }
        return null
    }

    override fun getName(): String? {
        return nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement {
        val nameNode = nameIdentifier?.node
            ?: throw IncorrectOperationException("Cannot rename element without name identifier")

        val newNode = CircomElementFactory.createIdentifier(project, name).node
        node.replaceChild(nameNode, newNode)
        return this
    }

    override fun getTextOffset(): Int {
        return nameIdentifier?.textOffset ?: super.getTextOffset()
    }

    override fun getUseScope(): SearchScope {
        return GlobalSearchScope.projectScope(project)
    }
}
