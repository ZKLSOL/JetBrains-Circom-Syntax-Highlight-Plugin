package com.ohaddahan.circom.lang

import com.ohaddahan.circom.parser.CircomLexer
import com.ohaddahan.circom.parser.CircomParser
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory

object CircomTokenTypes {
    val BAD_TOKEN_TYPE: IElementType = IElementType("BAD_TOKEN", CircomLanguage)

    @Volatile
    private var initialized = false

    init {
        initializeElementTypeFactory()
    }

    @JvmStatic
    @Synchronized
    fun initializeElementTypeFactory() {
        if (initialized) return
        initialized = true
        PSIElementTypeFactory.defineLanguageIElementTypes(
            CircomLanguage,
            CircomLexer.tokenNames,
            CircomParser.ruleNames
        )
    }

    val COMMENTS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.COMMENT,
        CircomLexer.LINE_COMMENT
    )

    val WHITESPACES: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.WS
    )

    val KEYWORDS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.PRAGMA,
        CircomLexer.CIRCOM,
        CircomLexer.CUSTOM_TEMPLATES,
        CircomLexer.INCLUDE,
        CircomLexer.TEMPLATE,
        CircomLexer.FUNCTION,
        CircomLexer.MAIN,
        CircomLexer.PUBLIC,
        CircomLexer.COMPONENT,
        CircomLexer.VAR,
        CircomLexer.SIGNAL,
        CircomLexer.INPUT,
        CircomLexer.OUTPUT,
        CircomLexer.IF,
        CircomLexer.ELSE,
        CircomLexer.FOR,
        CircomLexer.WHILE,
        CircomLexer.DO,
        CircomLexer.LOG,
        CircomLexer.ASSERT,
        CircomLexer.RETURN
    )

    val MODIFIERS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.CUSTOM,
        CircomLexer.PARALLEL,
        CircomLexer.BUS
    )

    val INTEGERS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.NUMBER
    )

    val STRINGS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.STRING
    )

    val CONSTRAINTS_GENERATORS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.EQ_CONSTRAINT,
        CircomLexer.LEFT_ASSIGNMENT,
        CircomLexer.RIGHT_ASSIGNMENT,
        CircomLexer.LEFT_CONSTRAINT,
        CircomLexer.RIGHT_CONSTRAINT
    )

    val SIGNAL_TYPES: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.SIGNAL_TYPE
    )

    val COMMA: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.COMMA
    )

    val SEMICOLON: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.SEMICOLON
    )

    val IDENTIFIERS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.ID
    )

    val SQUARE_BRACKET: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.LB,
        CircomLexer.RB
    )

    val CURLY_BRACKET: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.LC,
        CircomLexer.RC
    )

    val PARENTHESIS: TokenSet = PSIElementTypeFactory.createTokenSet(
        CircomLanguage,
        CircomLexer.LP,
        CircomLexer.RP
    )
}
