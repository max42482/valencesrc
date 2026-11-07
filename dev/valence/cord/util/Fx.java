package dev.valence.cord.util;

import dev.valence.gui.UiSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public final class Fx {
   private Fx() {
   }

   public static String t(String key, Object... args) {
      return class_1074.method_4662(key, args);
   }

   public static void sound(Fx.Sfx s) {
      switch (s) {
         case CLICK:
            UiSounds.click();
            break;
         case WIN:
            UiSounds.toggle(true);
            break;
         case ERROR:
            UiSounds.toggle(false);
            break;
         case TICK:
            UiSounds.hover();
      }
   }

   public static void info(String text) {
      class_310 mc = class_310.method_1551();
      if (mc.field_1705 != null) {
         mc.field_1705
            .method_1743()
            .method_1812(
               class_2561.method_43470("[Valence] ")
                  .method_27692(class_124.field_1075)
                  .method_10852(class_2561.method_43470(text).method_27692(class_124.field_1080))
            );
      }
   }

   public static void error(String text) {
      class_310 mc = class_310.method_1551();
      if (mc.field_1705 != null) {
         mc.field_1705
            .method_1743()
            .method_1812(
               class_2561.method_43470("[Valence] ")
                  .method_27692(class_124.field_1079)
                  .method_10852(class_2561.method_43470(text).method_27692(class_124.field_1061))
            );
         sound(Fx.Sfx.ERROR);
      }
   }

   public static void clipboard(String s) {
      class_310.method_1551().field_1774.method_1455(s);
   }

   public static String myName() {
      return class_310.method_1551().method_1548().method_1676();
   }

   @Environment(EnvType.CLIENT)
   public enum Sfx {
      CLICK,
      WIN,
      ERROR,
      TICK;
   }
}
