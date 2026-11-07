package dev.valence.mixin;

import dev.valence.config.ValenceConfig;
import dev.valence.sky.ValenceSky;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import net.minecraft.class_4063;
import net.minecraft.class_761;
import net.minecraft.class_9909;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(class_761.class)
public abstract class LevelRendererMixin {
   @Inject(method = "method_62204", at = @At("HEAD"), cancellable = true)
   private void valence$hideClouds(
      class_9909 frameGraph, class_4063 status, class_243 cameraPos, long gameTime, float partialTick, int cloudColor, float cloudHeight, CallbackInfo ci
   ) {
      if (ValenceConfig.get().skyHideClouds && ValenceSky.visible()) {
         ci.cancel();
      }
   }
}
