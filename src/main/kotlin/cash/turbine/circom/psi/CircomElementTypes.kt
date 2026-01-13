package cash.turbine.circom.psi

import cash.turbine.circom.lang.CircomLanguage
import cash.turbine.circom.lang.CircomTokenTypes
import cash.turbine.circom.parser.CircomParser
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.RuleIElementType

object CircomElementTypes {
    val FILE = IFileElementType(CircomLanguage)

    init {
        CircomTokenTypes.initializeElementTypeFactory()
    }

    private val ruleTypes: List<RuleIElementType> = PSIElementTypeFactory.getRuleIElementTypes(CircomLanguage)

    val CIRCUIT: IElementType = ruleTypes[CircomParser.RULE_circuit]
    val TEMPLATE_DEFINITION: IElementType = ruleTypes[CircomParser.RULE_templateDefinition]
    val FUNCTION_DEFINITION: IElementType = ruleTypes[CircomParser.RULE_functionDefinition]
    val BUS_DEFINITION: IElementType = ruleTypes[CircomParser.RULE_busDefinition]
    val SIGNAL_DECLARATION: IElementType = ruleTypes[CircomParser.RULE_signalDeclaration]
    val VAR_DECLARATION: IElementType = ruleTypes[CircomParser.RULE_varDeclaration]
    val COMPONENT_DECLARATION: IElementType = ruleTypes[CircomParser.RULE_componentDeclaration]
    val COMPONENT_MAIN_DECLARATION: IElementType = ruleTypes[CircomParser.RULE_componentMainDeclaration]
    val INCLUDE_DEFINITION: IElementType = ruleTypes[CircomParser.RULE_includeDefinition]
    val PRAGMA_DEFINITION: IElementType = ruleTypes[CircomParser.RULE_pragmaDefinition]
    val BODY: IElementType = ruleTypes[CircomParser.RULE_body]
    val STATEMENTS: IElementType = ruleTypes[CircomParser.RULE_statements]
    val EXPRESSION: IElementType = ruleTypes[CircomParser.RULE_expression]
    val PRIMARY_EXPRESSION: IElementType = ruleTypes[CircomParser.RULE_primaryExpression]
    val IDENTIFIER: IElementType = ruleTypes[CircomParser.RULE_identifier]
    val IDENTIFIER_STATEMENT: IElementType = ruleTypes[CircomParser.RULE_identifierStatement]
    val VAR_IDENTIFIER: IElementType = ruleTypes[CircomParser.RULE_varIdentifier]
    val SIGNAL_IDENTIFIER: IElementType = ruleTypes[CircomParser.RULE_signalIdentifier]
    val SIGNAL_HEADER: IElementType = ruleTypes[CircomParser.RULE_signalHeader]

    val DEFINITIONS: TokenSet = TokenSet.create(
        TEMPLATE_DEFINITION,
        FUNCTION_DEFINITION,
        BUS_DEFINITION
    )

    val DECLARATIONS: TokenSet = TokenSet.create(
        SIGNAL_DECLARATION,
        VAR_DECLARATION,
        COMPONENT_DECLARATION
    )
}
