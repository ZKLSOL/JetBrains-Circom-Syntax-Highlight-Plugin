package cash.turbine.circom.adaptors

import cash.turbine.circom.parser.CircomLexer
import cash.turbine.circom.parser.CircomParser
import cash.turbine.circom.lang.CircomLanguage
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.v4.runtime.Lexer

class CircomLexerAdaptor(lexer: CircomLexer) : ANTLRLexerAdaptor(CircomLanguage, lexer) {

    companion object {
        init {
            initializeElementTypeFactory()
        }

        @JvmStatic
        fun initializeElementTypeFactory() {
            val vocabulary = CircomLexer.VOCABULARY
            val tokenNames = Array(vocabulary.maxTokenType + 1) { i ->
                vocabulary.getSymbolicName(i) ?: vocabulary.getLiteralName(i) ?: "<INVALID>"
            }
            PSIElementTypeFactory.defineLanguageIElementTypes(
                CircomLanguage,
                tokenNames,
                CircomParser.ruleNames
            )
        }

        private val INITIAL_STATE = CircomLexerState(Lexer.DEFAULT_MODE, null)
    }

    override fun getInitialState(): CircomLexerState {
        return INITIAL_STATE
    }

    override fun getLexerState(lexer: Lexer): CircomLexerState {
        return if (lexer._modeStack.isEmpty) {
            CircomLexerState(lexer._mode, null)
        } else {
            CircomLexerState(lexer._mode, lexer._modeStack)
        }
    }
}
