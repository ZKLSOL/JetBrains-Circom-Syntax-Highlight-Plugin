package cash.turbine.circom.psi

import cash.turbine.circom.adaptors.CircomLexerAdaptor
import cash.turbine.circom.lang.CircomLanguage
import cash.turbine.circom.lang.CircomTokenTypes
import cash.turbine.circom.parser.CircomLexer
import cash.turbine.circom.parser.CircomParser
import cash.turbine.circom.psi.impl.*
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree

class CircomParserDefinition : ParserDefinition {

    init {
        CircomTokenTypes.initializeElementTypeFactory()
    }

    override fun createLexer(project: Project?): Lexer {
        val lexer = CircomLexer(null)
        return CircomLexerAdaptor(lexer)
    }

    override fun createParser(project: Project?): PsiParser {
        val parser = CircomParser(null)
        return object : ANTLRParserAdaptor(CircomLanguage, parser) {
            override fun parse(parser: Parser, root: com.intellij.psi.tree.IElementType): ParseTree {
                return (parser as CircomParser).circuit()
            }
        }
    }

    override fun getFileNodeType(): IFileElementType {
        return CircomElementTypes.FILE
    }

    override fun getWhitespaceTokens(): TokenSet {
        return CircomTokenTypes.WHITESPACES
    }

    override fun getCommentTokens(): TokenSet {
        return CircomTokenTypes.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return CircomTokenTypes.STRINGS
    }

    override fun createElement(node: ASTNode): PsiElement {
        val elementType = node.elementType

        return when (elementType) {
            CircomElementTypes.TEMPLATE_DEFINITION -> CircomTemplateDefinitionImpl(node)
            CircomElementTypes.FUNCTION_DEFINITION -> CircomFunctionDefinitionImpl(node)
            CircomElementTypes.SIGNAL_DECLARATION -> CircomSignalDeclarationImpl(node)
            CircomElementTypes.VAR_DECLARATION -> CircomVariableDeclarationImpl(node)
            CircomElementTypes.COMPONENT_DECLARATION -> CircomComponentDeclarationImpl(node)
            CircomElementTypes.INCLUDE_DEFINITION -> CircomIncludeStatementImpl(node)
            CircomElementTypes.IDENTIFIER_STATEMENT -> CircomIdentifierStatementImpl(node)
            CircomElementTypes.PRIMARY_EXPRESSION -> CircomCallExpressionImpl(node)
            CircomElementTypes.COMPONENT_MAIN_DECLARATION -> CircomComponentMainDeclarationImpl(node)
            else -> ANTLRPsiNode(node)
        }
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return CircomPsiFile(viewProvider)
    }
}
