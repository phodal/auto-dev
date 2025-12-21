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

public class NanoDSLComponentBodyImpl extends ASTWrapperPsiElement implements NanoDSLComponentBody {

  public NanoDSLComponentBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull NanoDSLVisitor visitor) {
    visitor.visitComponentBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof NanoDSLVisitor) accept((NanoDSLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<NanoDSLComponentInstance> getComponentInstanceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NanoDSLComponentInstance.class);
  }

  @Override
  @NotNull
  public List<NanoDSLContentBlock> getContentBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NanoDSLContentBlock.class);
  }

  @Override
  @NotNull
  public List<NanoDSLForBlock> getForBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NanoDSLForBlock.class);
  }

  @Override
  @NotNull
  public List<NanoDSLIfBlock> getIfBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NanoDSLIfBlock.class);
  }

  @Override
  @NotNull
  public List<NanoDSLPropertyAssignment> getPropertyAssignmentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NanoDSLPropertyAssignment.class);
  }

  @Override
  @NotNull
  public List<NanoDSLRequestBlock> getRequestBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NanoDSLRequestBlock.class);
  }

  @Override
  @NotNull
  public List<NanoDSLStateBlock> getStateBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, NanoDSLStateBlock.class);
  }

}
