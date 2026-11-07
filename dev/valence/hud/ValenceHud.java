package dev.valence.hud;

import dev.valence.config.ValenceConfig;
import dev.valence.gui.Theme;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_9779;

@Environment(EnvType.CLIENT)
public final class ValenceHud {
   private ValenceHud() {
   }

   public static void render(class_332 g, class_9779 delta) {
      class_310 mc = class_310.method_1551();
      if (!mc.field_1690.field_1842) {
         ValenceConfig cfg = ValenceConfig.get();
         if (cfg.hudLogo) {
            int size = Math.round(18.0F * cfg.hudLogoScale);
            Theme.image(g, Theme.LOGO, 4, 4, size, size, 128, 128, -285212673);
         }
      }
   }

   public static String formatTime(long ms) {
      long s = Math.max(0L, ms / 1000L);
      return String.format("%d:%02d", s / 60L, s % 60L);
   }
}
