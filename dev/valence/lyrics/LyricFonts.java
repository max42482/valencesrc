package dev.valence.lyrics;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11719;
import net.minecraft.class_2960;
import net.minecraft.class_11719.class_11721;

@Environment(EnvType.CLIENT)
public final class LyricFonts {
   public static final List<LyricFonts.LyricFont> ALL = List.of(
      new LyricFonts.LyricFont("poppins_bold", "Poppins", vl("poppins_bold")),
      new LyricFonts.LyricFont("poppins_italic", "Poppins Italic", vl("poppins_italic")),
      new LyricFonts.LyricFont("dmserif", "Serif", vl("dmserif")),
      new LyricFonts.LyricFont("abril", "Abril", vl("abril")),
      new LyricFonts.LyricFont("pacifico", "Pacifico", vl("pacifico")),
      new LyricFonts.LyricFont("kaushan", "Brush", vl("kaushan")),
      new LyricFonts.LyricFont("bangers", "Bangers", vl("bangers")),
      new LyricFonts.LyricFont("luckiest", "Luckiest", vl("luckiest")),
      new LyricFonts.LyricFont("lilita", "Lilita", vl("lilita")),
      new LyricFonts.LyricFont("default", "Minecraft", class_2960.method_60656("default"))
   );

   private LyricFonts() {
   }

   private static class_2960 vl(String name) {
      return class_2960.method_60655("valence", name);
   }

   public static LyricFonts.LyricFont byId(String id) {
      for (LyricFonts.LyricFont f : ALL) {
         if (f.id().equals(id)) {
            return f;
         }
      }

      return ALL.get(0);
   }

   @Environment(EnvType.CLIENT)
   public record LyricFont(String id, String displayName, class_2960 fontId) {
      public class_11719 description() {
         return new class_11721(this.fontId);
      }
   }
}
