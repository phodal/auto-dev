package cc.unitmesh.nanodsl.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static cc.unitmesh.nanodsl.language.psi.NanoDSLTypes.*;
import com.intellij.psi.TokenType;

%%

%{
  public _NanoDSLLexer() {
    this((java.io.Reader)null);
  }
%}

%class NanoDSLLexer
%class _NanoDSLLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

SPACE                    = [ \t\n\x0B\f\r]+
IDENTIFIER               = [a-zA-Z_][a-zA-Z0-9_]*
NUMBER                   = [0-9]+(\.[0-9]+)?
BOOLEAN                  = true|false
STRING                   = \"([^\\\"\r\n]|\\[^\r\n])*\"
COMMENT                  = #[^\r\n]*
NEWLINE                  = \n | \r | \r\n
WHITE_SPACE              = [ \t]+
INDENT                   = [ ][ ][ ][ ]+

COLON                    = :
COMMA                    = ,
LPAREN                   = \(
RPAREN                   = \)
LBRACE                   = \{
RBRACE                   = \}
LBRACKET                 = \[
RBRACKET                 = \]
DOT                      = \.
EQUALS                   = =

// Operators
BIND_READ                = "<<"
BIND_WRITE               = ":="
PLUS_EQUALS              = "+="
MINUS_EQUALS             = "-="
TIMES_EQUALS             = "*="
DIV_EQUALS               = "/="

// Keywords
COMPONENT                = component
STATE                    = state
REQUEST                  = request
IF                       = if
FOR                      = for
IN                       = in
CONTENT                  = content
ON_CLICK                 = on_click
ON_SUCCESS               = on_success
ON_ERROR                 = on_error
ON_CLOSE                 = on_close
ON_ROW_CLICK             = on_row_click
URL                      = url
METHOD                   = method
BODY                     = body
HEADERS                  = headers
TO                       = to
PARAMS                   = params
QUERY                    = query
REPLACE                  = replace

// Layout Components
VSTACK                   = VStack
HSTACK                   = HStack
CARD                     = Card
SPLITVIEW                = SplitView
GENCANVAS                = GenCanvas
FORM                     = Form
MODAL                    = Modal

// Display Components
TEXT                     = Text
IMAGE                    = Image
BADGE                    = Badge
DIVIDER                  = Divider
ALERT                    = Alert
PROGRESS                 = Progress
SPINNER                  = Spinner
DATACHART                = DataChart
DATATABLE                = DataTable

// Input Components
BUTTON                   = Button
INPUT                    = Input
TEXTAREA                 = TextArea
SELECT                   = Select
CHECKBOX                 = Checkbox
RADIO                    = Radio
RADIOGROUP               = RadioGroup
SWITCH                   = Switch
NUMBERINPUT              = NumberInput
SMARTTEXTFIELD           = SmartTextField
SLIDER                   = Slider
DATEPICKER               = DatePicker
DATERANGEPICKER          = DateRangePicker

// Actions
NAVIGATE                 = Navigate
FETCH                    = Fetch
SHOWTOAST                = ShowToast
STATEMUTATION            = StateMutation

// HTTP Methods
GET                      = GET
POST                     = POST
PUT                      = PUT
PATCH                    = PATCH
DELETE                   = DELETE

// Types
INT                      = int
FLOAT                    = float
STRING_TYPE              = string
BOOL                     = bool

%%

<YYINITIAL> {
  {COMMENT}                { return COMMENT; }
  {NEWLINE}                { return NEWLINE; }
  {WHITE_SPACE}            { return com.intellij.psi.TokenType.WHITE_SPACE; }
  
  // Keywords
  {COMPONENT}              { return COMPONENT; }
  {STATE}                  { return STATE; }
  {REQUEST}                { return REQUEST; }
  {IF}                     { return IF; }
  {FOR}                    { return FOR; }
  {IN}                     { return IN; }
  {CONTENT}                { return CONTENT; }
  {ON_CLICK}               { return ON_CLICK; }
  {ON_SUCCESS}             { return ON_SUCCESS; }
  {ON_ERROR}               { return ON_ERROR; }
  {ON_CLOSE}               { return ON_CLOSE; }
  {ON_ROW_CLICK}           { return ON_ROW_CLICK; }
  {URL}                    { return URL; }
  {METHOD}                 { return METHOD; }
  {BODY}                   { return BODY; }
  {HEADERS}                { return HEADERS; }
  {TO}                     { return TO; }
  {PARAMS}                 { return PARAMS; }
  {QUERY}                  { return QUERY; }
  {REPLACE}                { return REPLACE; }

  // Layout Components
  {VSTACK}                 { return VSTACK; }
  {HSTACK}                 { return HSTACK; }
  {CARD}                   { return CARD; }
  {SPLITVIEW}              { return SPLITVIEW; }
  {GENCANVAS}              { return GENCANVAS; }
  {FORM}                   { return FORM; }
  {MODAL}                  { return MODAL; }

  // Display Components
  {TEXT}                   { return TEXT; }
  {IMAGE}                  { return IMAGE; }
  {BADGE}                  { return BADGE; }
  {DIVIDER}                { return DIVIDER; }
  {ALERT}                  { return ALERT; }
  {PROGRESS}               { return PROGRESS; }
  {SPINNER}                { return SPINNER; }
  {DATACHART}              { return DATACHART; }
  {DATATABLE}              { return DATATABLE; }

  // Input Components
  {BUTTON}                 { return BUTTON; }
  {INPUT}                  { return INPUT; }
  {TEXTAREA}               { return TEXTAREA; }
  {SELECT}                 { return SELECT; }
  {CHECKBOX}               { return CHECKBOX; }
  {RADIO}                  { return RADIO; }
  {RADIOGROUP}             { return RADIOGROUP; }
  {SWITCH}                 { return SWITCH; }
  {NUMBERINPUT}            { return NUMBERINPUT; }
  {SMARTTEXTFIELD}         { return SMARTTEXTFIELD; }
  {SLIDER}                 { return SLIDER; }
  {DATEPICKER}             { return DATEPICKER; }
  {DATERANGEPICKER}        { return DATERANGEPICKER; }

  // Actions
  {NAVIGATE}               { return NAVIGATE; }
  {FETCH}                  { return FETCH; }
  {SHOWTOAST}              { return SHOWTOAST; }
  {STATEMUTATION}          { return STATEMUTATION; }

  // HTTP Methods
  {GET}                    { return GET; }
  {POST}                   { return POST; }
  {PUT}                    { return PUT; }
  {PATCH}                  { return PATCH; }
  {DELETE}                 { return DELETE; }

  // Types
  {INT}                    { return INT; }
  {FLOAT}                  { return FLOAT; }
  {STRING_TYPE}            { return STRING_TYPE; }
  {BOOL}                   { return BOOL; }

  // Operators
  {BIND_READ}              { return BIND_READ; }
  {BIND_WRITE}             { return BIND_WRITE; }
  {PLUS_EQUALS}            { return PLUS_EQUALS; }
  {MINUS_EQUALS}           { return MINUS_EQUALS; }
  {TIMES_EQUALS}           { return TIMES_EQUALS; }
  {DIV_EQUALS}             { return DIV_EQUALS; }

  // Literals
  {BOOLEAN}                { return BOOLEAN; }
  {NUMBER}                 { return NUMBER; }
  {STRING}                 { return STRING; }

  // Punctuation
  {COLON}                  { return COLON; }
  {COMMA}                  { return COMMA; }
  {LPAREN}                 { return LPAREN; }
  {RPAREN}                 { return RPAREN; }
  {LBRACE}                 { return LBRACE; }
  {RBRACE}                 { return RBRACE; }
  {LBRACKET}               { return LBRACKET; }
  {RBRACKET}               { return RBRACKET; }
  {DOT}                    { return DOT; }
  {EQUALS}                 { return EQUALS; }

  // Identifier (must be last to avoid conflicts)
  {IDENTIFIER}             { return IDENTIFIER; }

  [^]                      { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

