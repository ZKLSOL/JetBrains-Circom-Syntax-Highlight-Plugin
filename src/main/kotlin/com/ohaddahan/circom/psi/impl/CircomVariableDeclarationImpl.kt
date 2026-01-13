package com.ohaddahan.circom.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import javax.swing.Icon

class CircomVariableDeclarationImpl(node: ASTNode) : CircomNamedElementImpl(node) {

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = name ?: "<unnamed>"
            override fun getLocationString(): String? = containingFile?.name
            override fun getIcon(unused: Boolean): Icon? = null
        }
    }
}
