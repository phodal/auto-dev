// This is a generated file. Not intended for manual editing.
package cc.unitmesh.nanodsl.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static cc.unitmesh.nanodsl.language.psi.NanoDSLTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class NanoDSLParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return NanoDSLFile(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // (componentDecl | NEWLINE | COMMENT)*
  static boolean NanoDSLFile(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "NanoDSLFile")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!NanoDSLFile_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "NanoDSLFile", pos_)) break;
    }
    return true;
  }

  // componentDecl | NEWLINE | COMMENT
  private static boolean NanoDSLFile_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "NanoDSLFile_0")) return false;
    boolean result_;
    result_ = componentDecl(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEWLINE);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // actionName LPAREN argList? RPAREN
  public static boolean actionCall(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "actionCall")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACTION_CALL, "<action call>");
    result_ = actionName(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LPAREN);
    result_ = result_ && actionCall_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // argList?
  private static boolean actionCall_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "actionCall_2")) return false;
    argList(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // NAVIGATE | FETCH | SHOWTOAST | STATEMUTATION
  public static boolean actionName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "actionName")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ACTION_NAME, "<action name>");
    result_ = consumeToken(builder_, NAVIGATE);
    if (!result_) result_ = consumeToken(builder_, FETCH);
    if (!result_) result_ = consumeToken(builder_, SHOWTOAST);
    if (!result_) result_ = consumeToken(builder_, STATEMUTATION);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // expr | prop
  public static boolean arg(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arg")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARG, "<arg>");
    result_ = expr(builder_, level_ + 1);
    if (!result_) result_ = prop(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // arg (COMMA arg)*
  public static boolean argList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argList")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARG_LIST, "<arg list>");
    result_ = arg(builder_, level_ + 1);
    result_ = result_ && argList_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (COMMA arg)*
  private static boolean argList_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argList_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!argList_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "argList_1", pos_)) break;
    }
    return true;
  }

  // COMMA arg
  private static boolean argList_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argList_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && arg(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // expr (PLUS_EQUALS | MINUS_EQUALS | TIMES_EQUALS | DIV_EQUALS) expr
  public static boolean binaryExpr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "binaryExpr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BINARY_EXPR, "<binary expr>");
    result_ = expr(builder_, level_ + 1);
    result_ = result_ && binaryExpr_1(builder_, level_ + 1);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // PLUS_EQUALS | MINUS_EQUALS | TIMES_EQUALS | DIV_EQUALS
  private static boolean binaryExpr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "binaryExpr_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, PLUS_EQUALS);
    if (!result_) result_ = consumeToken(builder_, MINUS_EQUALS);
    if (!result_) result_ = consumeToken(builder_, TIMES_EQUALS);
    if (!result_) result_ = consumeToken(builder_, DIV_EQUALS);
    return result_;
  }

  /* ********************************************************** */
  // (stateBlock | requestBlock | propertyAssignment | contentBlock | componentInstance | ifBlock | forBlock | NEWLINE | COMMENT)*
  public static boolean componentBody(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentBody")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMPONENT_BODY, "<component body>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!componentBody_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "componentBody", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  // stateBlock | requestBlock | propertyAssignment | contentBlock | componentInstance | ifBlock | forBlock | NEWLINE | COMMENT
  private static boolean componentBody_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentBody_0")) return false;
    boolean result_;
    result_ = stateBlock(builder_, level_ + 1);
    if (!result_) result_ = requestBlock(builder_, level_ + 1);
    if (!result_) result_ = propertyAssignment(builder_, level_ + 1);
    if (!result_) result_ = contentBlock(builder_, level_ + 1);
    if (!result_) result_ = componentInstance(builder_, level_ + 1);
    if (!result_) result_ = ifBlock(builder_, level_ + 1);
    if (!result_) result_ = forBlock(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEWLINE);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // COMPONENT IDENTIFIER paramList? COLON NEWLINE componentBody
  public static boolean componentDecl(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentDecl")) return false;
    if (!nextTokenIs(builder_, COMPONENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, COMPONENT, IDENTIFIER);
    result_ = result_ && componentDecl_2(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COLON, NEWLINE);
    result_ = result_ && componentBody(builder_, level_ + 1);
    exit_section_(builder_, marker_, COMPONENT_DECL, result_);
    return result_;
  }

  // paramList?
  private static boolean componentDecl_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentDecl_2")) return false;
    paramList(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // componentName propList? COLON NEWLINE componentBody
  //                     | componentName LPAREN argList? RPAREN COLON? NEWLINE? componentBody?
  //                     | componentName LPAREN argList? RPAREN NEWLINE?
  public static boolean componentInstance(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMPONENT_INSTANCE, "<component instance>");
    result_ = componentInstance_0(builder_, level_ + 1);
    if (!result_) result_ = componentInstance_1(builder_, level_ + 1);
    if (!result_) result_ = componentInstance_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // componentName propList? COLON NEWLINE componentBody
  private static boolean componentInstance_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && componentInstance_0_1(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COLON, NEWLINE);
    result_ = result_ && componentBody(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // propList?
  private static boolean componentInstance_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_0_1")) return false;
    propList(builder_, level_ + 1);
    return true;
  }

  // componentName LPAREN argList? RPAREN COLON? NEWLINE? componentBody?
  private static boolean componentInstance_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LPAREN);
    result_ = result_ && componentInstance_1_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    result_ = result_ && componentInstance_1_4(builder_, level_ + 1);
    result_ = result_ && componentInstance_1_5(builder_, level_ + 1);
    result_ = result_ && componentInstance_1_6(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // argList?
  private static boolean componentInstance_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_1_2")) return false;
    argList(builder_, level_ + 1);
    return true;
  }

  // COLON?
  private static boolean componentInstance_1_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_1_4")) return false;
    consumeToken(builder_, COLON);
    return true;
  }

  // NEWLINE?
  private static boolean componentInstance_1_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_1_5")) return false;
    consumeToken(builder_, NEWLINE);
    return true;
  }

  // componentBody?
  private static boolean componentInstance_1_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_1_6")) return false;
    componentBody(builder_, level_ + 1);
    return true;
  }

  // componentName LPAREN argList? RPAREN NEWLINE?
  private static boolean componentInstance_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = componentName(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LPAREN);
    result_ = result_ && componentInstance_2_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    result_ = result_ && componentInstance_2_4(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // argList?
  private static boolean componentInstance_2_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_2_2")) return false;
    argList(builder_, level_ + 1);
    return true;
  }

  // NEWLINE?
  private static boolean componentInstance_2_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstance_2_4")) return false;
    consumeToken(builder_, NEWLINE);
    return true;
  }

  /* ********************************************************** */
  // (componentInstance | NEWLINE | COMMENT)*
  public static boolean componentInstances(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstances")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMPONENT_INSTANCES, "<component instances>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!componentInstances_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "componentInstances", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  // componentInstance | NEWLINE | COMMENT
  private static boolean componentInstances_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentInstances_0")) return false;
    boolean result_;
    result_ = componentInstance(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEWLINE);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // VSTACK | HSTACK | CARD | SPLITVIEW | GENCANVAS | FORM | MODAL
  //                 | TEXT | IMAGE | BADGE | DIVIDER | ALERT | PROGRESS | SPINNER | DATACHART | DATATABLE
  //                 | BUTTON | INPUT | TEXTAREA | SELECT | CHECKBOX | RADIO | RADIOGROUP | SWITCH
  //                 | NUMBERINPUT | SMARTTEXTFIELD | SLIDER | DATEPICKER | DATERANGEPICKER
  //                 | IDENTIFIER
  public static boolean componentName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "componentName")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMPONENT_NAME, "<component name>");
    result_ = consumeToken(builder_, VSTACK);
    if (!result_) result_ = consumeToken(builder_, HSTACK);
    if (!result_) result_ = consumeToken(builder_, CARD);
    if (!result_) result_ = consumeToken(builder_, SPLITVIEW);
    if (!result_) result_ = consumeToken(builder_, GENCANVAS);
    if (!result_) result_ = consumeToken(builder_, FORM);
    if (!result_) result_ = consumeToken(builder_, MODAL);
    if (!result_) result_ = consumeToken(builder_, TEXT);
    if (!result_) result_ = consumeToken(builder_, IMAGE);
    if (!result_) result_ = consumeToken(builder_, BADGE);
    if (!result_) result_ = consumeToken(builder_, DIVIDER);
    if (!result_) result_ = consumeToken(builder_, ALERT);
    if (!result_) result_ = consumeToken(builder_, PROGRESS);
    if (!result_) result_ = consumeToken(builder_, SPINNER);
    if (!result_) result_ = consumeToken(builder_, DATACHART);
    if (!result_) result_ = consumeToken(builder_, DATATABLE);
    if (!result_) result_ = consumeToken(builder_, BUTTON);
    if (!result_) result_ = consumeToken(builder_, INPUT);
    if (!result_) result_ = consumeToken(builder_, TEXTAREA);
    if (!result_) result_ = consumeToken(builder_, SELECT);
    if (!result_) result_ = consumeToken(builder_, CHECKBOX);
    if (!result_) result_ = consumeToken(builder_, RADIO);
    if (!result_) result_ = consumeToken(builder_, RADIOGROUP);
    if (!result_) result_ = consumeToken(builder_, SWITCH);
    if (!result_) result_ = consumeToken(builder_, NUMBERINPUT);
    if (!result_) result_ = consumeToken(builder_, SMARTTEXTFIELD);
    if (!result_) result_ = consumeToken(builder_, SLIDER);
    if (!result_) result_ = consumeToken(builder_, DATEPICKER);
    if (!result_) result_ = consumeToken(builder_, DATERANGEPICKER);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // CONTENT COLON NEWLINE componentInstances
  public static boolean contentBlock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "contentBlock")) return false;
    if (!nextTokenIs(builder_, CONTENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, CONTENT, COLON, NEWLINE);
    result_ = result_ && componentInstances(builder_, level_ + 1);
    exit_section_(builder_, marker_, CONTENT_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // STRING | NUMBER | BOOLEAN | IDENTIFIER (DOT IDENTIFIER)* | actionCall | binaryExpr
  public static boolean expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXPR, "<expr>");
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, BOOLEAN);
    if (!result_) result_ = expr_3(builder_, level_ + 1);
    if (!result_) result_ = actionCall(builder_, level_ + 1);
    if (!result_) result_ = binaryExpr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // IDENTIFIER (DOT IDENTIFIER)*
  private static boolean expr_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expr_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENTIFIER);
    result_ = result_ && expr_3_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (DOT IDENTIFIER)*
  private static boolean expr_3_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expr_3_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!expr_3_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expr_3_1", pos_)) break;
    }
    return true;
  }

  // DOT IDENTIFIER
  private static boolean expr_3_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expr_3_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, DOT, IDENTIFIER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // FOR IDENTIFIER IN expr COLON NEWLINE componentInstances
  public static boolean forBlock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "forBlock")) return false;
    if (!nextTokenIs(builder_, FOR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, FOR, IDENTIFIER, IN);
    result_ = result_ && expr(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COLON, NEWLINE);
    result_ = result_ && componentInstances(builder_, level_ + 1);
    exit_section_(builder_, marker_, FOR_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // IF expr COLON NEWLINE componentInstances
  public static boolean ifBlock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ifBlock")) return false;
    if (!nextTokenIs(builder_, IF)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IF);
    result_ = result_ && expr(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, COLON, NEWLINE);
    result_ = result_ && componentInstances(builder_, level_ + 1);
    exit_section_(builder_, marker_, IF_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER COLON typeRef
  public static boolean param(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "param")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, COLON);
    result_ = result_ && typeRef(builder_, level_ + 1);
    exit_section_(builder_, marker_, PARAM, result_);
    return result_;
  }

  /* ********************************************************** */
  // LPAREN param (COMMA param)* RPAREN
  public static boolean paramList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "paramList")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && param(builder_, level_ + 1);
    result_ = result_ && paramList_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, PARAM_LIST, result_);
    return result_;
  }

  // (COMMA param)*
  private static boolean paramList_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "paramList_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!paramList_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "paramList_2", pos_)) break;
    }
    return true;
  }

  // COMMA param
  private static boolean paramList_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "paramList_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && param(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER EQUALS expr
  //        | IDENTIFIER BIND_READ expr
  //        | IDENTIFIER BIND_WRITE expr
  public static boolean prop(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prop")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = prop_0(builder_, level_ + 1);
    if (!result_) result_ = prop_1(builder_, level_ + 1);
    if (!result_) result_ = prop_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, PROP, result_);
    return result_;
  }

  // IDENTIFIER EQUALS expr
  private static boolean prop_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prop_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, EQUALS);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER BIND_READ expr
  private static boolean prop_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prop_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, BIND_READ);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // IDENTIFIER BIND_WRITE expr
  private static boolean prop_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prop_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, BIND_WRITE);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LPAREN prop (COMMA prop)* RPAREN
  public static boolean propList(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "propList")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && prop(builder_, level_ + 1);
    result_ = result_ && propList_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, PROP_LIST, result_);
    return result_;
  }

  // (COMMA prop)*
  private static boolean propList_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "propList_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!propList_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "propList_2", pos_)) break;
    }
    return true;
  }

  // COMMA prop
  private static boolean propList_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "propList_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && prop(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER COLON expr NEWLINE
  public static boolean propertyAssignment(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "propertyAssignment")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, COLON);
    result_ = result_ && expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, NEWLINE);
    exit_section_(builder_, marker_, PROPERTY_ASSIGNMENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // REQUEST IDENTIFIER COLON NEWLINE requestEntries
  public static boolean requestBlock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "requestBlock")) return false;
    if (!nextTokenIs(builder_, REQUEST)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, REQUEST, IDENTIFIER, COLON, NEWLINE);
    result_ = result_ && requestEntries(builder_, level_ + 1);
    exit_section_(builder_, marker_, REQUEST_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // (requestEntry | NEWLINE | COMMENT)*
  public static boolean requestEntries(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "requestEntries")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REQUEST_ENTRIES, "<request entries>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!requestEntries_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "requestEntries", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  // requestEntry | NEWLINE | COMMENT
  private static boolean requestEntries_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "requestEntries_0")) return false;
    boolean result_;
    result_ = requestEntry(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEWLINE);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // (URL | METHOD | BODY | HEADERS | ON_SUCCESS | ON_ERROR) COLON expr NEWLINE
  public static boolean requestEntry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "requestEntry")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REQUEST_ENTRY, "<request entry>");
    result_ = requestEntry_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, NEWLINE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // URL | METHOD | BODY | HEADERS | ON_SUCCESS | ON_ERROR
  private static boolean requestEntry_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "requestEntry_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, URL);
    if (!result_) result_ = consumeToken(builder_, METHOD);
    if (!result_) result_ = consumeToken(builder_, BODY);
    if (!result_) result_ = consumeToken(builder_, HEADERS);
    if (!result_) result_ = consumeToken(builder_, ON_SUCCESS);
    if (!result_) result_ = consumeToken(builder_, ON_ERROR);
    return result_;
  }

  /* ********************************************************** */
  // STATE COLON NEWLINE stateEntries
  public static boolean stateBlock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stateBlock")) return false;
    if (!nextTokenIs(builder_, STATE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, STATE, COLON, NEWLINE);
    result_ = result_ && stateEntries(builder_, level_ + 1);
    exit_section_(builder_, marker_, STATE_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // (stateEntry | NEWLINE | COMMENT)*
  public static boolean stateEntries(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stateEntries")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STATE_ENTRIES, "<state entries>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!stateEntries_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "stateEntries", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  // stateEntry | NEWLINE | COMMENT
  private static boolean stateEntries_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stateEntries_0")) return false;
    boolean result_;
    result_ = stateEntry(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NEWLINE);
    if (!result_) result_ = consumeToken(builder_, COMMENT);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER COLON typeRef (EQUALS expr)? NEWLINE
  public static boolean stateEntry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stateEntry")) return false;
    if (!nextTokenIs(builder_, IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENTIFIER, COLON);
    result_ = result_ && typeRef(builder_, level_ + 1);
    result_ = result_ && stateEntry_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, NEWLINE);
    exit_section_(builder_, marker_, STATE_ENTRY, result_);
    return result_;
  }

  // (EQUALS expr)?
  private static boolean stateEntry_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stateEntry_3")) return false;
    stateEntry_3_0(builder_, level_ + 1);
    return true;
  }

  // EQUALS expr
  private static boolean stateEntry_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stateEntry_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EQUALS);
    result_ = result_ && expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // INT | FLOAT | STRING_TYPE | BOOL | IDENTIFIER
  public static boolean typeRef(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "typeRef")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TYPE_REF, "<type ref>");
    result_ = consumeToken(builder_, INT);
    if (!result_) result_ = consumeToken(builder_, FLOAT);
    if (!result_) result_ = consumeToken(builder_, STRING_TYPE);
    if (!result_) result_ = consumeToken(builder_, BOOL);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
