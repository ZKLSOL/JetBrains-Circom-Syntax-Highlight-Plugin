package com.ohaddahan.circom.stubs

import com.ohaddahan.circom.lang.CircomFileType
import com.ohaddahan.circom.psi.CircomNamedElement
import com.ohaddahan.circom.psi.impl.CircomFunctionDefinitionImpl
import com.ohaddahan.circom.psi.impl.CircomTemplateDefinitionImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.ohaddahan.circom.psi.CircomPsiFile

object CircomNameIndex {

    fun findTemplates(project: Project, name: String): List<CircomTemplateDefinitionImpl> {
        val results = mutableListOf<CircomTemplateDefinitionImpl>()
        val virtualFiles = FileTypeIndex.getFiles(CircomFileType, GlobalSearchScope.projectScope(project))

        for (virtualFile in virtualFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? CircomPsiFile ?: continue
            val template = psiFile.findTemplateByName(name)
            if (template != null) {
                results.add(template)
            }
        }

        return results
    }

    fun findFunctions(project: Project, name: String): List<CircomFunctionDefinitionImpl> {
        val results = mutableListOf<CircomFunctionDefinitionImpl>()
        val virtualFiles = FileTypeIndex.getFiles(CircomFileType, GlobalSearchScope.projectScope(project))

        for (virtualFile in virtualFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? CircomPsiFile ?: continue
            val function = psiFile.findFunctionByName(name)
            if (function != null) {
                results.add(function)
            }
        }

        return results
    }

    fun findAllTemplates(project: Project): List<CircomTemplateDefinitionImpl> {
        val results = mutableListOf<CircomTemplateDefinitionImpl>()
        val virtualFiles = FileTypeIndex.getFiles(CircomFileType, GlobalSearchScope.projectScope(project))

        for (virtualFile in virtualFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? CircomPsiFile ?: continue
            results.addAll(psiFile.templates)
        }

        return results
    }

    fun findAllFunctions(project: Project): List<CircomFunctionDefinitionImpl> {
        val results = mutableListOf<CircomFunctionDefinitionImpl>()
        val virtualFiles = FileTypeIndex.getFiles(CircomFileType, GlobalSearchScope.projectScope(project))

        for (virtualFile in virtualFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? CircomPsiFile ?: continue
            results.addAll(psiFile.functions)
        }

        return results
    }

    fun findAllNamedElements(project: Project): List<CircomNamedElement> {
        val results = mutableListOf<CircomNamedElement>()
        val virtualFiles = FileTypeIndex.getFiles(CircomFileType, GlobalSearchScope.projectScope(project))

        for (virtualFile in virtualFiles) {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) as? CircomPsiFile ?: continue
            results.addAll(PsiTreeUtil.findChildrenOfType(psiFile, CircomNamedElement::class.java))
        }

        return results
    }
}
