package com.ohaddahan.circom.psi

import com.ohaddahan.circom.lang.CircomFileType
import com.ohaddahan.circom.lang.CircomLanguage
import com.ohaddahan.circom.psi.impl.CircomFunctionDefinitionImpl
import com.ohaddahan.circom.psi.impl.CircomIncludeStatementImpl
import com.ohaddahan.circom.psi.impl.CircomTemplateDefinitionImpl
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.PsiTreeUtil

class CircomPsiFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CircomLanguage) {

    override fun getFileType(): FileType = CircomFileType

    override fun toString(): String = "Circom File"

    val templates: Collection<CircomTemplateDefinitionImpl>
        get() = PsiTreeUtil.findChildrenOfType(this, CircomTemplateDefinitionImpl::class.java)

    val functions: Collection<CircomFunctionDefinitionImpl>
        get() = PsiTreeUtil.findChildrenOfType(this, CircomFunctionDefinitionImpl::class.java)

    val includes: Collection<CircomIncludeStatementImpl>
        get() = PsiTreeUtil.findChildrenOfType(this, CircomIncludeStatementImpl::class.java)

    fun findTemplateByName(name: String): CircomTemplateDefinitionImpl? {
        return templates.find { it.name == name }
    }

    fun findFunctionByName(name: String): CircomFunctionDefinitionImpl? {
        return functions.find { it.name == name }
    }
}
