// This is a generated file. Not intended for manual editing.
package cc.unitmesh.nanodsl.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface NanoDSLComponentBody extends PsiElement {

  @NotNull
  List<NanoDSLComponentInstance> getComponentInstanceList();

  @NotNull
  List<NanoDSLContentBlock> getContentBlockList();

  @NotNull
  List<NanoDSLForBlock> getForBlockList();

  @NotNull
  List<NanoDSLIfBlock> getIfBlockList();

  @NotNull
  List<NanoDSLPropertyAssignment> getPropertyAssignmentList();

  @NotNull
  List<NanoDSLRequestBlock> getRequestBlockList();

  @NotNull
  List<NanoDSLStateBlock> getStateBlockList();

}
