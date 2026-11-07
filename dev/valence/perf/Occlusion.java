package dev.valence.perf;

import dev.valence.config.ValenceConfig;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_2338.class_2339;

@Environment(EnvType.CLIENT)
public final class Occlusion {
   private static final long HIDE_AFTER_NS = 200000000L;
   private static final long FRESH_NS = 300000000L;
   private static final long FORGET_NS = 3000000000L;
   private static final double MAX_DIST = 128.0;
   private static final Map<Integer, Occlusion.State> ENTITIES = new ConcurrentHashMap<>();
   private static final Map<Long, Occlusion.State> BLOCKS = new ConcurrentHashMap<>();
   private static volatile double camX;
   private static volatile double camY;
   private static volatile double camZ;
   private static volatile boolean running;

   private Occlusion() {
   }

   private static boolean enabled() {
      return ValenceConfig.get().perfCulling;
   }

   private static void start() {
      if (!running) {
         synchronized (Occlusion.class) {
            if (!running) {
               running = true;
               Thread t = new Thread(Occlusion::loop, "Valence Culling");
               t.setDaemon(true);
               t.setPriority(2);
               t.start();
            }
         }
      }
   }

   public static boolean hideEntity(class_1297 e, double cx, double cy, double cz) {
      if (!enabled()) {
         return false;
      }

      class_310 mc = class_310.method_1551();
      class_1297 camEntity = mc.method_1560();
      if (e != camEntity
         && !(e instanceof class_1657)
         && !e.method_5851()
         && !e.method_5733()
         && (camEntity == null || !camEntity.method_7325() && !e.method_5821(camEntity) && !camEntity.method_5821(e))) {
         class_238 b = e.method_5829();
         if (!(b.method_17939() > 8.0) && !(b.method_17940() > 8.0) && !(b.method_17941() > 8.0)) {
            camX = cx;
            camY = cy;
            camZ = cz;
            start();
            Occlusion.State s = ENTITIES.computeIfAbsent(e.method_5628(), k -> new Occlusion.State());
            s.x0 = b.field_1323;
            s.y0 = b.field_1322;
            s.z0 = b.field_1321;
            s.x1 = b.field_1320;
            s.y1 = b.field_1325;
            s.z1 = b.field_1324;
            s.blockTarget = false;
            return decide(s);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean hideBlock(class_2338 pos, double cx, double cy, double cz) {
      if (!enabled()) {
         return false;
      }

      class_1297 camEntity = class_310.method_1551().method_1560();
      if (camEntity != null && camEntity.method_7325()) {
         return false;
      }

      camX = cx;
      camY = cy;
      camZ = cz;
      start();
      Occlusion.State s = BLOCKS.computeIfAbsent(pos.method_10063(), k -> new Occlusion.State());
      s.x0 = pos.method_10263();
      s.y0 = pos.method_10264();
      s.z0 = pos.method_10260();
      s.x1 = pos.method_10263() + 1;
      s.y1 = pos.method_10264() + 1;
      s.z1 = pos.method_10260() + 1;
      s.targetCell = pos.method_10063();
      s.blockTarget = true;
      return decide(s);
   }

   private static boolean decide(Occlusion.State s) {
      long now = System.nanoTime();
      s.lastQuery = now;
      long check = s.lastCheck;
      return check != 0L && now - check <= 300000000L ? now - s.lastVisible > 200000000L : false;
   }

   public static void clear() {
      ENTITIES.clear();
      BLOCKS.clear();
   }

   private static void loop() {
      while (true) {
         try {
            long start = System.nanoTime();
            if (enabled()) {
               class_638 level = class_310.method_1551().field_1687;
               if (level == null) {
                  clear();
               } else {
                  sweep(ENTITIES, level, start);
                  sweep(BLOCKS, level, start);
               }
            }

            long took = (System.nanoTime() - start) / 1000000L;
            Thread.sleep(Math.max(4L, 10L - took));
         } catch (InterruptedException ie) {
            return;
         } catch (Throwable var5) {
         }
      }
   }

   private static <K> void sweep(Map<K, Occlusion.State> map, class_638 level, long now) {
      double cx = camX;
      double cy = camY;
      double cz = camZ;
      Iterator<Entry<K, Occlusion.State>> it = map.entrySet().iterator();

      while (it.hasNext()) {
         Occlusion.State s = it.next().getValue();
         if (now - s.lastQuery > 3000000000L) {
            it.remove();
         } else {
            boolean visible;
            try {
               visible = visible(level, s, cx, cy, cz);
            } catch (Throwable t) {
               visible = true;
            }

            long t = System.nanoTime();
            if (visible) {
               s.lastVisible = t;
            }

            s.lastCheck = t;
         }
      }
   }

   private static boolean visible(class_638 level, Occlusion.State s, double cx, double cy, double cz) {
      double x0 = s.x0;
      double y0 = s.y0;
      double z0 = s.z0;
      double x1 = s.x1;
      double y1 = s.y1;
      double z1 = s.z1;
      if (cx > x0 - 2.0 && cx < x1 + 2.0 && cy > y0 - 2.0 && cy < y1 + 2.0 && cz > z0 - 2.0 && cz < z1 + 2.0) {
         return true;
      }

      double mx = (x0 + x1) * 0.5;
      double my = (y0 + y1) * 0.5;
      double mz = (z0 + z1) * 0.5;
      double dx = mx - cx;
      double dy = my - cy;
      double dz = mz - cz;
      if (dx * dx + dy * dy + dz * dz > 16384.0) {
         return true;
      }

      class_2339 mp = new class_2339();
      double ix = Math.min(0.08, (x1 - x0) * 0.25);
      double iy = Math.min(0.08, (y1 - y0) * 0.25);
      double iz = Math.min(0.08, (z1 - z0) * 0.25);
      if (clearRay(level, mp, cx, cy, cz, mx, my, mz, s)) {
         return true;
      }

      for (int i = 0; i < 8; i++) {
         double px = (i & 1) == 0 ? x0 + ix : x1 - ix;
         double py = (i & 2) == 0 ? y0 + iy : y1 - iy;
         double pz = (i & 4) == 0 ? z0 + iz : z1 - iz;
         if (clearRay(level, mp, cx, cy, cz, px, py, pz, s)) {
            return true;
         }
      }

      return false;
   }

   private static boolean clearRay(class_638 level, class_2339 mp, double ax, double ay, double az, double bx, double by, double bz, Occlusion.State s) {
      int x = floor(ax);
      int y = floor(ay);
      int z = floor(az);
      int tx = floor(bx);
      int ty = floor(by);
      int tz = floor(bz);
      double dx = bx - ax;
      double dy = by - ay;
      double dz = bz - az;
      int sx = dx > 0.0 ? 1 : (dx < 0.0 ? -1 : 0);
      int sy = dy > 0.0 ? 1 : (dy < 0.0 ? -1 : 0);
      int sz = dz > 0.0 ? 1 : (dz < 0.0 ? -1 : 0);
      double tdx = sx != 0 ? Math.abs(1.0 / dx) : Double.MAX_VALUE;
      double tdy = sy != 0 ? Math.abs(1.0 / dy) : Double.MAX_VALUE;
      double tdz = sz != 0 ? Math.abs(1.0 / dz) : Double.MAX_VALUE;
      double tmx = sx > 0 ? (x + 1 - ax) * tdx : (sx < 0 ? (ax - x) * tdx : Double.MAX_VALUE);
      double tmy = sy > 0 ? (y + 1 - ay) * tdy : (sy < 0 ? (ay - y) * tdy : Double.MAX_VALUE);
      double tmz = sz > 0 ? (z + 1 - az) * tdz : (sz < 0 ? (az - z) * tdz : Double.MAX_VALUE);
      int steps = Math.abs(tx - x) + Math.abs(ty - y) + Math.abs(tz - z);

      for (int i = 0; i < steps; i++) {
         if (tmx < tmy && tmx < tmz) {
            x += sx;
            tmx += tdx;
         } else if (tmy < tmz) {
            y += sy;
            tmy += tdy;
         } else {
            z += sz;
            tmz += tdz;
         }

         if (x == tx && y == ty && z == tz) {
            return true;
         }

         if (s.blockTarget && class_2338.method_10064(x, y, z) == s.targetCell) {
            return true;
         }

         if (y >= level.method_31607() && y <= level.method_31600()) {
            class_2680 st = level.method_8320(mp.method_10103(x, y, z));
            if (st.method_26225() && st.method_26216()) {
               return false;
            }
         }
      }

      return true;
   }

   private static int floor(double v) {
      int i = (int)v;
      return v < i ? i - 1 : i;
   }

   @Environment(EnvType.CLIENT)
   private static final class State {
      volatile double x0;
      volatile double y0;
      volatile double z0;
      volatile double x1;
      volatile double y1;
      volatile double z1;
      volatile long lastQuery;
      volatile long lastVisible;
      volatile long lastCheck;
      volatile long targetCell;
      volatile boolean blockTarget;
   }
}
