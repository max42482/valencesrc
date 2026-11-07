package dev.valence.license;

import dev.valence.gui.Anim;
import dev.valence.gui.OptionList;
import dev.valence.gui.Theme;
import dev.valence.gui.UiSounds;
import dev.valence.gui.ValenceScreen;
import dev.valence.styles.StyleManager;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_342;
import net.minecraft.class_437;
import net.minecraft.class_5481;
import net.minecraft.class_6880;

@Environment(EnvType.CLIENT)
public class LicenseScreen extends class_437 {
   private static final int W = 280;
   private static final int H = 190;
   private static final int OK_COLOR = -9508952;
   private static final int BAD_COLOR = -495247;
   private final class_437 parent;
   private final boolean openMenuAfter;
   private class_342 field;
   private long openedAt;
   private boolean busy;
   private long busySince;
   private String message = "";
   private int messageColor = -8095322;
   private long messageAt;
   private long shakeAt;
   private long successAt;
   private String lastText = "";

   public LicenseScreen(class_437 parent, boolean openMenuAfter) {
      super(class_2561.method_43471("valence.license.title"));
      this.parent = parent;
      this.openMenuAfter = openMenuAfter;
   }

   private int px() {
      return (this.field_22789 - 280) / 2;
   }

   private int py() {
      return (this.field_22790 - 190) / 2;
   }

   protected void method_25426() {
      if (this.openedAt == 0L) {
         this.openedAt = System.currentTimeMillis();
      }

      String keep = this.field != null ? this.field.method_1882() : "";
      this.field = new class_342(this.field_22793, this.px() + 58, this.py() + 100, 180, 12, class_2561.method_43471("valence.license.field"));
      this.field.method_1858(false);
      this.field.method_1880(9);
      this.field.method_1868(this.successAt != 0L ? -9508952 : -1);
      this.field.method_1852(keep);
      this.field.method_47404(class_2561.method_43470("XXXX-XXXX").method_54663(-9542008));
      this.field.method_1863(this::format);
      this.method_37063(this.field);
      this.method_48265(this.field);
   }

   private void format(String s) {
      String n = License.normalize(s);
      if (n.length() > 8) {
         n = n.substring(0, 8);
      }

      String f = n.length() > 4 ? n.substring(0, 4) + "-" + n.substring(4) : n;
      if (!f.equals(s)) {
         this.field.method_1852(f);
         this.field.method_1872(false);
      }

      if (!f.equals(this.lastText)) {
         this.lastText = f;
         if (!this.busy && this.successAt == 0L && this.messageColor == -495247) {
            this.message = "";
         }
      }
   }

   private void redeem() {
      if (!this.busy && this.successAt == 0L) {
         String code = this.field.method_1882();
         if (License.normalize(code).length() != 8) {
            this.fail(class_2561.method_43471("valence.license.short").getString());
         } else {
            this.busy = true;
            this.busySince = System.currentTimeMillis();
            UiSounds.click();
            License.redeem(code).thenAccept(r -> class_310.method_1551().execute(() -> this.done(r)));
         }
      }
   }

   private void done(License.Result r) {
      this.busy = false;
      switch (r) {
         case OK:
            this.successAt = System.currentTimeMillis();
            this.message = class_2561.method_43471("valence.license.ok").getString();
            this.messageColor = -9508952;
            this.messageAt = this.successAt;
            this.field.method_1868(-9508952);
            this.field.method_1860(-9508952);
            this.field.method_1888(false);
            play(class_3417.field_14793, 1.5F, 0.9F);
            play(class_3417.field_14725, 2.0F, 0.5F);

            try {
               StyleManager.markDirty();
               StyleManager.apply();
            } catch (Throwable var3) {
            }
            break;
         case USED:
            this.fail(class_2561.method_43471("valence.license.used").getString());
            break;
         case OFFLINE:
            this.fail(class_2561.method_43471("valence.license.offline").getString());
            break;
         case INVALID:
            this.fail(class_2561.method_43471("valence.license.invalid").getString());
            break;
         default:
            this.fail(class_2561.method_43471("valence.license.error").getString());
      }
   }

   private void fail(String text) {
      this.message = text;
      this.messageColor = -495247;
      this.messageAt = System.currentTimeMillis();
      this.shakeAt = this.messageAt;
      play(class_3417.field_14624, 0.75F, 1.0F);
      play(class_3417.field_14624, 0.6F, 0.7F);
   }

   private static void play(class_6880<class_3414> sound, float pitch, float volume) {
      try {
         class_310.method_1551().method_1483().method_4873(class_1109.method_4757((class_3414)sound.comp_349(), pitch, volume));
      } catch (Throwable var4) {
      }
   }

   private float closing(long now) {
      return this.successAt == 0L ? 0.0F : Theme.easeOutCubic(Math.max(0.0F, Math.min(1.0F, (float)(now - this.successAt - 700L) / 420.0F)));
   }

   public void method_25393() {
      super.method_25393();
      if (this.successAt != 0L && System.currentTimeMillis() - this.successAt > 1150L) {
         class_310 mc = class_310.method_1551();
         mc.method_1507(this.openMenuAfter ? new ValenceScreen(this.parent) : this.parent);
      }
   }

   public void method_25420(class_332 g, int mouseX, int mouseY, float partialTick) {
      if (this.field_22787 != null && this.field_22787.field_1687 != null) {
         super.method_25420(g, mouseX, mouseY, partialTick);
      }

      long now = System.currentTimeMillis();
      float e = Theme.easeOutCubic(Math.min(1.0F, (float)(now - this.openedAt) / 260.0F)) * (1.0F - this.closing(now));
      g.method_25296(0, 0, this.field_22789, this.field_22790, Theme.withAlpha(-1945697265, e), Theme.withAlpha(-1291320302, e));
   }

   private void centered(class_332 g, class_327 f, class_2561 text, int cx, int y, float scale, int maxW, int color) {
      List<class_5481> lines = f.method_1728(text, Math.round(maxW / scale));

      for (int i = 0; i < lines.size(); i++) {
         g.method_51448().pushMatrix();
         g.method_51448().translate(cx, y + i * 10 * scale);
         g.method_51448().scale(scale, scale);
         g.method_35719(f, lines.get(i), 0, 0, color);
         g.method_51448().popMatrix();
      }
   }

   public void method_25394(class_332 g, int mx, int my, float pt) {
      long now = System.currentTimeMillis();
      float close = this.closing(now);
      float open = Theme.easeOutCubic(Math.min(1.0F, (float)(now - this.openedAt) / 360.0F)) * (1.0F - close);
      class_327 f = this.field_22793;
      int x1 = this.px();
      int y1 = this.py();
      int lx = x1 + 140;
      float shake = this.shakeAt == 0L ? 0.0F : Math.max(0.0F, 1.0F - (float)(now - this.shakeAt) / 420.0F);
      float fsx = (float)Math.sin((now - this.shakeAt) / 22.0) * 6.0F * shake * shake;
      float good = this.successAt == 0L ? 0.0F : Math.min(1.0F, (float)(now - this.successAt) / 250.0F);
      g.method_51448().pushMatrix();
      float sc = 0.94F + 0.06F * open;
      g.method_51448().translate(this.field_22789 / 2.0F, this.field_22790 / 2.0F + (1.0F - open) * 10.0F);
      g.method_51448().scale(sc, sc);
      g.method_51448().translate(-this.field_22789 / 2.0F, -this.field_22790 / 2.0F);
      Theme.glass(g, x1, y1, x1 + 280, y1 + 190, Theme.withAlpha(-1207105001, 0.4F + 0.6F * open), Theme.withAlpha(1086959871, open), 0.55F * open);
      Theme.sheen(g, x1, y1, x1 + 280, y1 + 40, 0.35F * open);
      float pulse = 0.3F + 0.1F * (float)Math.sin(now / 600.0);
      Theme.glow(g, lx - 15, y1 + 14, lx + 15, y1 + 44, -7643914, pulse * open);
      Theme.image(g, Theme.LOGO, lx - 14, y1 + 15, 28, 28, 128, 128, Theme.withAlpha(-1, open));
      g.method_51448().pushMatrix();
      g.method_51448().translate(lx, y1 + 50);
      g.method_51448().scale(1.6F, 1.6F);
      g.method_27534(f, class_2561.method_43470("VALENCE").method_27694(s -> s.method_10982(true)), 0, 0, Theme.withAlpha(-1, open));
      g.method_51448().popMatrix();
      this.centered(g, f, class_2561.method_43471("valence.license.subtitle"), lx, y1 + 69, 0.8F, 250, Theme.withAlpha(-4608298, open));
      int fx1 = x1 + 34;
      int fy1 = y1 + 92;
      int fx2 = x1 + 280 - 34;
      int fy2 = fy1 + 24;
      float fh = Anim.approach(Anim.key(9100, 0), this.field.method_25370() ? 1.0F : 0.0F, 12.0F);
      int line = Theme.lerp(872415231, -1433707274, fh);
      line = Theme.lerp(line, -495247, shake);
      line = Theme.lerp(line, -9508952, good);
      g.method_51448().pushMatrix();
      g.method_51448().translate(fsx, 0.0F);
      if (fh > 0.01F && good == 0.0F && shake == 0.0F) {
         Theme.glow(g, fx1, fy1, fx2, fy2, -7643914, 0.25F * fh * open);
      }

      if (shake > 0.01F) {
         Theme.glow(g, fx1, fy1, fx2, fy2, -495247, 0.4F * shake);
      }

      if (good > 0.01F) {
         Theme.glow(g, fx1, fy1, fx2, fy2, -9508952, 0.4F * good * open);
      }

      Theme.pill(g, fx1, fy1, fx2, fy2, Theme.withAlpha(1073741824, open));
      Theme.pillLine(g, fx1, fy1, fx2, fy2, Theme.withAlpha(line, open));
      int iconCol = good > 0.0F ? -9508952 : Theme.lerp(Theme.lerp(-8095322, -4745985, fh), -495247, shake);
      Theme.icon(g, good > 0.0F ? "check" : "star", fx1 + 8, fy1 + 7, 10, Theme.withAlpha(iconCol, open));
      g.method_51448().popMatrix();
      this.field.method_46421(Math.round(fx1 + 24 + fsx));
      this.field.method_46419(fy1 + 8);
      this.field.method_25358(fx2 - fx1 - 32);
      this.field.method_25350(open);
      super.method_25394(g, mx, my, pt);
      int bw = 124;
      int bx1 = lx - bw / 2;
      int by1 = y1 + 128;
      class_2561 label;
      if (this.successAt != 0L) {
         label = class_2561.method_43471("valence.license.done");
      } else if (this.busy) {
         int dots = (int)((now - this.busySince) / 300L % 4L);
         label = class_2561.method_43470(class_2561.method_43471("valence.license.checking").getString() + ".".repeat(dots));
      } else {
         label = class_2561.method_43471("valence.license.redeem");
      }

      if (open > 0.05F) {
         OptionList.accentButton(g, bx1, by1, bx1 + bw, by1 + 20, label, mx, my, Anim.key(9100, 1));
      }

      int ty = y1 + 158;
      if (!this.message.isEmpty()) {
         float ma = Math.min(1.0F, (float)(now - this.messageAt) / 200.0F) * open;
         g.method_51448().pushMatrix();
         g.method_51448().translate(fsx, (1.0F - ma) * 3.0F);
         this.centered(g, f, class_2561.method_43470(this.message), lx, ty, 0.8F, 250, Theme.withAlpha(this.messageColor, ma));
         g.method_51448().popMatrix();
      } else {
         this.centered(g, f, class_2561.method_43471("valence.license.hint"), lx, ty, 0.7F, 250, Theme.withAlpha(-8095322, open));
      }

      g.method_51448().popMatrix();
   }

   private boolean overButton(double mx, double my) {
      int lx = this.px() + 140;
      int by1 = this.py() + 128;
      return mx >= lx - 62 && mx < lx + 62 && my >= by1 && my < by1 + 20;
   }

   public boolean method_25402(class_11909 event, boolean doubleClick) {
      if (event.method_74245() == 0 && this.overButton(event.comp_4798(), event.comp_4799())) {
         this.redeem();
         return true;
      } else {
         return super.method_25402(event, doubleClick);
      }
   }

   public boolean method_25404(class_11908 event) {
      if (event.comp_4795() != 257 && event.comp_4795() != 335) {
         return super.method_25404(event);
      }

      this.redeem();
      return true;
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25419() {
      class_310.method_1551().method_1507(this.parent);
   }
}
