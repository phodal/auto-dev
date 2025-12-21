package cc.unitmesh.nanodsl.language.lexer

import cc.unitmesh.nanodsl.language.NanoDSLLanguage
import com.intellij.psi.tree.IElementType

class NanoDSLTokenType(debugName: String) : IElementType(debugName, NanoDSLLanguage) {
    override fun toString(): String = "NanoDSLTokenType." + super.toString()
}

