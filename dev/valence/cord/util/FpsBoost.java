package dev.valence.cord.util;

import dev.valence.cord.NeonLyrics;
import dev.valence.cord.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_7172;

@Environment(EnvType.CLIENT)
public final class FpsBoost {
   private FpsBoost() {
   }

   public static void apply(boolean on) {
      class_310 mc = class_310.method_1551();
      Config c = NeonLyrics.cfg();

      try {
         if (on) {
            c.boostBefore = save(mc.field_1690.method_42435()) + ";" + save(mc.field_1690.method_42475()) + ";" + save(mc.field_1690.method_41805());
            set(mc.field_1690.method_42435(), "false");
            set(mc.field_1690.method_42475(), "MINIMAL");
            set(mc.field_1690.method_41805(), "0");
            c.quality = 0;
         } else if (c.boostBefore != null && !c.boostBefore.isEmpty()) {
            String[] v = c.boostBefore.split(";", -1);
            if (v.length >= 3) {
               set(mc.field_1690.method_42435(), v[0]);
               set(mc.field_1690.method_42475(), v[1]);
               set(mc.field_1690.method_41805(), v[2]);
            }

            c.boostBefore = "";
            if (c.quality == 0) {
               c.quality = 1;
            }
         }

         mc.field_1690.method_1640();
      } catch (Exception var4) {
         NeonLyrics.LOG.warn("FPS-Boost konnte nicht alles umstellen", var4);
      }

      c.fpsBoost = on;
      NeonLyrics.saveConfig();
   }

   private static String save(class_7172<?> o) {
      Object v = o.method_41753();
      return v instanceof Enum<?> e ? e.name() : String.valueOf(v);
   }

   private static void set(class_7172 o, String value) {
      Object cur = o.method_41753();
      Object nv;
      if (cur instanceof Boolean) {
         nv = Boolean.parseBoolean(value);
      } else if (cur instanceof Integer) {
         nv = Integer.parseInt(value);
      } else {
         if (!(cur instanceof Enum<?> e)) {
            return;
         }

         nv = Enum.valueOf(e.getDeclaringClass(), value);
      }

      o.method_41748(nv);
   }
}
