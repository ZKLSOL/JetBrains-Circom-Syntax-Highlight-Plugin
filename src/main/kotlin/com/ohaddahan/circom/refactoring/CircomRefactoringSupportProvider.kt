package com.ohaddahan.circom.refactoring

import com.ohaddahan.circom.psi.CircomNamedElement
import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement

class CircomRefactoringSupportProvider : RefactoringSupportProvider() {

    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is CircomNamedElement
    }

    override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is CircomNamedElement
    }

    override fun isSafeDeleteAvailable(element: PsiElement): Boolean {
        return element is CircomNamedElement
    }
}
