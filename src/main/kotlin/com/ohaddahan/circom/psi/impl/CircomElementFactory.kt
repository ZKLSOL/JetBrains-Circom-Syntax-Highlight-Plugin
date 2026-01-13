package com.ohaddahan.circom.psi.impl

import com.ohaddahan.circom.lang.CircomFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil

object CircomElementFactory {

    fun createIdentifier(project: Project, name: String): PsiElement {
        val dummyFile = createDummyFile(project, "template $name() {}")
        return PsiTreeUtil.findChildrenOfType(dummyFile, PsiElement::class.java)
            .first { it.text == name && it.node.elementType == CircomNamedElementImpl.ID_TYPE }
    }

    fun createDummyFile(project: Project, text: String): PsiElement {
        return PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.circom", CircomFileType, text)
    }
}
