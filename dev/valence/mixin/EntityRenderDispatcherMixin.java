package dev.valence.mixin;

import dev.valence.perf.Occlusion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_4604;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(class_898.class)
public abstract class EntityRenderDispatcherMixin {
   @Inject(method = "method_3950", at = @At("HEAD"), cancellable = true, require = 0)
   private void valence$occlusion(class_1297 entity, class_4604 frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
      if (Occlusion.hideEntity(entity, camX, camY, camZ)) {
         cir.setReturnValue(false);
      }
   }
}
