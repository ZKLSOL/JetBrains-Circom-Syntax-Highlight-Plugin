package com.ohaddahan.circom.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import javax.swing.Icon

class CircomComponentDeclarationImpl(node: ASTNode) : CircomNamedElementImpl(node) {

    val templateName: String?
        get() {
            val text = node.text
            val eqIndex = text.indexOf("=")
            if (eqIndex == -1) return null
            return text.substring(eqIndex + 1)
                .trim()
                .substringBefore("(")
                .trim()
        }

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String {
                val tplName = templateName
                return if (tplName != null) "${name ?: "<unnamed>"} : $tplName"
                else name ?: "<unnamed>"
            }
            override fun getLocationString(): String? = containingFile?.name
            override fun getIcon(unused: Boolean): Icon? = null
        }
    }
}
