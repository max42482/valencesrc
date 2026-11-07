package dev.valence.mixin;

import dev.valence.config.ValenceConfig;
import dev.valence.license.License;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11659;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_4587;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(class_4603.class)
public abstract class TotemSmallMixin {
   @Shadow
   private class_1799 field_59972;
   @Unique
   private boolean valence$scaled;

   @Inject(method = "method_70939", at = @At("HEAD"), require = 0)
   private void valence$smallStart(class_4587 pose, float partialTick, class_11659 collector, CallbackInfo ci) {
      this.valence$scaled = false;
      class_1799 item = this.field_59972;
      if (item != null && item.method_31574(class_1802.field_8288) && ValenceConfig.get().totemPop == 1 && License.active()) {
         pose.method_22903();
         pose.method_22905(0.4F, 0.4F, 1.0F);
         this.valence$scaled = true;
      }
   }

   @Inject(method = "method_70939", at = @At("RETURN"), require = 0)
   private void valence$smallEnd(class_4587 pose, float partialTick, class_11659 collector, CallbackInfo ci) {
      if (this.valence$scaled) {
         pose.method_22909();
         this.valence$scaled = false;
      }
   }
}
