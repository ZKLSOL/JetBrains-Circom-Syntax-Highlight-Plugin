package com.ohaddahan.circom.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import javax.swing.Icon

class CircomFunctionDefinitionImpl(node: ASTNode) : CircomNamedElementImpl(node) {

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
