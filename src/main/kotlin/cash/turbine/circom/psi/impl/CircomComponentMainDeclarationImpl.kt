package cash.turbine.circom.psi.impl

import cash.turbine.circom.lang.CircomLanguage
import cash.turbine.circom.parser.CircomLexer
import cash.turbine.circom.reference.CircomReference
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class CircomComponentMainDeclarationImpl(node: ASTNode) : ANTLRPsiNode(node) {

    companion object {
        private val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(CircomLanguage)
        private val ID_TYPE = tokenTypes[CircomLexer.ID]
    }

    val templateName: String?
        get() {
            val children = node.getChildren(null)
            for (child in children) {
                if (child.elementType == ID_TYPE && child.text != "main") {
                    return child.text
                }
            }
            return null
        }

    override fun getReference(): PsiReference? {
        val children = node.getChildren(null)
        for (child in children) {
            if (child.elementType == ID_TYPE && child.text != "main") {
                val startOffset = child.startOffset - node.startOffset
                return CircomReference(this, TextRange(startOffset, startOffset + child.textLength))
            }
        }
        return null
    }
}
