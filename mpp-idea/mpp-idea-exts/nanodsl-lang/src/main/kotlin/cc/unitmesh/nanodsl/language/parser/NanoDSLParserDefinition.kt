package cc.unitmesh.nanodsl.language.parser

import cc.unitmesh.nanodsl.language.lexer.NanoDSLLexerAdapter
import cc.unitmesh.nanodsl.language.psi.NanoDSLFile
import cc.unitmesh.nanodsl.language.psi.NanoDSLTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class NanoDSLParserDefinition : ParserDefinition {
    companion object {
        val FILE = IFileElementType(cc.unitmesh.nanodsl.language.NanoDSLLanguage)
        val COMMENTS = TokenSet.create(NanoDSLTypes.COMMENT)
        val WHITESPACES = TokenSet.EMPTY
    }

    override fun createLexer(project: Project?): Lexer = NanoDSLLexerAdapter()

    override fun createParser(project: Project?): PsiParser = NanoDSLParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(NanoDSLTypes.STRING)

    override fun getWhitespaceTokens(): TokenSet = WHITESPACES

    override fun createElement(node: ASTNode?): PsiElement = NanoDSLTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = NanoDSLFile(viewProvider)
}

