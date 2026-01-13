package cash.turbine.circom.stubs

import cash.turbine.circom.lang.CircomFileType
import cash.turbine.circom.psi.CircomNamedElement
import cash.turbine.circom.psi.impl.CircomFunctionDefinitionImpl
import cash.turbine.circom.psi.impl.CircomTemplateDefinitionImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import cash.turbine.circom.psi.CircomPsiFile

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
