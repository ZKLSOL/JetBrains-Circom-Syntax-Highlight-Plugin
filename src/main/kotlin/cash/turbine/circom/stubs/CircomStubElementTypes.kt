package cash.turbine.circom.stubs

import cash.turbine.circom.lang.CircomLanguage
import cash.turbine.circom.psi.impl.CircomFunctionDefinitionImpl
import cash.turbine.circom.psi.impl.CircomTemplateDefinitionImpl
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType

object CircomStubElementTypes {
    val FILE: IStubFileElementType<CircomFileStub> = object : IStubFileElementType<CircomFileStub>(CircomLanguage) {
        override fun getStubVersion(): Int = 1

        override fun getExternalId(): String = "circom.file"

        override fun serialize(stub: CircomFileStub, dataStream: StubOutputStream) {}

        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): CircomFileStub {
            return CircomFileStub(null)
        }
    }
}

class CircomFileStub(file: cash.turbine.circom.psi.CircomPsiFile?) : PsiFileStubImpl<cash.turbine.circom.psi.CircomPsiFile>(file) {
    override fun getType(): IStubFileElementType<CircomFileStub> = CircomStubElementTypes.FILE
}
