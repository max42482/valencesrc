package dev.valence.gui.pages;

import dev.valence.config.ValenceConfig;
import dev.valence.cord.NeonLyrics;
import dev.valence.cord.config.Config;
import dev.valence.cord.hud.Gfx;
import dev.valence.cord.hud.Theme;
import dev.valence.gui.OptionList;
import dev.valence.gui.Page;
import dev.valence.gui.ValenceScreen;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_332;

@Environment(EnvType.CLIENT)
public class HudPage extends Page {
   private final OptionList options = new OptionList();

   @Override
   public String nameKey() {
      return "valence.page.hud";
   }

   @Override
   public String subtitleKey() {
      return "valence.page.hud.subtitle";
   }

   @Override
   public String icon() {
      return "display";
   }

   private static class_2561 t(String key, Object... args) {
      return class_2561.method_43469("neonlyrics." + key, args);
   }

   @Override
   public void init(ValenceScreen screen, int x, int y, int w, int h) {
      super.init(screen, x, y, w, h);
      this.options.setBounds(x, y, w, h);
      this.options.clear();
      Config c = NeonLyrics.cfg();
      final ValenceConfig vc = ValenceConfig.get();
      this.options.add(new OptionList.Header(t("ui.display.bar")));
      this.options.add(new OptionList.Toggle(t("ui.display.island"), t("ui.display.island.sub"), () -> c.islandEnabled, v -> c.islandEnabled = v));
      this.options.add(new OptionList.Toggle(t("opt.lyricsisland"), t("ui.display.lyricsisland.sub"), () -> c.lyricsIsland, v -> c.lyricsIsland = v));
      this.options.add(new OptionList.Toggle(t("extras.cover"), t("ui.display.cover.sub"), () -> c.albumCover, v -> c.albumCover = v));
      this.options.add(new OptionList.Toggle(t("opt.clock"), null, () -> c.showClock, v -> c.showClock = v));
      this.options.add(new OptionList.Toggle(t("opt.ping"), null, () -> c.showPing, v -> c.showPing = v));
      this.options.add(new OptionList.Toggle(t("opt.fps"), null, () -> c.showFps, v -> c.showFps = v));
      List<class_2561> fonts = new ArrayList<>();

      for (String n : Gfx.FONT_NAMES) {
         fonts.add(class_2561.method_43470(n));
      }

      this.options.add(OptionList.choice(t("ui.display.barfont"), fonts, () -> Math.min(c.uiFont, fonts.size() - 1), v -> {
         c.uiFont = v;
         Gfx.clearWidths();
      }, true));
      List<class_2561> accents = new ArrayList<>();

      for (String n : Theme.ACCENT_NAMES) {
         accents.add(class_2561.method_43470(n));
      }

      this.options
         .add(new OptionList.Colors(t("ui.display.accent"), Theme.ACCENTS, false, () -> Theme.ACCENTS[Math.floorMod(c.accent, Theme.ACCENTS.length)], v -> {
            for (int i = 0; i < Theme.ACCENTS.length; i++) {
               if (Theme.ACCENTS[i] == v) {
                  c.accent = i;
               }
            }
         }));
      this.options.add(new OptionList.Header(t("ui.display.hud")));
      this.options
         .add(
            new OptionList.Toggle(
               class_2561.method_43471("valence.settings.logo"), class_2561.method_43471("valence.settings.logo.desc"), () -> vc.hudLogo, v -> vc.hudLogo = v
            )
         );
      this.options
         .add(
            new OptionList.Slider(
               class_2561.method_43471("valence.settings.logo.size"),
               0.5,
               3.0,
               0.1,
               () -> (double)vc.hudLogoScale,
               v -> vc.hudLogoScale = v.floatValue(),
               v -> String.format("%.1fx", v)
            ) {
               @Override
               public boolean visible() {
                  return vc.hudLogo;
               }
            }
         );
      this.options.add(new OptionList.Toggle(t("extras.startup"), null, () -> c.fxStartup, v -> c.fxStartup = v));
   }

   @Override
   public void render(class_332 g, int mx, int my, float pt) {
      this.options.render(g, mx, my);
   }

   @Override
   public boolean mouseClicked(double mx, double my, int button) {
      return this.options.mouseClicked(mx, my, button);
   }

   @Override
   public boolean mouseDragged(double mx, double my, int button) {
      return this.options.mouseDragged(mx, my);
   }

   @Override
   public boolean mouseReleased(double mx, double my, int button) {
      return this.options.mouseReleased();
   }

   @Override
   public boolean mouseScrolled(double mx, double my, double amount) {
      return this.options.mouseScrolled(mx, my, amount);
   }
}
