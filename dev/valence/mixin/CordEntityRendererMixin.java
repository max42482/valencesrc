package dev.valence.mixin;

import dev.valence.cord.cosmetic.DeathFx;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_4604;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(class_897.class)
public abstract class CordEntityRendererMixin {
   @Inject(method = "method_3933(Lnet/minecraft/class_1297;Lnet/minecraft/class_4604;DDD)Z", at = @At("HEAD"), cancellable = true, require = 0)
   private void valence$hideForDeathFx(class_1297 entity, class_4604 frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
      if (DeathFx.hides(entity)) {
         cir.setReturnValue(false);
      }
   }
}
