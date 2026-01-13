package com.ohaddahan.circom.psi.impl

import com.ohaddahan.circom.lang.CircomLanguage
import com.ohaddahan.circom.parser.CircomLexer
import com.ohaddahan.circom.psi.CircomNamedElement
import com.ohaddahan.circom.psi.CircomPsiFile
import com.ohaddahan.circom.reference.CircomReference
import com.ohaddahan.circom.stubs.CircomNameIndex
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
