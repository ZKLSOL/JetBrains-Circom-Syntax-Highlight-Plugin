package cash.turbine.circom.ide

import cash.turbine.circom.lang.CircomLanguage
import cash.turbine.circom.lang.CircomTokenTypes
import cash.turbine.circom.parser.CircomLexer
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory

class CircomFoldingBuilder : FoldingBuilderEx() {

    companion object {
        init {
            CircomTokenTypes.initializeElementTypeFactory()
        }

        private val tokenTypes = PSIElementTypeFactory.getTokenIElementTypes(CircomLanguage)
        private val LC_TYPE = tokenTypes[CircomLexer.LC]
        private val RC_TYPE = tokenTypes[CircomLexer.RC]
        private val COMMENT_TYPE = tokenTypes[CircomLexer.COMMENT]
    }

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        PsiTreeUtil.processElements(root) { element ->
            val node = element.node
            if (node != null) {
                val elementType = node.elementType

                if (elementType == LC_TYPE) {
                    val closingBrace = findMatchingClosingBrace(element)
                    if (closingBrace != null) {
                        val startOffset = element.textRange.startOffset
                        val endOffset = closingBrace.textRange.endOffset
                        if (endOffset > startOffset + 1) {
                            descriptors.add(
                                FoldingDescriptor(
                                    node,
                                    TextRange(startOffset, endOffset),
                                    FoldingGroup.newGroup("circom-block")
                                )
                            )
                        }
                    }
                }

                if (elementType == COMMENT_TYPE) {
                    val text = element.text
                    if (text.contains("\n")) {
                        descriptors.add(
                            FoldingDescriptor(
                                node,
                                element.textRange,
                                FoldingGroup.newGroup("circom-comment")
                            )
                        )
                    }
                }
            }
            true
        }

        return descriptors.toTypedArray()
    }

    private fun findMatchingClosingBrace(openBrace: PsiElement): PsiElement? {
        var depth = 1
        var current = openBrace.nextSibling

        while (current != null && depth > 0) {
            val type = current.node?.elementType
            when (type) {
                LC_TYPE -> depth++
                RC_TYPE -> {
                    depth--
                    if (depth == 0) return current
                }
            }
            current = current.nextSibling
        }

        return null
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return when (node.elementType) {
            COMMENT_TYPE -> "/* ... */"
            LC_TYPE -> "{ ... }"
            else -> "..."
        }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
