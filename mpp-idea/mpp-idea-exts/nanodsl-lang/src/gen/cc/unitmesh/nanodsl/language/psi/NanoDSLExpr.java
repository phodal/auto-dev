// This is a generated file. Not intended for manual editing.
package cc.unitmesh.nanodsl.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NanoDSLExpr extends PsiElement {

  @Nullable
  NanoDSLActionCall getActionCall();

  @Nullable
  NanoDSLBinaryExpr getBinaryExpr();

  @Nullable
  PsiElement getBoolean();

  @Nullable
  PsiElement getNumber();

  @Nullable
  PsiElement getString();

}
