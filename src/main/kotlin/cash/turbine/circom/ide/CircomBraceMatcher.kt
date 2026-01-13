package cash.turbine.circom.ide

import cash.turbine.circom.lang.CircomTokenTypes
import cash.turbine.circom.parser.CircomLexer
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory

class CircomBraceMatcher : PairedBraceMatcher {
    companion object {
        private val BRACE_PAIRS: Array<BracePair>

        init {
            CircomTokenTypes.initializeElementTypeFactory()

            val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(
                cash.turbine.circom.lang.CircomLanguage
            )

            BRACE_PAIRS = arrayOf(
                BracePair(tokenTypes[CircomLexer.LP], tokenTypes[CircomLexer.RP], false),
                BracePair(tokenTypes[CircomLexer.LB], tokenTypes[CircomLexer.RB], false),
                BracePair(tokenTypes[CircomLexer.LC], tokenTypes[CircomLexer.RC], true)
            )
        }
    }

    override fun getPairs(): Array<BracePair> = BRACE_PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}
