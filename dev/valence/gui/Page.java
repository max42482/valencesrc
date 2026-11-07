package dev.valence.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;

@Environment(EnvType.CLIENT)
public abstract class Page {
   protected ValenceScreen screen;
   protected int x;
   protected int y;
   protected int w;
   protected int h;

   public void init(ValenceScreen screen, int x, int y, int w, int h) {
      this.screen = screen;
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
   }

   public abstract String nameKey();

   public abstract String subtitleKey();

   public abstract String icon();

   public abstract void render(class_332 var1, int var2, int var3, float var4);

   public boolean mouseClicked(double mx, double my, int button) {
      return false;
   }

   public boolean mouseReleased(double mx, double my, int button) {
      return false;
   }

   public boolean mouseDragged(double mx, double my, int button) {
      return false;
   }

   public boolean mouseScrolled(double mx, double my, double amount) {
      return false;
   }

   public boolean keyPressed(class_11908 event) {
      return false;
   }

   public boolean charTyped(class_11905 event) {
      return false;
   }

   public void tick() {
   }

   protected class_327 font() {
      return class_310.method_1551().field_1772;
   }

   protected boolean inside(double mx, double my, int x1, int y1, int x2, int y2) {
      return mx >= x1 && mx < x2 && my >= y1 && my < y2;
   }

   protected static void click() {
      UiSounds.click();
   }
}
