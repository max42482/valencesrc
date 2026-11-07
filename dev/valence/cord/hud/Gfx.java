package dev.valence.cord.hud;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.valence.cord.NeonLyrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10799;
import net.minecraft.class_11719;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_11719.class_11721;

@Environment(EnvType.CLIENT)
public final class Gfx {
   public static float alpha = 1.0F;
   private static long revealStart = 0L;
   private static int revealTop = 0;
   private static int curY = Integer.MIN_VALUE;
   public static boolean revealOn = false;
   public static final Gfx.Role UI = Gfx.Role.UI;
   public static final Gfx.Role BOLD = Gfx.Role.BOLD;
   public static final Gfx.Role LYRICS = Gfx.Role.LYRICS;
   public static final String[] FONT_IDS = new String[]{
      "poppins",
      "nunito",
      "montserrat",
      "inter",
      "outfit",
      "spacegrotesk",
      "manrope",
      "dmsans",
      "plusjakartasans",
      "sora",
      "lexend",
      "rubik",
      "urbanist",
      "syne",
      "kanit",
      "fredoka",
      "baloo2",
      "unbounded",
      "orbitron",
      "bebasneue",
      "pacifico",
      "caveat"
   };
   public static final String[] FONT_NAMES = new String[]{
      "Poppins",
      "Nunito",
      "Montserrat",
      "Inter",
      "Outfit",
      "Space Grotesk",
      "Manrope",
      "DM Sans",
      "Jakarta Sans",
      "Sora",
      "Lexend",
      "Rubik",
      "Urbanist",
      "Syne",
      "Kanit",
      "Fredoka",
      "Baloo",
      "Unbounded",
      "Orbitron",
      "Bebas Neue",
      "Pacifico",
      "Caveat",
      "Minecraft"
   };
   private static final class_11719[][] FD = new class_11719[FONT_IDS.length][3];
   private static Gfx.Role cur;
   private static final Map<class_11719, Map<String, Integer>> WIDTHS;

   private Gfx() {
   }

   public static void startReveal(int top, long start) {
      revealTop = top;
      revealStart = start;
   }

   private static float reveal(int y) {
      if (revealOn && revealStart != 0L && NeonLyrics.cfg().fxMenuAnim && y != Integer.MIN_VALUE) {
         long el = System.currentTimeMillis() - revealStart;
         if (el > 900L) {
            revealStart = 0L;
            return 1.0F;
         } else {
            float front = (float)el * 0.55F;
            float d = front - (y - revealTop);
            return Math.max(0.0F, Math.min(1.0F, d / 40.0F));
         }
      } else {
         return 1.0F;
      }
   }

   public static int argb(int rgb, float a) {
      int al = Math.round(Math.max(0.0F, Math.min(1.0F, a * alpha * reveal(curY))) * 255.0F);
      return al << 24 | rgb & 16777215;
   }

   public static int argbRaw(int rgb, float a) {
      int al = Math.round(Math.max(0.0F, Math.min(1.0F, a)) * 255.0F);
      return al << 24 | rgb & 16777215;
   }

   public static int lerpColor(int c1, int c2, float t) {
      t = Math.max(0.0F, Math.min(1.0F, t));
      int r = (int)((c1 >> 16 & 0xFF) + ((c2 >> 16 & 0xFF) - (c1 >> 16 & 0xFF)) * t);
      int g = (int)((c1 >> 8 & 0xFF) + ((c2 >> 8 & 0xFF) - (c1 >> 8 & 0xFF)) * t);
      int b = (int)((c1 & 0xFF) + ((c2 & 0xFF) - (c1 & 0xFF)) * t);
      return r << 16 | g << 8 | b;
   }

   public static int brighten(int c, float t) {
      return lerpColor(c, 16777215, t);
   }

   public static int hsv(float h, float s, float v) {
      h -= (float)Math.floor(h);
      int i = (int)(h * 6.0F);
      float f = h * 6.0F - i;
      float p = v * (1.0F - s);
      float q = v * (1.0F - f * s);
      float t = v * (1.0F - (1.0F - f) * s);
      float r;
      float g;
      float b;
      switch (i % 6) {
         case 0:
            r = v;
            g = t;
            b = p;
            break;
         case 1:
            r = q;
            g = v;
            b = p;
            break;
         case 2:
            r = p;
            g = v;
            b = t;
            break;
         case 3:
            r = p;
            g = q;
            b = v;
            break;
         case 4:
            r = t;
            g = p;
            b = v;
            break;
         default:
            r = v;
            g = p;
            b = q;
      }

      return (int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F);
   }

   public static void rect(class_332 g, int x, int y, int w, int h, int rgb, float a) {
      if (w > 0 && h > 0 && !(a * alpha <= 0.004F)) {
         curY = y;
         g.method_25294(x, y, x + w, y + h, argb(rgb, a));
      }
   }

   private static int guiScale() {
      if (!revealOn && NeonLyrics.cfg().quality == 0) {
         return 1;
      }

      try {
         return Math.max(1, Math.min(2, Math.round(class_310.method_1551().method_22683().method_4495())));
      } catch (Exception var1) {
         return 1;
      }
   }

   public static void round(class_332 g, int x, int y, int w, int h, int r, int rgb, float a) {
      if (w > 0 && h > 0 && !(a * alpha <= 0.004F)) {
         curY = y;
         r = Math.max(0, Math.min(r, Math.min(w, h) / 2));
         int col = argb(rgb, a);
         if (r == 0) {
            g.method_25294(x, y, x + w, y + h, col);
         } else if (r >= 2 && Shapes.usable()) {
            aaRound(g, x, y, w, h, r, rgb, a);
         } else {
            int s = guiScale();
            if (s == 1) {
               roundRows(g, x, y, w, h, r, col);
            } else {
               g.method_25294(x, y + r, x + w, y + h - r, col);
               g.method_51448().pushMatrix();
               g.method_51448().scale(1.0F / s, 1.0F / s);
               int X = x * s;
               int Y = y * s;
               int W = w * s;
               int H = h * s;
               int R = r * s;

               for (int i = 0; i < R; i++) {
                  double dy = R - i - 0.5;
                  int dx = (int)Math.round(R - Math.sqrt(Math.max(0.0, R * R - dy * dy)));
                  g.method_25294(X + dx, Y + i, X + W - dx, Y + i + 1, col);
                  g.method_25294(X + dx, Y + H - i - 1, X + W - dx, Y + H - i, col);
               }

               g.method_51448().popMatrix();
            }
         }
      }
   }

   private static int fullScale() {
      try {
         return Math.max(1, Math.min(6, Math.round(class_310.method_1551().method_22683().method_4495())));
      } catch (Exception var1) {
         return 2;
      }
   }

   public static void aaRound(class_332 g, float x, float y, float w, float h, float r, int rgb, float a) {
      if (!(w <= 0.0F) && !(h <= 0.0F) && !(a * alpha <= 0.004F)) {
         int s = fullScale();
         float X = x * s;
         float Y = y * s;
         float W = w * s;
         float H = h * s;
         float R = Math.max(0.0F, Math.min(r * s, Math.min(W, H) / 2.0F));
         g.method_51448().pushMatrix();
         g.method_51448().scale(1.0F / s, 1.0F / s);
         int x0 = Math.round(X);
         int y0 = Math.round(Y);
         int x1 = Math.round(X + W);
         int y1 = Math.round(Y + H);
         int iw = x1 - x0;
         int ih = y1 - y0;
         int ir = (int)Math.ceil(R);
         int rq = Math.min(Math.round(R), Math.min(iw, ih) / 2);
         if (rq > 96) {
            rq = Math.min(512, (rq + 4) / 8 * 8);
         }

         if (rq * 2 > Math.min(iw, ih)) {
            int n0 = rq * 2;
            if (iw == ih) {
               int c0x = x0 + iw / 2;
               int c0y = y0 + ih / 2;
               x0 = c0x - rq;
               y0 = c0y - rq;
               x1 = x0 + n0;
               y1 = y0 + n0;
               ih = n0;
               iw = n0;
            } else {
               rq = Math.min(iw, ih) / 2;
            }
         }

         if (rq >= 2 && Shapes.usable()) {
            class_2960 id = Shapes.circle(rq);
            if (id != null) {
               try {
                  int col = argb(rgb, a);
                  int n = rq * 2;
                  RenderPipeline pipe = class_10799.field_56883;
                  if (iw == n && ih == n) {
                     g.method_25291(pipe, id, x0, y0, 0.0F, 0.0F, n, n, n, n, col);
                  } else {
                     g.method_25291(pipe, id, x0, y0, 0.0F, 0.0F, rq, rq, n, n, col);
                     g.method_25291(pipe, id, x1 - rq, y0, rq, 0.0F, rq, rq, n, n, col);
                     g.method_25291(pipe, id, x0, y1 - rq, 0.0F, rq, rq, rq, n, n, col);
                     g.method_25291(pipe, id, x1 - rq, y1 - rq, rq, rq, rq, rq, n, n, col);
                     if (iw > n) {
                        g.method_25294(x0 + rq, y0, x1 - rq, y0 + rq, col);
                        g.method_25294(x0 + rq, y1 - rq, x1 - rq, y1, col);
                     }

                     if (ih > n) {
                        g.method_25294(x0, y0 + rq, x1, y1 - rq, col);
                     }
                  }

                  g.method_51448().popMatrix();
                  return;
               } catch (Throwable var32) {
                  Shapes.fail();
               }
            }
         }

         if (ih - 2 * ir > 0) {
            g.method_25294(x0, y0 + ir, x1, y1 - ir, argb(rgb, a));
         }

         float[] e = new float[4];

         for (int i = 0; i < ir; i++) {
            float mn = Float.MAX_VALUE;
            float mx = -1.0F;

            for (int k = 0; k < 4; k++) {
               float dy = R - (i + 0.125F + k * 0.25F);
               float ex = dy <= 0.0F ? 0.0F : R - (float)Math.sqrt(Math.max(0.0F, R * R - dy * dy));
               e[k] = ex;
               mn = Math.min(mn, ex);
               mx = Math.max(mx, ex);
            }

            int full = (int)Math.ceil(mx);
            int yt = y0 + i;
            int yb = y1 - 1 - i;
            if (iw - 2 * full > 0) {
               g.method_25294(x0 + full, yt, x1 - full, yt + 1, argb(rgb, a));
               if (yb != yt) {
                  g.method_25294(x0 + full, yb, x1 - full, yb + 1, argb(rgb, a));
               }
            }

            for (int c = (int)Math.floor(mn); c < full; c++) {
               float cov = 0.0F;

               for (int k = 0; k < 4; k++) {
                  cov += Math.max(0.0F, Math.min(1.0F, c + 1 - e[k]));
               }

               cov /= 4.0F;
               if (!(cov <= 0.01F)) {
                  int col = argb(rgb, a * cov);
                  g.method_25294(x0 + c, yt, x0 + c + 1, yt + 1, col);
                  g.method_25294(x1 - c - 1, yt, x1 - c, yt + 1, col);
                  if (yb != yt) {
                     g.method_25294(x0 + c, yb, x0 + c + 1, yb + 1, col);
                     g.method_25294(x1 - c - 1, yb, x1 - c, yb + 1, col);
                  }
               }
            }
         }

         g.method_51448().popMatrix();
      }
   }

   public static void aaCircle(class_332 g, float cx, float cy, float r, int rgb, float a) {
      aaRound(g, cx - r, cy - r, r * 2.0F, r * 2.0F, r, rgb, a);
   }

   public static void aaStroke(class_332 g, float[] xs, float[] ys, float width, int rgb, float a) {
      float step = Math.max(0.5F, width * 0.45F);

      for (int i = 0; i + 1 < xs.length; i++) {
         float dx = xs[i + 1] - xs[i];
         float dy = ys[i + 1] - ys[i];
         float len = (float)Math.sqrt(dx * dx + dy * dy);
         int n = Math.max(1, (int)(len / step));

         for (int k = 0; k <= n; k++) {
            float t = (float)k / n;
            aaCircle(g, xs[i] + dx * t, ys[i] + dy * t, width / 2.0F, rgb, a);
         }
      }
   }

   private static void roundRows(class_332 g, int x, int y, int w, int h, int r, int col) {
      g.method_25294(x, y + r, x + w, y + h - r, col);

      for (int i = 0; i < r; i++) {
         double dy = r - i - 0.5;
         int dx = (int)Math.round(r - Math.sqrt(Math.max(0.0, r * r - dy * dy)));
         g.method_25294(x + dx, y + i, x + w - dx, y + i + 1, col);
         g.method_25294(x + dx, y + h - i - 1, x + w - dx, y + h - i, col);
      }
   }

   public static void glow(class_332 g, int x, int y, int w, int h, int r, int rgb, float strength, int size) {
      if (size >= 7 && !(strength <= 0.05F) && !(alpha <= 0.01F)) {
         float k = Math.min(1.0F, strength);
         aaRound(g, x - 3, y - 1, w + 6, h + 6, r + 3, 0, 0.14F * k);
      }
   }

   public static void flowBorder(class_332 g, int x, int y, int w, int h, int c1, int c2, float a, float phase) {
      flowBorder(g, x, y, w, h, c1, c2, a, phase, 1.0F);
   }

   public static void circle(class_332 g, float cx, float cy, float r, int rgb, float a) {
      int ri = Math.max(1, Math.round(r));
      round(g, Math.round(cx) - ri, Math.round(cy) - ri, ri * 2, ri * 2, ri, rgb, a);
   }

   public static void flowBorder(class_332 g, int x, int y, int w, int h, int c1, int c2, float a, float phase, float portion) {
      float k = Math.max(0.0F, Math.min(1.0F, a)) * Math.max(0.0F, Math.min(1.0F, portion));
      if (!(k <= 0.01F)) {
         rect(g, x, y, w, 1, 16777215, 0.1F * k);
         rect(g, x, y + h - 1, w, 1, 16777215, 0.04F * k);
      }
   }

   public static void hGradient(class_332 g, int x, int y, int w, int h, int c1, int c2, float a) {
      if (w > 0 && h > 0) {
         int step = 2;

         for (int i = 0; i < w; i += step) {
            g.method_25294(x + i, y, x + Math.min(w, i + step), y + h, argb(lerpColor(c1, c2, (float)i / w), a));
         }
      }
   }

   public static void vGradient(class_332 g, int x, int y, int w, int h, int top, int bottom, float aTop, float aBottom) {
      g.method_25296(x, y, x + w, y + h, argb(top, aTop), argb(bottom, aBottom));
   }

   public static class_327 font() {
      return class_310.method_1551().field_1772;
   }

   private static int uiIndex() {
      return NeonLyrics.cfg().uiFont;
   }

   public static boolean clean() {
      int i = uiIndex();
      return i >= 0 && i < FONT_IDS.length;
   }

   private static class_11719 resolve(Gfx.Role r) {
      if (r == Gfx.Role.LYRICS) {
         int i = Math.floorMod(NeonLyrics.cfg().lyricsFont, FONT_IDS.length);
         return FD[i][2];
      } else {
         return !clean() ? null : FD[uiIndex()][r == Gfx.Role.BOLD ? 1 : 0];
      }
   }

   public static void textIn(class_332 g, String s, int x, int y, int rgb, float a, int fontIdx, boolean bold) {
      if (!(a * alpha < 0.03F)) {
         class_2561 c = fontIdx >= 0 && fontIdx < FONT_IDS.length
            ? class_2561.method_43470(s).method_27696(class_2583.field_24360.method_27704(FD[fontIdx][bold ? 1 : 0]))
            : class_2561.method_43470(s);
         g.method_51439(font(), c, x, y, argb(rgb, a), false);
      }
   }

   public static int wIn(String s, int fontIdx, boolean bold) {
      return fontIdx >= 0 && fontIdx < FONT_IDS.length
         ? font().method_27525(class_2561.method_43470(s).method_27696(class_2583.field_24360.method_27704(FD[fontIdx][bold ? 1 : 0])))
         : font().method_1727(s);
   }

   public static Gfx.Role useFont(Gfx.Role f) {
      Gfx.Role old = cur;
      cur = f;
      return old;
   }

   public static class_2561 comp(String s, Gfx.Role r) {
      class_11719 f = resolve(r);
      return f != null ? class_2561.method_43470(s).method_27696(class_2583.field_24360.method_27704(f)) : class_2561.method_43470(s);
   }

   public static int w(String s) {
      return w(s, cur);
   }

   public static int w(String s, Gfx.Role r) {
      if (s != null && !s.isEmpty()) {
         class_11719 f = resolve(r);
         if (f == null) {
            return font().method_1727(s);
         }

         Map<String, Integer> m = WIDTHS.computeIfAbsent(f, k -> new HashMap<>());
         Integer v = m.get(s);
         if (v == null) {
            if (m.size() > 3000) {
               m.clear();
            }

            v = font().method_27525(comp(s, r));
            m.put(s, v);
         }

         return v;
      } else {
         return 0;
      }
   }

   public static void clearWidths() {
      WIDTHS.clear();
   }

   public static void text(class_332 g, String s, int x, int y, int rgb, float a, boolean shadow) {
      if (!(a * alpha < 0.03F) && s != null) {
         curY = y;
         if (resolve(cur) == null) {
            g.method_51433(font(), s, x, y, argb(rgb, a), shadow);
         } else {
            class_2561 c = comp(s, cur);
            if (shadow && NeonLyrics.cfg().quality > 0) {
               g.method_51448().pushMatrix();
               g.method_51448().translate(0.5F, 0.7F);
               g.method_51439(font(), c, x, y, argb(328458, a * 0.45F), false);
               g.method_51448().popMatrix();
            }

            g.method_51439(font(), c, x, y, argb(rgb, a), false);
         }
      }
   }

   public static void textBold(class_332 g, String s, int x, int y, int rgb, float a, boolean shadow) {
      Gfx.Role o = useFont(BOLD);
      text(g, s, x, y, rgb, a, shadow);
      useFont(o);
   }

   public static void text(class_332 g, String s, int x, int y, int rgb) {
      text(g, s, x, y, rgb, 1.0F, false);
   }

   public static void center(class_332 g, String s, int cx, int y, int rgb, float a, boolean shadow) {
      text(g, s, cx - w(s) / 2, y, rgb, a, shadow);
   }

   public static void right(class_332 g, String s, int rx, int y, int rgb, float a, boolean shadow) {
      text(g, s, rx - w(s), y, rgb, a, shadow);
   }

   public static void glowText(class_332 g, String s, int x, int y, int rgb, float a) {
      Gfx.Role o = useFont(BOLD);
      text(g, s, x, y, brighten(rgb, 0.5F), a, false);
      useFont(o);
   }

   public static void rainbowText(class_332 g, String s, int x, int y, int c1, int c2, float a, float phase) {
      textBold(g, s, x, y, 16777215, a, false);
   }

   public static String ellipsize(String s, int maxW) {
      if (s == null) {
         return "";
      }

      if (w(s) <= maxW) {
         return s;
      }

      while (s.length() > 1 && w(s + "…") > maxW) {
         s = s.substring(0, s.length() - 1);
      }

      return s + "…";
   }

   public static List<String> wrap(String s, int maxW) {
      List<String> out = new ArrayList<>();

      for (String para : s.split("\n")) {
         StringBuilder line = new StringBuilder();

         for (String w : para.split(" ")) {
            String test = line.length() == 0 ? w : line + " " + w;
            if (w(test) > maxW && line.length() > 0) {
               out.add(line.toString());
               line = new StringBuilder(w);
            } else {
               line = new StringBuilder(test);
            }
         }

         out.add(line.toString());
      }

      return out;
   }

   static {
      String[] kinds = new String[]{"_ui", "_bold", "_lyrics"};

      for (int i = 0; i < FONT_IDS.length; i++) {
         for (int k = 0; k < 3; k++) {
            FD[i][k] = new class_11721(class_2960.method_60655("valence_cord", FONT_IDS[i] + kinds[k]));
         }
      }

      cur = Gfx.Role.UI;
      WIDTHS = new HashMap<>();
   }

   @Environment(EnvType.CLIENT)
   public enum Role {
      UI,
      BOLD,
      LYRICS;
   }
}
