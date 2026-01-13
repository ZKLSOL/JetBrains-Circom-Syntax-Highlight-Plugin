package cash.turbine.circom.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import javax.swing.Icon

class CircomSignalDeclarationImpl(node: ASTNode) : CircomNamedElementImpl(node) {

    val isInput: Boolean
        get() = node.text.contains("input")

    val isOutput: Boolean
        get() = node.text.contains("output")

    val signalType: String
        get() = when {
            isInput -> "input"
            isOutput -> "output"
            else -> "intermediate"
        }

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = "${name ?: "<unnamed>"} : $signalType signal"
            override fun getLocationString(): String? = containingFile?.name
            override fun getIcon(unused: Boolean): Icon? = null
        }
    }
}
