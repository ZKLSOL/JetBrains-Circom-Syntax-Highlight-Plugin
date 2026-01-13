package cash.turbine.circom.ide

import cash.turbine.circom.psi.CircomPsiFile
import cash.turbine.circom.psi.impl.*
import com.intellij.ide.structureView.*
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon

class CircomStructureViewFactory : PsiStructureViewFactory {

    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        if (psiFile !is CircomPsiFile) return null

        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return CircomStructureViewModel(psiFile, editor)
            }
        }
    }
}

class CircomStructureViewModel(
    psiFile: CircomPsiFile,
    editor: Editor?
) : StructureViewModelBase(psiFile, editor, CircomStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        val value = element.value
        return value !is CircomPsiFile &&
               value !is CircomTemplateDefinitionImpl &&
               value !is CircomFunctionDefinitionImpl
    }

    override fun getSuitableClasses(): Array<Class<*>> {
        return arrayOf(
            CircomTemplateDefinitionImpl::class.java,
            CircomFunctionDefinitionImpl::class.java,
            CircomSignalDeclarationImpl::class.java,
            CircomVariableDeclarationImpl::class.java,
            CircomComponentDeclarationImpl::class.java
        )
    }
}

class CircomStructureViewElement(private val element: PsiElement) : StructureViewTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) {
        if (element is NavigatablePsiElement) {
            element.navigate(requestFocus)
        }
    }

    override fun canNavigate(): Boolean = element is NavigatablePsiElement

    override fun canNavigateToSource(): Boolean = canNavigate()

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String {
                return when (element) {
                    is CircomPsiFile -> element.name
                    is CircomTemplateDefinitionImpl -> "template ${element.name ?: "<unnamed>"}"
                    is CircomFunctionDefinitionImpl -> "function ${element.name ?: "<unnamed>"}"
                    is CircomSignalDeclarationImpl -> "${element.signalType} ${element.name ?: "<unnamed>"}"
                    is CircomVariableDeclarationImpl -> "var ${element.name ?: "<unnamed>"}"
                    is CircomComponentDeclarationImpl -> "component ${element.name ?: "<unnamed>"}"
                    else -> element.text.take(30)
                }
            }

            override fun getLocationString(): String? = null

            override fun getIcon(unused: Boolean): Icon? = null
        }
    }

    override fun getChildren(): Array<TreeElement> {
        val children = mutableListOf<TreeElement>()

        when (element) {
            is CircomPsiFile -> {
                // Add templates and functions as top-level children
                element.templates.forEach { children.add(CircomStructureViewElement(it)) }
                element.functions.forEach { children.add(CircomStructureViewElement(it)) }
            }
            is CircomTemplateDefinitionImpl -> {
                // Add signals, variables, and components within template
                PsiTreeUtil.findChildrenOfType(element, CircomSignalDeclarationImpl::class.java)
                    .forEach { children.add(CircomStructureViewElement(it)) }
                PsiTreeUtil.findChildrenOfType(element, CircomVariableDeclarationImpl::class.java)
                    .forEach { children.add(CircomStructureViewElement(it)) }
                PsiTreeUtil.findChildrenOfType(element, CircomComponentDeclarationImpl::class.java)
                    .forEach { children.add(CircomStructureViewElement(it)) }
            }
            is CircomFunctionDefinitionImpl -> {
                // Add variables within function
                PsiTreeUtil.findChildrenOfType(element, CircomVariableDeclarationImpl::class.java)
                    .forEach { children.add(CircomStructureViewElement(it)) }
            }
        }

        return children.toTypedArray()
    }
}
