package dev.valence.mixin;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3262;
import net.minecraft.class_3304;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(class_3304.class)
public abstract class ReloadableResourceManagerMixin {
   @ModifyVariable(method = "method_18232", at = @At("HEAD"), argsOnly = true, require = 0)
   private List<class_3262> valence$stylesOnTop(List<class_3262> packs) {
      class_3262 ours = null;

      for (class_3262 p : packs) {
         if ("file/Valence Styles".equals(p.method_14409())) {
            ours = p;
         }
      }

      if (ours != null && packs.get(packs.size() - 1) != ours) {
         List<class_3262> out = new ArrayList<>(packs);
         out.remove(ours);
         out.add(ours);
         return out;
      } else {
         return packs;
      }
   }
}
