package dev.valence.mixin;

import dev.valence.config.ValenceConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_9919;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(class_9919.class)
public abstract class FramerateLimitTrackerMixin {
   @Inject(method = "method_61937", at = @At("RETURN"), cancellable = true, require = 0)
   private void valence$backgroundLimit(CallbackInfoReturnable<Integer> cir) {
      if (ValenceConfig.get().perfBackgroundFps && !class_310.method_1551().method_1569()) {
         cir.setReturnValue(Math.min((Integer)cir.getReturnValue(), 30));
      }
   }
}
