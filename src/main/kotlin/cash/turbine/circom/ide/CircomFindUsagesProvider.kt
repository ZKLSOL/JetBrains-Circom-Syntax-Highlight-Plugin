package cash.turbine.circom.ide

import cash.turbine.circom.lang.CircomLanguage
import cash.turbine.circom.lang.CircomTokenTypes
import cash.turbine.circom.psi.CircomNamedElement
import cash.turbine.circom.psi.impl.*
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import cash.turbine.circom.adaptors.CircomLexerAdaptor
import cash.turbine.circom.parser.CircomLexer

class CircomFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            CircomLexerAdaptor(CircomLexer(null)),
            CircomTokenTypes.IDENTIFIERS,
            CircomTokenTypes.COMMENTS,
            CircomTokenTypes.STRINGS
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is CircomNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        return when (element) {
            is CircomTemplateDefinitionImpl -> "template"
            is CircomFunctionDefinitionImpl -> "function"
            is CircomSignalDeclarationImpl -> "signal"
            is CircomVariableDeclarationImpl -> "variable"
            is CircomComponentDeclarationImpl -> "component"
            else -> "element"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when (element) {
            is CircomNamedElement -> element.name ?: "<unnamed>"
            else -> element.text
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return when (element) {
            is CircomTemplateDefinitionImpl -> "template ${element.name ?: "<unnamed>"}"
            is CircomFunctionDefinitionImpl -> "function ${element.name ?: "<unnamed>"}"
            is CircomSignalDeclarationImpl -> "${element.signalType} signal ${element.name ?: "<unnamed>"}"
            is CircomVariableDeclarationImpl -> "var ${element.name ?: "<unnamed>"}"
            is CircomComponentDeclarationImpl -> "component ${element.name ?: "<unnamed>"}"
            is CircomNamedElement -> element.name ?: "<unnamed>"
            else -> element.text
        }
    }
}
