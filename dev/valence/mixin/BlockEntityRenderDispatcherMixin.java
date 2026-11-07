package dev.valence.mixin;

import dev.valence.perf.Occlusion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11954;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_310;
import net.minecraft.class_824;
import net.minecraft.class_827;
import net.minecraft.class_11683.class_11792;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(class_824.class)
public abstract class BlockEntityRenderDispatcherMixin {
   @Inject(method = "method_74348", at = @At("HEAD"), cancellable = true, require = 0)
   private void valence$occlusion(class_2586 blockEntity, float partialTick, class_11792 crumbling, CallbackInfoReturnable<class_11954> cir) {
      class_310 mc = class_310.method_1551();
      if (crumbling == null && mc.field_1773 != null) {
         class_243 cam = mc.field_1773.method_19418().method_71156();
         class_827<class_2586, class_11954> renderer = ((class_824)this).method_3550(blockEntity);
         if (renderer != null && !renderer.method_3563()) {
            if (Occlusion.hideBlock(blockEntity.method_11016(), cam.field_1352, cam.field_1351, cam.field_1350)) {
               cir.setReturnValue(null);
            }
         }
      }
   }
}
