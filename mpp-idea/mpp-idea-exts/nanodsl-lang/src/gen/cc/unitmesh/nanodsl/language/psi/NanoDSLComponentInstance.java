// This is a generated file. Not intended for manual editing.
package cc.unitmesh.nanodsl.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NanoDSLComponentInstance extends PsiElement {

  @Nullable
  NanoDSLArgList getArgList();

  @Nullable
  NanoDSLComponentBody getComponentBody();

  @NotNull
  NanoDSLComponentName getComponentName();

  @Nullable
  NanoDSLPropList getPropList();

  @Nullable
  PsiElement getNewline();

}
