package dev.valence.mixin;

import dev.valence.config.ValenceConfig;
import dev.valence.license.License;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(class_757.class)
public abstract class TotemPopMixin {
   @Inject(method = "method_3189", at = @At("HEAD"), cancellable = true, require = 0)
   private void valence$noTotemOverlay(class_1799 stack, CallbackInfo ci) {
      try {
         if (stack != null && stack.method_31574(class_1802.field_8288) && ValenceConfig.get().totemPop == 0 && License.active()) {
            ci.cancel();
         }
      } catch (Throwable var4) {
      }
   }
}
