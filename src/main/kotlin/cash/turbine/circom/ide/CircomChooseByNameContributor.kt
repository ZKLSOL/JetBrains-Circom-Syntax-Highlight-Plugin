package cash.turbine.circom.ide

import cash.turbine.circom.psi.CircomNamedElement
import cash.turbine.circom.stubs.CircomNameIndex
import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project

class CircomChooseByNameContributor : ChooseByNameContributor {

    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        val names = mutableListOf<String>()

        CircomNameIndex.findAllTemplates(project).mapNotNull { it.name }.forEach { names.add(it) }
        CircomNameIndex.findAllFunctions(project).mapNotNull { it.name }.forEach { names.add(it) }

        return names.toTypedArray()
    }

    override fun getItemsByName(
        name: String,
        pattern: String,
        project: Project,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        val items = mutableListOf<NavigationItem>()

        CircomNameIndex.findTemplates(project, name).forEach { items.add(it) }
        CircomNameIndex.findFunctions(project, name).forEach { items.add(it) }

        return items.toTypedArray()
    }
}
