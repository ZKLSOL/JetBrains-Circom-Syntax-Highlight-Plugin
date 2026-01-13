package cash.turbine.circom.reference

import cash.turbine.circom.lang.CircomTokenTypes
import cash.turbine.circom.parser.CircomLexer
import cash.turbine.circom.psi.CircomNamedElement
import cash.turbine.circom.psi.CircomPsiFile
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.antlr.intellij.adaptor.lexer.TokenIElementType

class CircomReferenceContributor : PsiReferenceContributor() {

    init {
        CircomTokenTypes.initializeElementTypeFactory()
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    // Only process elements in Circom files
                    if (element.containingFile !is CircomPsiFile) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val elementType = element.node?.elementType

                    // Check if this is an ID token
                    val isIdToken = elementType is TokenIElementType &&
                                    elementType.antlrTokenType == CircomLexer.ID

                    if (!isIdToken) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    // Skip if this element is the name identifier of a definition
                    val namedParent = PsiTreeUtil.getParentOfType(element, CircomNamedElement::class.java)
                    if (namedParent != null && namedParent.nameIdentifier == element) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val text = element.text
                    return arrayOf(
                        CircomReference(element, TextRange(0, text.length))
                    )
                }
            }
        )
    }
}
