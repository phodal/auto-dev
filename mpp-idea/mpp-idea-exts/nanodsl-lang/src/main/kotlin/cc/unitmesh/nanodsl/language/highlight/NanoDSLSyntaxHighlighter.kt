package cc.unitmesh.nanodsl.language.highlight

import cc.unitmesh.nanodsl.language.lexer.NanoDSLLexerAdapter
import cc.unitmesh.nanodsl.language.psi.NanoDSLTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class NanoDSLSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = NanoDSLLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return pack(ATTRIBUTES[tokenType])
    }

    companion object {
        private val ATTRIBUTES: MutableMap<IElementType, TextAttributesKey> = HashMap()

        // Keywords
        private val KEYWORDS: TokenSet = TokenSet.create(
            NanoDSLTypes.COMPONENT,
            NanoDSLTypes.STATE,
            NanoDSLTypes.REQUEST,
            NanoDSLTypes.IF,
            NanoDSLTypes.FOR,
            NanoDSLTypes.IN,
            NanoDSLTypes.CONTENT,
        )

        // Event handlers
        private val EVENT_HANDLERS: TokenSet = TokenSet.create(
            NanoDSLTypes.ON_CLICK,
            NanoDSLTypes.ON_SUCCESS,
            NanoDSLTypes.ON_ERROR,
            NanoDSLTypes.ON_CLOSE,
            NanoDSLTypes.ON_ROW_CLICK,
        )

        // Request properties
        private val REQUEST_PROPS: TokenSet = TokenSet.create(
            NanoDSLTypes.URL,
            NanoDSLTypes.METHOD,
            NanoDSLTypes.BODY,
            NanoDSLTypes.HEADERS,
            NanoDSLTypes.TO,
            NanoDSLTypes.PARAMS,
            NanoDSLTypes.QUERY,
            NanoDSLTypes.REPLACE,
        )

        // Layout components
        private val LAYOUT_COMPONENTS: TokenSet = TokenSet.create(
            NanoDSLTypes.VSTACK,
            NanoDSLTypes.HSTACK,
            NanoDSLTypes.CARD,
            NanoDSLTypes.SPLITVIEW,
            NanoDSLTypes.GENCANVAS,
            NanoDSLTypes.FORM,
            NanoDSLTypes.MODAL,
        )

        // Display components
        private val DISPLAY_COMPONENTS: TokenSet = TokenSet.create(
            NanoDSLTypes.TEXT,
            NanoDSLTypes.IMAGE,
            NanoDSLTypes.BADGE,
            NanoDSLTypes.DIVIDER,
            NanoDSLTypes.ALERT,
            NanoDSLTypes.PROGRESS,
            NanoDSLTypes.SPINNER,
            NanoDSLTypes.DATACHART,
            NanoDSLTypes.DATATABLE,
        )

        // Input components
        private val INPUT_COMPONENTS: TokenSet = TokenSet.create(
            NanoDSLTypes.BUTTON,
            NanoDSLTypes.INPUT,
            NanoDSLTypes.TEXTAREA,
            NanoDSLTypes.SELECT,
            NanoDSLTypes.CHECKBOX,
            NanoDSLTypes.RADIO,
            NanoDSLTypes.RADIOGROUP,
            NanoDSLTypes.SWITCH,
            NanoDSLTypes.NUMBERINPUT,
            NanoDSLTypes.SMARTTEXTFIELD,
            NanoDSLTypes.SLIDER,
            NanoDSLTypes.DATEPICKER,
            NanoDSLTypes.DATERANGEPICKER,
        )

        // Actions
        private val ACTIONS: TokenSet = TokenSet.create(
            NanoDSLTypes.NAVIGATE,
            NanoDSLTypes.FETCH,
            NanoDSLTypes.SHOWTOAST,
            NanoDSLTypes.STATEMUTATION,
        )

        // HTTP methods
        private val HTTP_METHODS: TokenSet = TokenSet.create(
            NanoDSLTypes.GET,
            NanoDSLTypes.POST,
            NanoDSLTypes.PUT,
            NanoDSLTypes.PATCH,
            NanoDSLTypes.DELETE,
        )

        // Types
        private val TYPES: TokenSet = TokenSet.create(
            NanoDSLTypes.INT,
            NanoDSLTypes.FLOAT,
            NanoDSLTypes.STRING_TYPE,
            NanoDSLTypes.BOOL,
        )

        // Operators
        private val OPERATORS: TokenSet = TokenSet.create(
            NanoDSLTypes.BIND_READ,
            NanoDSLTypes.BIND_WRITE,
            NanoDSLTypes.PLUS_EQUALS,
            NanoDSLTypes.MINUS_EQUALS,
            NanoDSLTypes.TIMES_EQUALS,
            NanoDSLTypes.DIV_EQUALS,
        )

        init {
            // Keywords - bold and prominent
            fillMap(ATTRIBUTES, KEYWORDS, DefaultLanguageHighlighterColors.KEYWORD)
            
            // Event handlers - function-like
            fillMap(ATTRIBUTES, EVENT_HANDLERS, DefaultLanguageHighlighterColors.INSTANCE_METHOD)
            
            // Request properties - metadata-like
            fillMap(ATTRIBUTES, REQUEST_PROPS, DefaultLanguageHighlighterColors.METADATA)
            
            // Components - class-like
            fillMap(ATTRIBUTES, LAYOUT_COMPONENTS, DefaultLanguageHighlighterColors.CLASS_NAME)
            fillMap(ATTRIBUTES, DISPLAY_COMPONENTS, DefaultLanguageHighlighterColors.CLASS_NAME)
            fillMap(ATTRIBUTES, INPUT_COMPONENTS, DefaultLanguageHighlighterColors.CLASS_NAME)
            
            // Actions - static method-like
            fillMap(ATTRIBUTES, ACTIONS, DefaultLanguageHighlighterColors.STATIC_METHOD)
            
            // HTTP methods - constant-like
            fillMap(ATTRIBUTES, HTTP_METHODS, DefaultLanguageHighlighterColors.CONSTANT)
            
            // Types - type-like
            fillMap(ATTRIBUTES, TYPES, DefaultLanguageHighlighterColors.CLASS_REFERENCE)

            // Operators - operator-like
            fillMap(ATTRIBUTES, OPERATORS, DefaultLanguageHighlighterColors.OPERATION_SIGN)

            // Literals
            ATTRIBUTES[NanoDSLTypes.STRING] = DefaultLanguageHighlighterColors.STRING
            ATTRIBUTES[NanoDSLTypes.NUMBER] = DefaultLanguageHighlighterColors.NUMBER
            ATTRIBUTES[NanoDSLTypes.BOOLEAN] = DefaultLanguageHighlighterColors.KEYWORD

            // Comments
            ATTRIBUTES[NanoDSLTypes.COMMENT] = DefaultLanguageHighlighterColors.LINE_COMMENT

            // Identifiers
            ATTRIBUTES[NanoDSLTypes.IDENTIFIER] = DefaultLanguageHighlighterColors.IDENTIFIER

            // Punctuation
            ATTRIBUTES[NanoDSLTypes.COLON] = DefaultLanguageHighlighterColors.OPERATION_SIGN
            ATTRIBUTES[NanoDSLTypes.COMMA] = DefaultLanguageHighlighterColors.COMMA
            ATTRIBUTES[NanoDSLTypes.DOT] = DefaultLanguageHighlighterColors.DOT
            ATTRIBUTES[NanoDSLTypes.EQUALS] = DefaultLanguageHighlighterColors.OPERATION_SIGN

            // Brackets
            ATTRIBUTES[NanoDSLTypes.LPAREN] = DefaultLanguageHighlighterColors.PARENTHESES
            ATTRIBUTES[NanoDSLTypes.RPAREN] = DefaultLanguageHighlighterColors.PARENTHESES
            ATTRIBUTES[NanoDSLTypes.LBRACE] = DefaultLanguageHighlighterColors.BRACES
            ATTRIBUTES[NanoDSLTypes.RBRACE] = DefaultLanguageHighlighterColors.BRACES
            ATTRIBUTES[NanoDSLTypes.LBRACKET] = DefaultLanguageHighlighterColors.BRACKETS
            ATTRIBUTES[NanoDSLTypes.RBRACKET] = DefaultLanguageHighlighterColors.BRACKETS
        }
    }
}

