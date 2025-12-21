// This is a generated file. Not intended for manual editing.
package cc.unitmesh.nanodsl.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static cc.unitmesh.nanodsl.language.psi.NanoDSLTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import cc.unitmesh.nanodsl.language.psi.*;

public class NanoDSLComponentInstanceImpl extends ASTWrapperPsiElement implements NanoDSLComponentInstance {

  public NanoDSLComponentInstanceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NanoDSLVisitor visitor) {
    visitor.visitComponentInstance(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NanoDSLVisitor) accept((NanoDSLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public NanoDSLArgList getArgList() {
    return findChildByClass(NanoDSLArgList.class);
  }

  @Override
  @Nullable
  public NanoDSLComponentBody getComponentBody() {
    return findChildByClass(NanoDSLComponentBody.class);
  }

  @Override
  @NotNull
  public NanoDSLComponentName getComponentName() {
    return findNotNullChildByClass(NanoDSLComponentName.class);
  }

  @Override
  @Nullable
  public NanoDSLPropList getPropList() {
    return findChildByClass(NanoDSLPropList.class);
  }

  @Override
  @Nullable
  public PsiElement getNewline() {
    return findChildByType(NEWLINE);
  }

}
