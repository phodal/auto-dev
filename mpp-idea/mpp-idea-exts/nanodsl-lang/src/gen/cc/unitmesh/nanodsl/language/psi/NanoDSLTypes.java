// This is a generated file. Not intended for manual editing.
package cc.unitmesh.nanodsl.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import cc.unitmesh.nanodsl.language.lexer.NanoDSLTokenType;
import cc.unitmesh.nanodsl.language.psi.impl.*;

public interface NanoDSLTypes {

  IElementType ACTION_CALL = new NanoDSLElementType("ACTION_CALL");
  IElementType ACTION_NAME = new NanoDSLElementType("ACTION_NAME");
  IElementType ARG = new NanoDSLElementType("ARG");
  IElementType ARG_LIST = new NanoDSLElementType("ARG_LIST");
  IElementType BINARY_EXPR = new NanoDSLElementType("BINARY_EXPR");
  IElementType COMPONENT_BODY = new NanoDSLElementType("COMPONENT_BODY");
  IElementType COMPONENT_DECL = new NanoDSLElementType("COMPONENT_DECL");
  IElementType COMPONENT_INSTANCE = new NanoDSLElementType("COMPONENT_INSTANCE");
  IElementType COMPONENT_INSTANCES = new NanoDSLElementType("COMPONENT_INSTANCES");
  IElementType COMPONENT_NAME = new NanoDSLElementType("COMPONENT_NAME");
  IElementType CONTENT_BLOCK = new NanoDSLElementType("CONTENT_BLOCK");
  IElementType EXPR = new NanoDSLElementType("EXPR");
  IElementType FOR_BLOCK = new NanoDSLElementType("FOR_BLOCK");
  IElementType IF_BLOCK = new NanoDSLElementType("IF_BLOCK");
  IElementType PARAM = new NanoDSLElementType("PARAM");
  IElementType PARAM_LIST = new NanoDSLElementType("PARAM_LIST");
  IElementType PROP = new NanoDSLElementType("PROP");
  IElementType PROPERTY_ASSIGNMENT = new NanoDSLElementType("PROPERTY_ASSIGNMENT");
  IElementType PROP_LIST = new NanoDSLElementType("PROP_LIST");
  IElementType REQUEST_BLOCK = new NanoDSLElementType("REQUEST_BLOCK");
  IElementType REQUEST_ENTRIES = new NanoDSLElementType("REQUEST_ENTRIES");
  IElementType REQUEST_ENTRY = new NanoDSLElementType("REQUEST_ENTRY");
  IElementType STATE_BLOCK = new NanoDSLElementType("STATE_BLOCK");
  IElementType STATE_ENTRIES = new NanoDSLElementType("STATE_ENTRIES");
  IElementType STATE_ENTRY = new NanoDSLElementType("STATE_ENTRY");
  IElementType TYPE_REF = new NanoDSLElementType("TYPE_REF");

  IElementType ALERT = new NanoDSLTokenType("Alert");
  IElementType BADGE = new NanoDSLTokenType("Badge");
  IElementType BIND_READ = new NanoDSLTokenType("<<");
  IElementType BIND_WRITE = new NanoDSLTokenType(":=");
  IElementType BODY = new NanoDSLTokenType("body");
  IElementType BOOL = new NanoDSLTokenType("bool");
  IElementType BOOLEAN = new NanoDSLTokenType("BOOLEAN");
  IElementType BUTTON = new NanoDSLTokenType("Button");
  IElementType CARD = new NanoDSLTokenType("Card");
  IElementType CHECKBOX = new NanoDSLTokenType("Checkbox");
  IElementType COLON = new NanoDSLTokenType(":");
  IElementType COMMA = new NanoDSLTokenType(",");
  IElementType COMMENT = new NanoDSLTokenType("COMMENT");
  IElementType COMPONENT = new NanoDSLTokenType("component");
  IElementType CONTENT = new NanoDSLTokenType("content");
  IElementType DATACHART = new NanoDSLTokenType("DataChart");
  IElementType DATATABLE = new NanoDSLTokenType("DataTable");
  IElementType DATEPICKER = new NanoDSLTokenType("DatePicker");
  IElementType DATERANGEPICKER = new NanoDSLTokenType("DateRangePicker");
  IElementType DELETE = new NanoDSLTokenType("DELETE");
  IElementType DIVIDER = new NanoDSLTokenType("Divider");
  IElementType DIV_EQUALS = new NanoDSLTokenType("/=");
  IElementType DOT = new NanoDSLTokenType(".");
  IElementType EQUALS = new NanoDSLTokenType("=");
  IElementType FETCH = new NanoDSLTokenType("Fetch");
  IElementType FLOAT = new NanoDSLTokenType("float");
  IElementType FOR = new NanoDSLTokenType("for");
  IElementType FORM = new NanoDSLTokenType("Form");
  IElementType GENCANVAS = new NanoDSLTokenType("GenCanvas");
  IElementType GET = new NanoDSLTokenType("GET");
  IElementType HEADERS = new NanoDSLTokenType("headers");
  IElementType HSTACK = new NanoDSLTokenType("HStack");
  IElementType IDENTIFIER = new NanoDSLTokenType("IDENTIFIER");
  IElementType IF = new NanoDSLTokenType("if");
  IElementType IMAGE = new NanoDSLTokenType("Image");
  IElementType IN = new NanoDSLTokenType("in");
  IElementType INPUT = new NanoDSLTokenType("Input");
  IElementType INT = new NanoDSLTokenType("int");
  IElementType LBRACE = new NanoDSLTokenType("{");
  IElementType LBRACKET = new NanoDSLTokenType("[");
  IElementType LPAREN = new NanoDSLTokenType("(");
  IElementType METHOD = new NanoDSLTokenType("method");
  IElementType MINUS_EQUALS = new NanoDSLTokenType("-=");
  IElementType MODAL = new NanoDSLTokenType("Modal");
  IElementType NAVIGATE = new NanoDSLTokenType("Navigate");
  IElementType NEWLINE = new NanoDSLTokenType("NEWLINE");
  IElementType NUMBER = new NanoDSLTokenType("NUMBER");
  IElementType NUMBERINPUT = new NanoDSLTokenType("NumberInput");
  IElementType ON_CLICK = new NanoDSLTokenType("on_click");
  IElementType ON_CLOSE = new NanoDSLTokenType("on_close");
  IElementType ON_ERROR = new NanoDSLTokenType("on_error");
  IElementType ON_ROW_CLICK = new NanoDSLTokenType("on_row_click");
  IElementType ON_SUCCESS = new NanoDSLTokenType("on_success");
  IElementType PARAMS = new NanoDSLTokenType("params");
  IElementType PATCH = new NanoDSLTokenType("PATCH");
  IElementType PLUS_EQUALS = new NanoDSLTokenType("+=");
  IElementType POST = new NanoDSLTokenType("POST");
  IElementType PROGRESS = new NanoDSLTokenType("Progress");
  IElementType PUT = new NanoDSLTokenType("PUT");
  IElementType QUERY = new NanoDSLTokenType("query");
  IElementType RADIO = new NanoDSLTokenType("Radio");
  IElementType RADIOGROUP = new NanoDSLTokenType("RadioGroup");
  IElementType RBRACE = new NanoDSLTokenType("}");
  IElementType RBRACKET = new NanoDSLTokenType("]");
  IElementType REPLACE = new NanoDSLTokenType("replace");
  IElementType REQUEST = new NanoDSLTokenType("request");
  IElementType RPAREN = new NanoDSLTokenType(")");
  IElementType SELECT = new NanoDSLTokenType("Select");
  IElementType SHOWTOAST = new NanoDSLTokenType("ShowToast");
  IElementType SLIDER = new NanoDSLTokenType("Slider");
  IElementType SMARTTEXTFIELD = new NanoDSLTokenType("SmartTextField");
  IElementType SPINNER = new NanoDSLTokenType("Spinner");
  IElementType SPLITVIEW = new NanoDSLTokenType("SplitView");
  IElementType STATE = new NanoDSLTokenType("state");
  IElementType STATEMUTATION = new NanoDSLTokenType("StateMutation");
  IElementType STRING = new NanoDSLTokenType("STRING");
  IElementType STRING_TYPE = new NanoDSLTokenType("string");
  IElementType SWITCH = new NanoDSLTokenType("Switch");
  IElementType TEXT = new NanoDSLTokenType("Text");
  IElementType TEXTAREA = new NanoDSLTokenType("TextArea");
  IElementType TIMES_EQUALS = new NanoDSLTokenType("*=");
  IElementType TO = new NanoDSLTokenType("to");
  IElementType URL = new NanoDSLTokenType("url");
  IElementType VSTACK = new NanoDSLTokenType("VStack");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ACTION_CALL) {
        return new NanoDSLActionCallImpl(node);
      }
      else if (type == ACTION_NAME) {
        return new NanoDSLActionNameImpl(node);
      }
      else if (type == ARG) {
        return new NanoDSLArgImpl(node);
      }
      else if (type == ARG_LIST) {
        return new NanoDSLArgListImpl(node);
      }
      else if (type == BINARY_EXPR) {
        return new NanoDSLBinaryExprImpl(node);
      }
      else if (type == COMPONENT_BODY) {
        return new NanoDSLComponentBodyImpl(node);
      }
      else if (type == COMPONENT_DECL) {
        return new NanoDSLComponentDeclImpl(node);
      }
      else if (type == COMPONENT_INSTANCE) {
        return new NanoDSLComponentInstanceImpl(node);
      }
      else if (type == COMPONENT_INSTANCES) {
        return new NanoDSLComponentInstancesImpl(node);
      }
      else if (type == COMPONENT_NAME) {
        return new NanoDSLComponentNameImpl(node);
      }
      else if (type == CONTENT_BLOCK) {
        return new NanoDSLContentBlockImpl(node);
      }
      else if (type == EXPR) {
        return new NanoDSLExprImpl(node);
      }
      else if (type == FOR_BLOCK) {
        return new NanoDSLForBlockImpl(node);
      }
      else if (type == IF_BLOCK) {
        return new NanoDSLIfBlockImpl(node);
      }
      else if (type == PARAM) {
        return new NanoDSLParamImpl(node);
      }
      else if (type == PARAM_LIST) {
        return new NanoDSLParamListImpl(node);
      }
      else if (type == PROP) {
        return new NanoDSLPropImpl(node);
      }
      else if (type == PROPERTY_ASSIGNMENT) {
        return new NanoDSLPropertyAssignmentImpl(node);
      }
      else if (type == PROP_LIST) {
        return new NanoDSLPropListImpl(node);
      }
      else if (type == REQUEST_BLOCK) {
        return new NanoDSLRequestBlockImpl(node);
      }
      else if (type == REQUEST_ENTRIES) {
        return new NanoDSLRequestEntriesImpl(node);
      }
      else if (type == REQUEST_ENTRY) {
        return new NanoDSLRequestEntryImpl(node);
      }
      else if (type == STATE_BLOCK) {
        return new NanoDSLStateBlockImpl(node);
      }
      else if (type == STATE_ENTRIES) {
        return new NanoDSLStateEntriesImpl(node);
      }
      else if (type == STATE_ENTRY) {
        return new NanoDSLStateEntryImpl(node);
      }
      else if (type == TYPE_REF) {
        return new NanoDSLTypeRefImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
