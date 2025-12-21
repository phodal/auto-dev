// This is a generated file. Not intended for manual editing.
package cc.unitmesh.nanodsl.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NanoDSLStateEntry extends PsiElement {

  @Nullable
  NanoDSLExpr getExpr();

  @NotNull
  NanoDSLTypeRef getTypeRef();

  @NotNull
  PsiElement getIdentifier();

  @NotNull
  PsiElement getNewline();

}
