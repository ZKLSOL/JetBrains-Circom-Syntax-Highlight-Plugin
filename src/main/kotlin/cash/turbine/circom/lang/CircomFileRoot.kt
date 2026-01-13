package cash.turbine.circom.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class CircomFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CircomLanguage) {

    override fun getFileType(): FileType {
        return CircomFileType
    }

    override fun toString(): String {
        return "Circom file"
    }
}
