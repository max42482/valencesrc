package dev.valence.mixin;

import dev.valence.config.ValenceConfig;
import dev.valence.sky.ValenceSky;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4184;
import net.minecraft.class_5636;
import net.minecraft.class_638;
import net.minecraft.class_758;
import net.minecraft.class_2874.class_12326;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(class_758.class)
public abstract class FogRendererMixin {
   @Inject(method = "method_62185", at = @At("RETURN"), cancellable = true)
   private void valence$tintFog(
      class_4184 camera, float partialTick, class_638 level, int renderDistance, float darkenWorld, CallbackInfoReturnable<Vector4f> cir
   ) {
      if (camera.method_19334() == class_5636.field_27888) {
         class_12326 skybox = level.method_8597().comp_5220();
         boolean end = skybox == class_12326.field_64387;
         if (skybox != class_12326.field_64385 && (!end || ValenceConfig.get().skyInEnd)) {
            Vector4f v = (Vector4f)cir.getReturnValue();
            int vanilla = Math.round(clamp(v.w) * 255.0F) << 24
               | Math.round(clamp(v.x) * 255.0F) << 16
               | Math.round(clamp(v.y) * 255.0F) << 8
               | Math.round(clamp(v.z) * 255.0F);
            int c = ValenceSky.fogColor(vanilla);
            if (c != vanilla) {
               cir.setReturnValue(new Vector4f((c >> 16 & 0xFF) / 255.0F, (c >> 8 & 0xFF) / 255.0F, (c & 0xFF) / 255.0F, v.w));
            }
         }
      }
   }

   private static float clamp(float f) {
      return f < 0.0F ? 0.0F : (f > 1.0F ? 1.0F : f);
   }
}
