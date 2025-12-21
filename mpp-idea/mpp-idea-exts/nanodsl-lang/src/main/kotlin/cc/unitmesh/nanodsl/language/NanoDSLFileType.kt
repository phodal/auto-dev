package cc.unitmesh.nanodsl.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object NanoDSLFileType : LanguageFileType(NanoDSLLanguage) {
    override fun getName(): String = "NanoDSL"
    
    override fun getDescription(): String = "NanoDSL Language File"
    
    override fun getDefaultExtension(): String = "nanodsl"
    
    override fun getIcon(): Icon? = null
}

