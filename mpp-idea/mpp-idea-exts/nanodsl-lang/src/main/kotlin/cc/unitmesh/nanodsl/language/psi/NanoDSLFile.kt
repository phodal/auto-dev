package cc.unitmesh.nanodsl.language.psi

import cc.unitmesh.nanodsl.language.NanoDSLFileType
import cc.unitmesh.nanodsl.language.NanoDSLLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class NanoDSLFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, NanoDSLLanguage) {
    override fun getFileType(): FileType = NanoDSLFileType
    
    override fun toString(): String = "NanoDSL File"
}

