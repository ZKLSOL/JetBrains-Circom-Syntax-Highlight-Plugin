package com.ohaddahan.circom.psi.impl

import com.ohaddahan.circom.parser.CircomLexer
import com.ohaddahan.circom.lang.CircomLanguage
import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class CircomIncludeStatementImpl(node: ASTNode) : ANTLRPsiNode(node) {

    companion object {
        private val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(CircomLanguage)
        private val STRING_TYPE = tokenTypes[CircomLexer.STRING]
    }

    val includePath: String?
        get() {
            val stringNode = node.findChildByType(STRING_TYPE)
            val text = stringNode?.text ?: return null
            return text.removeSurrounding("\"")
        }
}
