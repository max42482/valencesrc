package dev.valence.cord.hud;

import dev.valence.cord.NeonLyrics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public final class Shapes {
   public static final int MAX_R = 512;
   private static final Map<Integer, class_2960> CIRCLES = new HashMap<>();
   private static boolean failed = false;

   private Shapes() {
   }

   public static boolean usable() {
      return !failed;
   }

   public static void fail() {
      failed = true;
   }

   public static class_2960 circle(int r) {
      if (!failed && r >= 1) {
         if (r > 96) {
            r = Math.min(512, (r + 4) / 8 * 8);
         }

         class_2960 id = CIRCLES.get(r);
         if (id != null) {
            return id;
         }

         try {
            int n = r * 2;
            BufferedImage img = new BufferedImage(n, n, 2);

            for (int y = 0; y < n; y++) {
               for (int x = 0; x < n; x++) {
                  int hit = 0;

                  for (int sy = 0; sy < 4; sy++) {
                     for (int sx = 0; sx < 4; sx++) {
                        float dx = x + (sx + 0.5F) / 4.0F - r;
                        float dy = y + (sy + 0.5F) / 4.0F - r;
                        if (dx * dx + dy * dy <= (float)r * r) {
                           hit++;
                        }
                     }
                  }

                  int a = Math.round(hit / 16.0F * 255.0F);
                  img.setRGB(x, y, a << 24 | 16777215);
               }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "png", out);
            class_1011 ni = class_1011.method_49277(out.toByteArray());
            id = class_2960.method_60655("valence_cord", "shape/c" + r);
            class_1043 tex = new class_1043(() -> "neonlyrics_circle", ni);
            class_310.method_1551().method_1531().method_4616(id, tex);
            CIRCLES.put(r, id);
            return id;
         } catch (Throwable var11) {
            NeonLyrics.LOG.warn("Formen als Textur gehen nicht, nutze einfache Variante", var11);
            failed = true;
            return null;
         }
      } else {
         return null;
      }
   }
}
