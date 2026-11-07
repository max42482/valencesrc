package dev.valence.gui;

import dev.valence.ValenceClient;
import dev.valence.config.ValenceConfig;
import dev.valence.cord.lyrics.SpotifyClient;
import dev.valence.gui.pages.HudPage;
import dev.valence.gui.pages.InfoPage;
import dev.valence.gui.pages.LyricsPage;
import dev.valence.gui.pages.OverviewPage;
import dev.valence.gui.pages.PerformancePage;
import dev.valence.gui.pages.SettingsPage;
import dev.valence.gui.pages.SkyPage;
import dev.valence.gui.pages.StylesPage;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_437;

@Environment(EnvType.CLIENT)
public class ValenceScreen extends class_437 {
   private static int lastPage = 0;
   private static final int NAV_Y = 50;
   private static final int ITEM_H_MAX = 22;
   private final class_437 parent;
   private final List<Page> pages = List.of(
      new OverviewPage(), new SkyPage(), new LyricsPage(), new StylesPage(), new HudPage(), new PerformancePage(), new SettingsPage(), new InfoPage()
   );
   private int page;
   private int previousPage;
   private int px1;
   private int py1;
   private int px2;
   private int py2;
   private int sidebarW;
   private float open;
   private boolean closing;
   private float pageAnim = 1.0F;
   private float glowX = -1.0F;
   private float glowY = -1.0F;
   private float tabAnim;
   private boolean playedOpen;
   private float ui = 1.0F;
   private int vw;
   private int vh;
   private long openedAt = -1L;
   private long closingAt;
   private final MediaStage stage = new MediaStage();
   private float shift;
   private float stageIn;

   private int itemH() {
      int free = this.py2 - this.py1 - 50 - 40;
      return Math.max(15, Math.min(22, free / Math.max(1, this.pages.size()) - 2));
   }

   private int itemStep() {
      return this.itemH() + (this.itemH() >= 20 ? 3 : 2);
   }

   public ValenceScreen(class_437 parent) {
      super(class_2561.method_43471("valence.menu.title"));
      this.parent = parent;
      this.page = lastPage;
      this.previousPage = this.page;
      this.tabAnim = this.page;
   }

   protected void method_25426() {
      this.ui = UiScale.factor();
      this.vw = Math.max(1, Math.round(this.field_22789 / this.ui));
      this.vh = Math.max(1, Math.round(this.field_22790 / this.ui));
      int pw = Math.min(600, this.vw - 24);
      int ph = Math.min(350, this.vh - 24);
      this.px1 = (this.vw - pw) / 2;
      this.py1 = (this.vh - ph) / 2;
      this.px2 = this.px1 + pw;
      this.py2 = this.py1 + ph;
      this.sidebarW = Math.max(100, Math.min(128, pw / 4));
      int cx = this.px1 + this.sidebarW + 14;
      int cy = this.py1 + 44;
      int cw = this.px2 - cx - 14;
      int ch = this.py2 - cy - 12;

      for (Page p : this.pages) {
         p.init(this, cx, cy, cw, ch);
      }

      if (!this.playedOpen) {
         this.playedOpen = true;
         UiSounds.open();
         Anim.restartClock();
      }
   }

   public void openPage(int index) {
      if (index >= 0 && index < this.pages.size() && index != this.page) {
         this.previousPage = this.page;
         this.page = index;
         lastPage = index;
         this.pageAnim = 0.0F;
      }
   }

   public int pageIndexOf(Class<? extends Page> type) {
      for (int i = 0; i < this.pages.size(); i++) {
         if (type.isInstance(this.pages.get(i))) {
            return i;
         }
      }

      return 0;
   }

   public void method_25393() {
      this.pages.get(this.page).tick();
   }

   public void method_25420(class_332 g, int mouseX, int mouseY, float partialTick) {
      if (this.field_22787.field_1687 == null) {
         super.method_25420(g, mouseX, mouseY, partialTick);
      } else {
         if (ValenceConfig.get().menuBlur) {
            this.method_57734(g);
         }

         float e = Theme.easeOutCubic(this.open);
         g.method_25296(0, 0, this.field_22789, this.field_22790, Theme.withAlpha(-1945697265, e), Theme.withAlpha(-1291320302, e));
      }
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25419() {
      ValenceConfig.save();
      if (ValenceConfig.get().menuAnimations && !this.closing) {
         this.closing = true;
         this.closingAt = System.nanoTime();
      } else {
         this.finishClose();
      }
   }

   private void finishClose() {
      Anim.reset();
      this.field_22787.method_1507(this.parent);
   }

   public void method_25394(class_332 g, int rawMouseX, int rawMouseY, float partialTick) {
      Anim.frame();
      float dt = Anim.dt();
      ValenceConfig cfg = ValenceConfig.get();
      long nowNs = System.nanoTime();
      if (this.openedAt < 0L) {
         this.openedAt = nowNs;
      }

      if (this.closing) {
         float t = Math.min(1.0F, (float)(nowNs - this.closingAt) / 1.7E8F);
         this.open = 1.0F - t * t;
         if (t >= 1.0F) {
            this.finishClose();
            return;
         }
      } else {
         this.open = cfg.menuAnimations ? Math.min(1.0F, (float)(nowNs - this.openedAt) / 3.4E8F) : 1.0F;
      }

      this.pageAnim = cfg.menuAnimations ? Math.min(1.0F, this.pageAnim + dt / 0.26F) : 1.0F;
      super.method_25394(g, rawMouseX, rawMouseY, partialTick);
      float ease = this.closing ? this.open : easeOutQuint(this.open);
      class_327 font = this.field_22793;
      boolean hasSong = SpotifyClient.nowOrLast().hasTrack();
      int room = (this.vw - (this.px2 - this.px1)) / 2;
      float targetShift = hasSong && room >= 110 ? Math.max(0.0F, Math.min(room - 16.0F, 240.0F - room)) : 0.0F;
      boolean stageFits = hasSong && room + targetShift - 34.0F >= 150.0F;
      this.shift = this.shift + (targetShift - this.shift) * Anim.k(9.0F);
      this.stageIn = this.stageIn + ((stageFits ? 1.0F : 0.0F) - this.stageIn) * Anim.k(7.0F);
      int mouseX = Math.round(rawMouseX / this.ui - this.shift);
      int mouseY = Math.round(rawMouseY / this.ui);
      g.method_51448().pushMatrix();
      g.method_51448().scale(this.ui, this.ui);
      RoundedImage.menuScale = this.ui;
      if (this.stageIn > 0.01F) {
         int sx0 = 18;
         int sx1 = Math.round(this.px1 + this.shift) - 22;
         this.stage
            .render(
               g,
               sx0 - Math.round((1.0F - this.stageIn * ease) * 24.0F),
               this.py1 + 6,
               sx1 - sx0,
               this.py2 - this.py1 - 12,
               this.closing ? -1000 : Math.round(rawMouseX / this.ui),
               this.closing ? -1000 : mouseY,
               this.stageIn * ease
            );
      }

      g.method_51448().translate(this.shift, (1.0F - ease) * 14.0F);
      Theme.glass(g, this.px1, this.py1, this.px2, this.py2, Theme.withAlpha(-1207105001, 0.4F + 0.6F * ease), 1086959871, 0.55F * ease);
      Theme.round(g, this.px1 + 5, this.py1 + 5, this.px1 + this.sidebarW, this.py2 - 5, 234881023);
      Theme.outline(g, this.px1 + 5, this.py1 + 5, this.px1 + this.sidebarW, this.py2 - 5, 352321535);
      Theme.glow(g, this.px1 + 12, this.py1 + 12, this.px1 + 36, this.py1 + 36, -7643914, 0.35F);
      Theme.image(g, Theme.LOGO, this.px1 + 11, this.py1 + 11, 26, 26, 128, 128, -1);
      g.method_51448().pushMatrix();
      g.method_51448().translate(this.px1 + 41, this.py1 + 14);
      g.method_51448().scale(1.35F, 1.35F);
      g.method_51439(font, class_2561.method_43470("Valence"), 0, 0, -1, false);
      g.method_51448().popMatrix();
      g.method_51448().pushMatrix();
      g.method_51448().translate(this.px1 + 41, this.py1 + 28);
      g.method_51448().scale(0.7F, 0.7F);
      g.method_51439(font, class_2561.method_43471("valence.menu.tagline"), 0, 0, -8095322, false);
      g.method_51448().popMatrix();
      this.tabAnim = this.tabAnim + (this.page - this.tabAnim) * Anim.k(16.0F);
      int navY = this.py1 + 50;
      int hlY = Math.round(navY + this.tabAnim * this.itemStep());
      Theme.glow(g, this.px1 + 11, hlY, this.px1 + this.sidebarW - 6, hlY + this.itemH(), -7643914, 0.3F);
      Theme.round(g, this.px1 + 11, hlY, this.px1 + this.sidebarW - 6, hlY + this.itemH(), 1384865014);
      Theme.sheen(g, this.px1 + 11, hlY, this.px1 + this.sidebarW - 6, hlY + this.itemH(), 0.5F);
      Theme.outline(g, this.px1 + 11, hlY, this.px1 + this.sidebarW - 6, hlY + this.itemH(), -1718919946);
      Theme.pill(g, this.px1 + 8, hlY + 6, this.px1 + 10, hlY + this.itemH() - 6, -4745985);

      for (int i = 0; i < this.pages.size(); i++) {
         Page p = this.pages.get(i);
         int iy = navY + i * this.itemStep();
         boolean hovered = !this.closing && mouseX >= this.px1 + 11 && mouseX < this.px1 + this.sidebarW - 6 && mouseY >= iy && mouseY < iy + this.itemH();
         float hv = Anim.hover(Anim.key(1, i), hovered && i != this.page);
         if (hv > 0.01F && i != this.page) {
            Theme.round(g, this.px1 + 11, iy, this.px1 + this.sidebarW - 6, iy + this.itemH(), Theme.withAlpha(486539263, hv));
         }

         int col = i == this.page ? -1 : Theme.lerp(-4608298, -724737, hv);
         int shift = Math.round(hv * 2.0F);
         Theme.icon(g, p.icon(), this.px1 + 17 + shift, iy + (this.itemH() - 12) / 2, 12, i == this.page ? -4745985 : col);
         g.method_51439(font, class_2561.method_43471(p.nameKey()), this.px1 + 34 + shift, iy + (this.itemH() - 8) / 2, col, false);
      }

      int vy = this.py2 - 27;
      g.method_25294(this.px1 + 12, vy - 7, this.px1 + this.sidebarW - 7, vy - 6, 352321535);
      Theme.image(g, Theme.LOGO, this.px1 + 12, vy, 16, 16, 128, 128, -1);
      g.method_51433(font, "Valence", this.px1 + 32, vy, -4608298, false);
      g.method_51448().pushMatrix();
      g.method_51448().translate(this.px1 + 32, vy + 10);
      g.method_51448().scale(0.75F, 0.75F);
      g.method_51433(font, "v1.9.2", 0, 0, -8095322, false);
      g.method_51448().popMatrix();
      Page current = this.pages.get(this.page);
      int hx = this.px1 + this.sidebarW + 14;
      float pe = Theme.easeOutCubic(this.pageAnim);
      float slide = (1.0F - pe) * 10.0F * (this.page >= this.previousPage ? 1 : -1);
      g.method_51448().pushMatrix();
      g.method_51448().translate(hx, this.py1 + 12 + (1.0F - pe) * 3.0F);
      g.method_51448().scale(1.4F, 1.4F);
      g.method_51439(font, class_2561.method_43471(current.nameKey()), 0, 0, Theme.withAlpha(-724737, 0.3F + 0.7F * pe), false);
      g.method_51448().popMatrix();
      g.method_51439(font, class_2561.method_43471(current.subtitleKey()), hx, this.py1 + 27, Theme.withAlpha(-8095322, 0.3F + 0.7F * pe), false);
      String[] slogan = class_2561.method_43471("valence.menu.slogan").getString().split("\n");
      int sloganW = 0;

      for (String line : slogan) {
         sloganW = Math.max(sloganW, font.method_1727(line));
      }

      int rx = this.px2 - 36 - sloganW;
      if (rx > hx + 170) {
         Theme.pill(g, rx - 7, this.py1 + 12, rx - 5, this.py1 + 12 + slogan.length * 10, -7643914);

         for (int i = 0; i < slogan.length; i++) {
            g.method_51433(font, slogan[i], rx, this.py1 + 13 + i * 10, -4608298, false);
         }
      }

      int bx = this.px2 - 24;
      int by = this.py1 + 9;
      boolean closeHover = mouseX >= bx && mouseX < bx + 15 && mouseY >= by && mouseY < by + 15;
      float ch = Anim.hover(Anim.key(2, 0), closeHover);
      Theme.glow(g, bx, by, bx + 15, by + 15, -495247, 0.3F * ch);
      Theme.pill(g, bx, by, bx + 15, by + 15, Theme.lerp(452984831, 1727558001, ch));
      Theme.pillLine(g, bx, by, bx + 15, by + 15, Theme.lerp(654311423, -1711295308, ch));
      Theme.icon(g, "close", bx + 4, by + 4, 7, Theme.lerp(-4608298, -1, ch));
      g.method_25294(hx, this.py1 + 39, this.px2 - 14, this.py1 + 40, 352321535);
      g.method_51448().pushMatrix();
      g.method_51448().translate(slide, 0.0F);
      current.render(g, this.closing ? -1000 : Math.round(mouseX - slide), this.closing ? -1000 : mouseY, partialTick);
      g.method_51448().popMatrix();
      float cover = Math.max(1.0F - pe, 1.0F - ease);
      if (cover > 0.001F) {
         Theme.round(g, hx - 4, this.py1 + 41, this.px2 - 6, this.py2 - 6, Theme.withAlpha(-536016361, cover));
      }

      if (cfg.mouseGlow && mouseX >= this.px1 && mouseX < this.px2 && mouseY >= this.py1 && mouseY < this.py2) {
         if (this.glowX < 0.0F) {
            this.glowX = mouseX;
            this.glowY = mouseY;
         }

         float k = Math.min(1.0F, dt * 24.0F);
         this.glowX = this.glowX + (mouseX - this.glowX) * k;
         this.glowY = this.glowY + (mouseY - this.glowY) * k;
         int r = 50;
         g.method_44379(this.px1 + 1, this.py1 + 1, this.px2 - 1, this.py2 - 1);
         Theme.image(g, Theme.SPOTLIGHT, Math.round(this.glowX) - r, Math.round(this.glowY) - r, r * 2, r * 2, 128, 128, 819115775);
         g.method_44380();
      } else {
         this.glowX = -1.0F;
      }

      g.method_51448().popMatrix();
      RoundedImage.menuScale = 1.0F;
   }

   private static float easeOutQuint(float t) {
      float k = 1.0F - Math.max(0.0F, Math.min(1.0F, t));
      return 1.0F - k * k * k * k * k;
   }

   private double mx(double rawX) {
      return rawX / this.ui - this.shift;
   }

   private double my(double rawY) {
      return rawY / this.ui;
   }

   private class_11909 scaled(class_11909 e) {
      return new class_11909(this.mx(e.comp_4798()), this.my(e.comp_4799()), e.comp_4800());
   }

   public boolean method_25402(class_11909 event, boolean doubleClick) {
      if (this.closing) {
         return true;
      }

      if (this.stageIn > 0.5F && this.stage.click(event.comp_4798() / this.ui, event.comp_4799() / this.ui)) {
         return true;
      }

      double mx = this.mx(event.comp_4798());
      double my = this.my(event.comp_4799());
      int bx = this.px2 - 24;
      int by = this.py1 + 9;
      if (mx >= bx && mx < bx + 15 && my >= by && my < by + 15) {
         UiSounds.click();
         this.method_25419();
         return true;
      }

      int navY = this.py1 + 50;

      for (int i = 0; i < this.pages.size(); i++) {
         int iy = navY + i * this.itemStep();
         if (mx >= this.px1 + 11 && mx < this.px1 + this.sidebarW - 6 && my >= iy && my < iy + this.itemH()) {
            if (i != this.page) {
               UiSounds.click();
               this.openPage(i);
            }

            return true;
         }
      }

      return this.pages.get(this.page).mouseClicked(mx, my, event.method_74245()) ? true : super.method_25402(this.scaled(event), doubleClick);
   }

   public boolean method_25406(class_11909 event) {
      return this.pages.get(this.page).mouseReleased(this.mx(event.comp_4798()), this.my(event.comp_4799()), event.method_74245())
         ? true
         : super.method_25406(this.scaled(event));
   }

   public boolean method_25403(class_11909 event, double dx, double dy) {
      return this.pages.get(this.page).mouseDragged(this.mx(event.comp_4798()), this.my(event.comp_4799()), event.method_74245())
         ? true
         : super.method_25403(this.scaled(event), dx / this.ui, dy / this.ui);
   }

   public boolean method_25401(double mx, double my, double scrollX, double scrollY) {
      return this.pages.get(this.page).mouseScrolled(this.mx(mx), this.my(my), scrollY) ? true : super.method_25401(this.mx(mx), this.my(my), scrollX, scrollY);
   }

   public boolean method_25404(class_11908 event) {
      if (this.pages.get(this.page).keyPressed(event)) {
         return true;
      } else if (ValenceClient.OPEN_MENU != null && ValenceClient.OPEN_MENU.method_1417(event)) {
         this.method_25419();
         return true;
      } else {
         return super.method_25404(event);
      }
   }

   public boolean method_25400(class_11905 event) {
      return this.pages.get(this.page).charTyped(event) ? true : super.method_25400(event);
   }
}
