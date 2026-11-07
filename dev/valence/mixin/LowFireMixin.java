package dev.valence.mixin;

import dev.valence.config.ValenceConfig;
import dev.valence.license.License;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1058;
import net.minecraft.class_12249;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4603;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(class_4603.class)
public abstract class LowFireMixin {
   @Inject(method = "method_23070", at = @At("HEAD"), cancellable = true)
   private static void valence$lowFire(class_4587 pose, class_4597 buffers, class_1058 sprite, CallbackInfo ci) {
      float h = Math.max(0.1F, Math.min(1.0F, ValenceConfig.get().fireHeight));
      if (!(h >= 0.999F) && License.active()) {
         ci.cancel();
         class_4588 vc = buffers.method_73477(class_12249.method_75943(sprite.method_45852()));
         float u0 = sprite.method_4594();
         float u1 = sprite.method_4577();
         float v0 = sprite.method_4593();
         float v1 = sprite.method_4575();
         float drop = (1.0F - h) * 0.55F;
         float alpha = 0.9F;

         for (int i = 0; i < 2; i++) {
            pose.method_22903();
            pose.method_46416(-(i * 2 - 1) * 0.24F, -0.3F - drop, 0.0F);
            pose.method_22907(class_7833.field_40716.rotationDegrees((i * 2 - 1) * 10.0F));
            Matrix4f m = pose.method_23760().method_23761();
            vc.method_22918(m, -0.5F, -0.5F, -0.5F).method_22913(u1, v1).method_22915(1.0F, 1.0F, 1.0F, alpha);
            vc.method_22918(m, 0.5F, -0.5F, -0.5F).method_22913(u0, v1).method_22915(1.0F, 1.0F, 1.0F, alpha);
            vc.method_22918(m, 0.5F, 0.5F, -0.5F).method_22913(u0, v0).method_22915(1.0F, 1.0F, 1.0F, alpha);
            vc.method_22918(m, -0.5F, 0.5F, -0.5F).method_22913(u1, v0).method_22915(1.0F, 1.0F, 1.0F, alpha);
            pose.method_22909();
         }
      }
   }
}
