package dev.valence.cord.sky;

import dev.valence.cord.NeonLyrics;
import dev.valence.cord.config.Config;
import dev.valence.cord.hud.Anim;
import dev.valence.cord.hud.Gfx;
import dev.valence.cord.hud.Island;
import dev.valence.cord.util.Fx;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1937;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4063;
import net.minecraft.class_7172;
import net.minecraft.class_9779;

@Environment(EnvType.CLIENT)
public final class SkyRenderer {
   private static float sw;
   private static float sh;
   private static float focal;
   private static double rx;
   private static double ry;
   private static double rz;
   private static double ux;
   private static double uy;
   private static double uz;
   private static double fx;
   private static double fy;
   private static double fz;
   private static float camYaw;
   private static float camPitch;
   private static final float[] P = new float[3];
   private static final Anim visible = new Anim(0.0, 4.0);
   private static final Random RND = new Random();
   private static float[][] stars;
   private static final List<float[]> shooting = new ArrayList<>();
   private static long nextShooting = 0L;
   private static double[][] parts = new double[0][];
   private static SkyPreset.Weather partType = null;
   private static long lastNanos = System.nanoTime();
   private static long flashAt = 0L;
   private static long nextFlash = 0L;
   private static boolean forcedRain = false;
   private static int lastSky = -99;

   private SkyRenderer() {
   }

   private static void setupCamera(class_332 g, class_310 mc, float pt) {
      sw = g.method_51421();
      sh = g.method_51443();
      double fov = Math.toRadians(((Integer)mc.field_1690.method_41808().method_41753()).intValue());
      focal = (float)(sh / 2.0 / Math.tan(fov / 2.0));
      camYaw = mc.field_1724.method_5705(pt);
      camPitch = mc.field_1724.method_5695(pt);
      double y = Math.toRadians(camYaw);
      double p = Math.toRadians(camPitch);
      fx = -Math.sin(y) * Math.cos(p);
      fy = -Math.sin(p);
      fz = Math.cos(y) * Math.cos(p);
      rx = -Math.cos(y);
      ry = 0.0;
      rz = -Math.sin(y);
      ux = ry * fz - rz * fy;
      uy = rz * fx - rx * fz;
      uz = rx * fy - ry * fx;
   }

   private static boolean project(double dx, double dy, double dz) {
      double zc = dx * fx + dy * fy + dz * fz;
      if (zc < 0.02) {
         return false;
      }

      double xc = dx * rx + dy * ry + dz * rz;
      double yc = dx * ux + dy * uy + dz * uz;
      P[0] = (float)(sw / 2.0F + focal * xc / zc);
      P[1] = (float)(sh / 2.0F - focal * yc / zc);
      P[2] = (float)zc;
      return true;
   }

   private static boolean projectDir(double azDeg, double elDeg) {
      double a = Math.toRadians(azDeg);
      double e = Math.toRadians(elDeg);
      return project(-Math.sin(a) * Math.cos(e), Math.sin(e), Math.cos(a) * Math.cos(e));
   }

   private static void initStars() {
      Random r = new Random(1337L);
      stars = new float[520][5];

      for (int i = 0; i < stars.length; i++) {
         float[] s = stars[i];
         s[0] = r.nextFloat() * 360.0F;
         s[1] = (float)Math.toDegrees(Math.asin(0.05 + r.nextFloat() * 0.95));
         s[2] = r.nextFloat() < 0.12F ? 2.0F : 1.0F;
         s[3] = 0.35F + r.nextFloat() * 0.65F;
         s[4] = r.nextFloat() * 6.28F;
      }
   }

   public static void tick(class_310 mc) {
      Config c = NeonLyrics.cfg();
      if (c.sky != lastSky) {
         if (lastSky != -99 && mc.field_1724 != null) {
            SkyPreset p = SkyPreset.get(c.sky);
            Island.notify(p == null ? Fx.t("neonlyrics.sky.off") : Fx.t("neonlyrics.sky.enabled", Fx.t(p.key())), p != null);
         }

         lastSky = c.sky;
      }

      if (mc.field_1687 == null) {
         forcedRain = false;
      } else {
         updateClouds(mc, c);
         SkyPreset p = SkyPreset.get(c.sky);
         boolean wantRain = p != null
            && c.realWeather
            && (p.weather == SkyPreset.Weather.RAIN || p.weather == SkyPreset.Weather.THUNDER)
            && mc.field_1687.method_27983() == class_1937.field_25179;
         if (wantRain) {
            mc.field_1687.method_8519(1.0F);
            mc.field_1687.method_8496(p.weather == SkyPreset.Weather.THUNDER ? 1.0F : 0.0F);
            forcedRain = true;
         } else if (forcedRain) {
            mc.field_1687.method_8519(0.0F);
            mc.field_1687.method_8496(0.0F);
            forcedRain = false;
         }
      }
   }

   private static void updateClouds(class_310 mc, Config c) {
      boolean hide = c.hideClouds && c.sky >= 0;
      class_7172<class_4063> opt = mc.field_1690.method_42528();
      if (hide && opt.method_41753() != class_4063.field_18162) {
         c.cloudsBefore = ((class_4063)opt.method_41753()).name();
         opt.method_41748(class_4063.field_18162);
         NeonLyrics.saveConfig();
      } else if (!hide && !c.cloudsBefore.isEmpty()) {
         try {
            opt.method_41748(class_4063.valueOf(c.cloudsBefore));
         } catch (Exception var5) {
         }

         c.cloudsBefore = "";
         NeonLyrics.saveConfig();
      }
   }

   public static void render(class_332 g, class_9779 delta) {
      class_310 mc = class_310.method_1551();
      if (!mc.field_1690.field_1842 && mc.field_1724 != null && mc.field_1687 != null) {
         Config c = NeonLyrics.cfg();
         long nowNs = System.nanoTime();
         float dt = Math.min(0.1F, (float)(nowNs - lastNanos) / 1.0E9F);
         lastNanos = nowNs;
         float pt = delta.method_60637(false);
         setupCamera(g, mc, pt);
         SkyPreset p = SkyPreset.get(c.sky);
         float old = Gfx.alpha;
         Gfx.alpha = 1.0F;
         if (p != null && p.weather == SkyPreset.Weather.THUNDER && mc.field_1687.method_27983() == class_1937.field_25179) {
            drawLightning(g);
         }

         Gfx.alpha = old;
      }
   }

   private static void drawLightning(class_332 g) {
      long now = System.currentTimeMillis();
      if (now > nextFlash) {
         flashAt = now;
         nextFlash = now + 5000L + RND.nextInt(9000);
      }

      long age = now - flashAt;
      if (age < 600L) {
         float a = age < 90L ? 0.32F : (age < 180L ? 0.05F : (age < 260L ? 0.22F : 0.22F * (1.0F - (float)(age - 260L) / 340.0F)));
         Gfx.rect(g, 0, 0, (int)sw, (int)sh, 15266047, Math.max(0.0F, a));
      }
   }
}
