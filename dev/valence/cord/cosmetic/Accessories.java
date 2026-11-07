package dev.valence.cord.cosmetic;

import dev.valence.cord.NeonLyrics;
import dev.valence.cord.config.Config;
import dev.valence.cord.lyrics.NowPlaying;
import dev.valence.cord.lyrics.SpotifyClient;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_12249;
import net.minecraft.class_1944;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_746;
import net.minecraft.class_765;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public final class Accessories {
   public static final String[] HATS = new String[]{
      "none",
      "headphones",
      "cone",
      "crown",
      "halo",
      "pumpkin",
      "flame_crown",
      "saturn",
      "storm_cloud",
      "unicorn",
      "disco_ball",
      "rubber_duck",
      "octopus",
      "cat_ears",
      "devil_horns",
      "top_hat",
      "cap",
      "party_hat",
      "viking",
      "bunny_ears",
      "note_ring",
      "flower_crown",
      "beanie",
      "visor",
      "witch_hat",
      "antennae",
      "mushroom"
   };
   public static final class_2960 WHITE = class_2960.method_60655("valence_cord", "cosmetic/white");
   private static boolean whiteReady;
   private static boolean whiteFailed;
   private static final float[][] MESH = new float[HATS.length][];
   private static final boolean[] TRIED = new boolean[HATS.length];
   private static final int STRIDE = 14;

   private Accessories() {
   }

   private static float[] mesh(int i) {
      if (TRIED[i]) {
         return MESH[i];
      }

      TRIED[i] = true;

      try (InputStream in = Accessories.class.getResourceAsStream("/assets/valence_cord/models/" + HATS[i] + ".bin")) {
         if (in != null) {
            ByteBuffer b = ByteBuffer.wrap(in.readAllBytes()).order(ByteOrder.LITTLE_ENDIAN);
            if (b.getInt() != 827149390) {
               return null;
            }

            int n = b.getInt();
            float[] m = new float[n * 14];

            for (int t = 0; t < n; t++) {
               for (int k = 0; k < 12; k++) {
                  m[t * 14 + k] = b.getFloat();
               }

               m[t * 14 + 12] = Float.intBitsToFloat(b.getInt());
               m[t * 14 + 13] = b.getInt();
            }

            MESH[i] = m;
            return MESH[i];
         } else {
            Object var10 = null;
            return (float[])var10;
         }
      } catch (Throwable var9) {
         NeonLyrics.LOG.warn("Hut-Modell fehlt: " + HATS[i], var9);
         return MESH[i];
      }
   }

   public static boolean whiteTex() {
      return white();
   }

   private static boolean white() {
      if (whiteReady) {
         return true;
      }

      if (whiteFailed) {
         return false;
      }

      try {
         BufferedImage img = new BufferedImage(4, 4, 2);

         for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
               img.setRGB(x, y, -1);
            }
         }

         ByteArrayOutputStream out = new ByteArrayOutputStream();
         ImageIO.write(img, "png", out);
         class_310.method_1551().method_1531().method_4616(WHITE, new class_1043(() -> "neonlyrics_white", class_1011.method_49277(out.toByteArray())));
         whiteReady = true;
      } catch (Throwable var3) {
         whiteFailed = true;
      }

      return whiteReady;
   }

   public static void render(class_310 mc, class_4597 buffers, Matrix4f base, class_243 cam, float pt) {
      Config c = NeonLyrics.cfg();
      int idx = c.hat;
      if (idx > 0 && idx < HATS.length) {
         class_746 pl = mc.field_1724;
         if (pl != null && !pl.method_5767() && !pl.method_6113() && !pl.method_5681() && !pl.method_6128() && !mc.field_1690.method_31044().method_31034()) {
            float[] m = mesh(idx);
            if (m != null && white()) {
               class_243 pos = pl.method_30950(pt);
               double neckY = pos.field_1351 + pl.method_5751() - 0.22;
               float yaw = (float)Math.toRadians(class_3532.method_17821(pt, pl.field_6259, pl.field_6241));
               float pitch = (float)Math.toRadians(pl.method_5695(pt));
               float cy = (float)Math.cos(-yaw);
               float sy = (float)Math.sin(-yaw);
               float cp = (float)Math.cos(pitch);
               float sp = (float)Math.sin(pitch);
               float s = 0.060937498F;
               double ox = pos.field_1352 - cam.field_1352;
               double oy = neckY - cam.field_1351;
               double oz = pos.field_1350 - cam.field_1350;

               int light;
               try {
                  class_2338 bp = class_2338.method_49637(pos.field_1352, neckY + 0.3, pos.field_1350);
                  light = class_765.method_23687(mc.field_1687.method_8314(class_1944.field_9282, bp), mc.field_1687.method_8314(class_1944.field_9284, bp));
               } catch (Throwable var49) {
                  light = 15728880;
               }

               NowPlaying np = SpotifyClient.now();
               float beat = np.playing ? np.beatPulse(np.progressNow(c.lyricsOffsetMs)) : 0.0F;
               float glow = c.hatGlow ? 0.75F + 0.25F * beat : 0.55F;
               class_4588 vc = buffers.method_73477(class_12249.method_75994(WHITE));
               int n = m.length / 14;
               float[] v = new float[9];

               for (int t = 0; t < n; t++) {
                  int o = t * 14;
                  float nx = m[o + 9];
                  float ny = m[o + 10];
                  float nz = m[o + 11];
                  float ny1 = ny * cp - nz * sp;
                  float nz1 = ny * sp + nz * cp;
                  float nx2 = nx * cy + nz1 * sy;
                  float nz2 = -nx * sy + nz1 * cy;

                  for (int k = 0; k < 3; k++) {
                     float x = m[o + k * 3] * s;
                     float y = (m[o + k * 3 + 1] + 4.0F) * s;
                     float z = m[o + k * 3 + 2] * s;
                     float y1 = y * cp - z * sp;
                     float z1 = y * sp + z * cp;
                     v[k * 3] = x * cy + z1 * sy;
                     v[k * 3 + 1] = y1;
                     v[k * 3 + 2] = -x * sy + z1 * cy;
                  }

                  int rgb = Float.floatToRawIntBits(m[o + 12]);
                  boolean em = m[o + 13] > 0.5F;
                  int li = light;
                  int col;
                  if (em) {
                     int r = Math.min(255, Math.round((rgb >> 16 & 0xFF) * (0.6F + 0.6F * glow)));
                     int g = Math.min(255, Math.round((rgb >> 8 & 0xFF) * (0.6F + 0.6F * glow)));
                     int bl = Math.min(255, Math.round((rgb & 0xFF) * (0.6F + 0.6F * glow)));
                     col = 0xFF000000 | r << 16 | g << 8 | bl;
                     li = 15728880;
                  } else {
                     col = 0xFF000000 | rgb;
                  }

                  for (int k = 0; k < 4; k++) {
                     int kk = Math.min(k, 2);
                     vc.method_22918(base, (float)(ox + v[kk * 3]), (float)(oy + v[kk * 3 + 1]), (float)(oz + v[kk * 3 + 2]))
                        .method_39415(col)
                        .method_22913(0.5F, 0.5F)
                        .method_22922(class_4608.field_21444)
                        .method_60803(li)
                        .method_22914(nx2, ny1, nz2);
                  }
               }
            }
         }
      }
   }
}
