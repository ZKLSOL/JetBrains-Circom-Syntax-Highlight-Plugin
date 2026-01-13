package com.ohaddahan.circom.psi.impl

import com.ohaddahan.circom.parser.CircomLexer
import com.ohaddahan.circom.lang.CircomLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory

object CircomPsiImplUtil {
    private val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(CircomLanguage)
    private val ID_TYPE = tokenTypes[CircomLexer.ID]

    fun findNameIdentifier(element: PsiElement): PsiElement? {
        return PsiTreeUtil.findChildrenOfType(element, PsiElement::class.java)
            .firstOrNull { it.node.elementType == ID_TYPE }
    }

    fun getName(element: PsiElement): String? {
        return findNameIdentifier(element)?.text
    }
}
