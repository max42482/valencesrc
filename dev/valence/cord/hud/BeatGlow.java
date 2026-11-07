package dev.valence.cord.hud;

import dev.valence.cord.NeonLyrics;
import dev.valence.cord.config.Config;
import dev.valence.cord.lyrics.NowPlaying;
import dev.valence.cord.lyrics.SpotifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_9779;

@Environment(EnvType.CLIENT)
public final class BeatGlow {
   private static float level = 0.0F;
   private static long last = System.nanoTime();

   private BeatGlow() {
   }

   public static void render(class_332 g, class_9779 delta) {
      Config c = NeonLyrics.cfg();
      class_310 mc = class_310.method_1551();
      long now = System.nanoTime();
      float dt = Math.min(0.1F, (float)(now - last) / 1.0E9F);
      last = now;
      NowPlaying np = SpotifyClient.now();
      float beat = c.beatGlow && np.playing && !mc.field_1690.field_1842 ? np.beatPulse(np.progressNow(c.lyricsOffsetMs)) : 0.0F;
      level = beat > level ? beat : level + (beat - level) * (1.0F - (float)Math.exp(-5.0F * dt));
      if (!(level < 0.02F)) {
         float old = Gfx.alpha;
         Gfx.alpha = 1.0F;
         int w = g.method_51421();
         int h = g.method_51443();
         int acc = Theme.accent();
         int size = Math.max(24, Math.min(w, h) / 7);
         float a = 0.28F * level;
         Gfx.vGradient(g, 0, 0, w, size, acc, acc, a, 0.0F);
         Gfx.vGradient(g, 0, h - size, w, size, acc, acc, 0.0F, a);
         int steps = 12;

         for (int i = 0; i < steps; i++) {
            float k = 1.0F - (float)i / steps;
            int x0 = Math.round((float)(size * i) / steps);
            int x1 = Math.round((float)(size * (i + 1)) / steps);
            Gfx.rect(g, x0, 0, x1 - x0, h, acc, a * k * k / 2.2F);
            Gfx.rect(g, w - x1, 0, x1 - x0, h, acc, a * k * k / 2.2F);
         }

         Gfx.alpha = old;
      }
   }
}
