package com.ohaddahan.circom.psi.impl

import com.ohaddahan.circom.lang.CircomLanguage
import com.ohaddahan.circom.parser.CircomLexer
import com.ohaddahan.circom.reference.CircomReference
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class CircomCallExpressionImpl(node: ASTNode) : ANTLRPsiNode(node) {

    companion object {
        private val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(CircomLanguage)
        private val ID_TYPE = tokenTypes[CircomLexer.ID]
        private val LP_TYPE = tokenTypes[CircomLexer.LP]
    }

    val calleeName: String?
        get() = node.findChildByType(ID_TYPE)?.text

    private val isFunctionCall: Boolean
        get() {
            val idNode = node.findChildByType(ID_TYPE) ?: return false
            val lpNode = node.findChildByType(LP_TYPE) ?: return false
            return lpNode.startOffset > idNode.startOffset
        }

    override fun getReference(): PsiReference? {
        if (!isFunctionCall) return null

        val idNode = node.findChildByType(ID_TYPE) ?: return null
        val idText = idNode.text
        val startOffset = idNode.startOffset - node.startOffset
        return CircomReference(this, TextRange(startOffset, startOffset + idText.length))
    }
}
