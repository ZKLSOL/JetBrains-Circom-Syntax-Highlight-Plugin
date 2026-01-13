package cash.turbine.circom.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project

class CircomNamesValidator : NamesValidator {

    private val keywords = setOf(
        "pragma", "circom", "include",
        "template", "function", "component", "main", "public",
        "signal", "input", "output", "var",
        "if", "else", "for", "while", "do", "return",
        "parallel", "custom", "bus",
        "log", "assert"
    )

    override fun isKeyword(name: String, project: Project?): Boolean {
        return name in keywords
    }

    override fun isIdentifier(name: String, project: Project?): Boolean {
        if (name.isEmpty()) return false
        if (isKeyword(name, project)) return false

        val first = name[0]
        if (!first.isLetter() && first != '_' && first != '$') return false

        return name.drop(1).all { it.isLetterOrDigit() || it == '_' || it == '$' }
    }
}
