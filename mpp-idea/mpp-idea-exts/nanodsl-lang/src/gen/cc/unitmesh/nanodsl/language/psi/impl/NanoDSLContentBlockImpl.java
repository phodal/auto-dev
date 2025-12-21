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

public class NanoDSLContentBlockImpl extends ASTWrapperPsiElement implements NanoDSLContentBlock {

  public NanoDSLContentBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NanoDSLVisitor visitor) {
    visitor.visitContentBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NanoDSLVisitor) accept((NanoDSLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public NanoDSLComponentInstances getComponentInstances() {
    return findNotNullChildByClass(NanoDSLComponentInstances.class);
  }

  @Override
  @NotNull
  public PsiElement getNewline() {
    return findNotNullChildByType(NEWLINE);
  }

}
