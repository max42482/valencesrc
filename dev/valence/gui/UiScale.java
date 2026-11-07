package dev.valence.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public final class UiScale {
   private UiScale() {
   }

   public static int pixelScale() {
      class_1041 w = class_310.method_1551().method_22683();
      float fit = Math.min(w.method_4489() / 700.0F, w.method_4506() / 400.0F);
      return Math.max(1, (int)Math.floor(fit + 0.25F));
   }

   public static float factor() {
      double gui = class_310.method_1551().method_22683().method_4495();
      return (float)(pixelScale() / Math.max(1.0, gui));
   }
}
