package com.ohaddahan.circom.psi.impl

import com.ohaddahan.circom.psi.CircomElementTypes
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon

class CircomTemplateDefinitionImpl(node: ASTNode) : CircomNamedElementImpl(node) {

    val isParallel: Boolean
        get() = node.text.contains("parallel")

    val isCustom: Boolean
        get() = node.text.contains("custom")

    val parameters: List<String>
        get() {
            val paramList = node.text
                .substringAfter("(", "")
                .substringBefore(")", "")
                .trim()
            return if (paramList.isEmpty()) emptyList()
            else paramList.split(",").map { it.trim() }
        }

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = name ?: "<unnamed>"
            override fun getLocationString(): String? = containingFile?.name
            override fun getIcon(unused: Boolean): Icon? = null
        }
    }
}
