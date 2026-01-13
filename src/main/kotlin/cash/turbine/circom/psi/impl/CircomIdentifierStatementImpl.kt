package cash.turbine.circom.psi.impl

import cash.turbine.circom.lang.CircomLanguage
import cash.turbine.circom.parser.CircomLexer
import cash.turbine.circom.psi.CircomNamedElement
import cash.turbine.circom.psi.CircomPsiFile
import cash.turbine.circom.reference.CircomReference
import cash.turbine.circom.stubs.CircomNameIndex
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class CircomIdentifierStatementImpl(node: ASTNode) : ANTLRPsiNode(node) {

    companion object {
        private val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(CircomLanguage)
        private val ID_TYPE = tokenTypes[CircomLexer.ID]
    }

    val identifierName: String?
        get() = node.findChildByType(ID_TYPE)?.text

    override fun getReference(): PsiReference? {
        val idNode = node.findChildByType(ID_TYPE) ?: return null
        val idText = idNode.text
        val startOffset = idNode.startOffset - node.startOffset
        return CircomReference(this, TextRange(startOffset, startOffset + idText.length))
    }
}
