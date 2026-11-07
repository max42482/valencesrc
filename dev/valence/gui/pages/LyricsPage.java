package dev.valence.gui.pages;

import dev.valence.config.ValenceConfig;
import dev.valence.cord.NeonLyrics;
import dev.valence.cord.config.Config;
import dev.valence.cord.hud.Island;
import dev.valence.cord.lyrics.Favorites;
import dev.valence.cord.lyrics.LocalMedia;
import dev.valence.cord.lyrics.SpotifyClient;
import dev.valence.cord.sky.WorldSky;
import dev.valence.gui.Anim;
import dev.valence.gui.NowPlaying;
import dev.valence.gui.OptionList;
import dev.valence.gui.Page;
import dev.valence.gui.Theme;
import dev.valence.gui.UiSounds;
import dev.valence.gui.ValenceScreen;
import dev.valence.lyrics.LyricFonts;
import dev.valence.lyrics.LyricStyles;
import dev.valence.lyrics.SkyLyrics;
import java.util.List;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;

@Environment(EnvType.CLIENT)
public class LyricsPage extends Page {
   private static final String[] LANGS = new String[]{"de", "en", "es", "fr", "tr", "pl", "it", "ru"};
   private final OptionList options = new OptionList();
   private final NowPlaying nowPlaying = new NowPlaying(62);

   @Override
   public String nameKey() {
      return "valence.page.lyrics";
   }

   @Override
   public String subtitleKey() {
      return "valence.page.lyrics.subtitle";
   }

   @Override
   public String icon() {
      return "music";
   }

   private static class_2561 t(String key, Object... args) {
      return class_2561.method_43469("neonlyrics." + key, args);
   }

   private static Config c() {
      return NeonLyrics.cfg();
   }

   @Override
   public void init(ValenceScreen screen, int x, int y, int w, int h) {
      super.init(screen, x, y, w, h);
      this.options.setBounds(x, y + 58, w, h - 58);
      this.options.clear();
      Config c = c();
      final ValenceConfig v = ValenceConfig.get();
      this.options.add(new OptionList.Header(class_2561.method_43471("valence.lyrics.section.general")));
      this.options
         .add(
            new OptionList.Toggle(
               class_2561.method_43471("valence.lyrics.enabled"), class_2561.method_43471("valence.lyrics.enabled.desc"), () -> v.lyricsEnabled, val -> {
                  v.lyricsEnabled = val;
                  SkyLyrics.clear();
               }
            )
         );
      this.options
         .add(
            new OptionList.Cycle(
                  class_2561.method_43471("valence.lyrics.mode"),
                  List.of("sky", "world", "auto", "screen", "bar"),
                  List.of(
                     class_2561.method_43471("valence.lyrics.mode.sky"),
                     class_2561.method_43471("valence.lyrics.mode.world"),
                     class_2561.method_43471("valence.lyrics.mode.auto"),
                     class_2561.method_43471("valence.lyrics.mode.screen"),
                     class_2561.method_43471("valence.lyrics.mode.bar")
                  ),
                  () -> v.lyricsMode,
                  val -> {
                     v.lyricsMode = val;
                     SkyLyrics.clear();
                  }
               )
               .arrows()
         );
      this.options.add(new OptionList.Info(() -> class_2561.method_43471("valence.lyrics.mode.desc." + v.lyricsMode), 2));
      this.options
         .add(
            new OptionList.Cycle(
                  class_2561.method_43471("valence.lyrics.animation"),
                  List.of("word", "line", "single"),
                  List.of(
                     class_2561.method_43471("valence.lyrics.animation.word"),
                     class_2561.method_43471("valence.lyrics.animation.line"),
                     class_2561.method_43471("valence.lyrics.animation.single")
                  ),
                  () -> v.lyricsAnimation,
                  val -> {
                     v.lyricsAnimation = val;
                     SkyLyrics.clear();
                  }
               )
               .arrows()
         );
      this.options
         .add(
            new OptionList.Toggle(
               class_2561.method_43471("valence.lyrics.topbar"),
               class_2561.method_43471("valence.lyrics.topbar.desc"),
               () -> c.lyricsIsland,
               val -> c.lyricsIsland = val
            )
         );
      this.options.add(new OptionList.Button(class_2561.method_43471("valence.lyrics.demo"), class_2561.method_43471("valence.lyrics.demo.button"), () -> {
         SkyLyrics.showDemo(class_2561.method_43471("valence.lyrics.demo.text").getString());
         class_310.method_1551().method_1507(null);
      }));
      this.options.add(new OptionList.Header(class_2561.method_43471("valence.lyrics.section.look")));
      this.options.add(new LyricsPage.PresetPicker());
      this.options.add(new LyricsPage.FontPicker());
      this.options
         .add(
            new OptionList.Cycle(
               class_2561.method_43471("valence.lyrics.colormode"),
               List.of("gradient", "solid", "rainbow"),
               List.of(
                  class_2561.method_43471("valence.lyrics.colormode.gradient"),
                  class_2561.method_43471("valence.lyrics.colormode.solid"),
                  class_2561.method_43471("valence.lyrics.colormode.rainbow")
               ),
               () -> v.lyricsColorMode,
               val -> v.lyricsColorMode = val
            )
         );
      this.options.add(new OptionList.Colors(class_2561.method_43471("valence.lyrics.color1"), () -> v.lyricsColorA, val -> {
         v.lyricsColorA = val;
         v.lyricsPreset = "";
      }));
      this.options.add(new OptionList.Colors(class_2561.method_43471("valence.lyrics.color2"), () -> v.lyricsColorB, val -> {
         v.lyricsColorB = val;
         v.lyricsPreset = "";
      }) {
         @Override
         public boolean visible() {
            return "gradient".equals(v.lyricsColorMode);
         }
      });
      this.options
         .add(
            new OptionList.Slider(
               class_2561.method_43471("valence.lyrics.glow"),
               0.0,
               1.0,
               0.05,
               () -> (double)v.lyricsGlow,
               val -> v.lyricsGlow = val.floatValue(),
               val -> Math.round(val * 100.0) + "%"
            )
         );
      this.options
         .add(
            new OptionList.Slider(
               class_2561.method_43471("valence.lyrics.size"),
               0.5,
               2.0,
               0.05,
               () -> (double)v.lyricsScale,
               val -> v.lyricsScale = val.floatValue(),
               val -> String.format(Locale.ROOT, "%.2fx", val)
            )
         );
      this.options.add(new OptionList.Expander(class_2561.method_43471("valence.lyrics.section.advanced")));
      this.options.add(new OptionList.Toggle(class_2561.method_43471("valence.lyrics.outline"), null, () -> v.lyricsOutline, val -> v.lyricsOutline = val));
      this.options
         .add(new OptionList.Colors(class_2561.method_43471("valence.lyrics.outlinecolor"), () -> v.lyricsOutlineColor, val -> v.lyricsOutlineColor = val) {
            @Override
            public boolean visible() {
               return v.lyricsOutline;
            }
         });
      this.options
         .add(new OptionList.Toggle(class_2561.method_43471("valence.lyrics.uppercase"), null, () -> v.lyricsUppercase, val -> v.lyricsUppercase = val));
      this.options
         .add(
            new OptionList.Slider(
               class_2561.method_43471("valence.lyrics.tilt"),
               0.0,
               1.0,
               0.05,
               () -> (double)v.lyricsTilt,
               val -> v.lyricsTilt = val.floatValue(),
               val -> Math.round(val * 100.0) + "%"
            )
         );
      this.options
         .add(
            new OptionList.Slider(
               class_2561.method_43471("valence.lyrics.height"),
               5.0,
               60.0,
               1.0,
               () -> (double)v.lyricsElevation,
               val -> v.lyricsElevation = val.floatValue(),
               val -> Math.round(val) + "°"
            )
         );
      this.options
         .add(
            new OptionList.Slider(
               class_2561.method_43471("valence.lyrics.hold"),
               0.5,
               6.0,
               0.5,
               () -> (double)v.lyricsHoldSeconds,
               val -> v.lyricsHoldSeconds = val.floatValue(),
               val -> String.format(Locale.ROOT, "%.1fs", val)
            )
         );
      this.options
         .add(
            new OptionList.Toggle(
               class_2561.method_43471("valence.lyrics.title"),
               class_2561.method_43471("valence.lyrics.title.desc"),
               () -> v.lyricsShowTitleWithoutLyrics,
               val -> v.lyricsShowTitleWithoutLyrics = val
            )
         );
      this.options
         .add(
            new OptionList.Buttons(
               t("opt.lyricsoffset"),
               () -> class_2561.method_43470((c.lyricsOffsetMs >= 0 ? "+" : "") + String.format(Locale.ROOT, "%.1fs", c.lyricsOffsetMs / 1000.0F)),
               List.of(class_2561.method_43470("-0.1"), class_2561.method_43470("0"), class_2561.method_43470("+0.1")),
               List.of(() -> c.lyricsOffsetMs -= 100, () -> c.lyricsOffsetMs = 0, () -> c.lyricsOffsetMs += 100)
            )
         );
      this.options.add(new OptionList.Button(t("ui.spotify.sync"), class_2561.method_43471("valence.lyrics.sync.button"), SpotifyClient::syncHere));
      this.options.add(new OptionList.Info(LyricsPage::statusText, 2));
      this.options
         .add(
            new OptionList.Segmented(
               t("ui.spotify.mode"), null, List.of(t("spotify.mode.auto"), t("spotify.mode.api")), () -> c.spotifyMode, SpotifyClient::setMode
            )
         );
      this.options.add(autoOnly(new OptionList.Button(t("spotify.auto.retry"), t("spotify.auto.retry"), () -> {
         LocalMedia.restart();
         Island.notify(class_2561.method_43471("neonlyrics.spotify.auto.retrying").getString(), true);
      })));
      this.options
         .add(apiOnly(new OptionList.TextField(t("spotify.clientid"), t("hint.clientid"), 64, () -> c.spotifyClientId, val -> c.spotifyClientId = val.trim())));
      this.options
         .add(
            apiOnly(
               new OptionList.Button(
                  class_2561.method_43471("valence.lyrics.spotify.login"),
                  () -> class_2561.method_43471(
                     SpotifyClient.status() == SpotifyClient.Status.CONNECTED ? "neonlyrics.spotify.disconnect" : "neonlyrics.spotify.connect"
                  ),
                  () -> {
                     if (SpotifyClient.status() == SpotifyClient.Status.CONNECTED) {
                        SpotifyClient.logout();
                     } else {
                        NeonLyrics.saveConfig();
                        SpotifyClient.login();
                     }
                  }
               )
            )
         );
      this.options
         .add(
            apiOnly(
               new OptionList.Info(
                  () -> class_2561.method_43470(
                     "1. "
                        + t("spotify.step1").getString()
                        + "\n2. "
                        + t("spotify.step2").getString()
                        + " http://127.0.0.1:8789/callback\n3. "
                        + t("spotify.step3").getString()
                  ),
                  4
               )
            )
         );
      this.options
         .add(
            apiOnly(
               new OptionList.Button(
                  t("spotify.dashboard"), t("spotify.dashboard"), () -> class_156.method_668().method_670("https://developer.spotify.com/dashboard")
               )
            )
         );
      this.options.add(new OptionList.Toggle(t("ui.spotify.demo"), t("ui.spotify.demo.sub"), () -> c.demoLyrics, val -> {
         c.demoLyrics = val;
         if (val) {
            SpotifyClient.restartDemo();
         }
      }));
      this.options.add(new OptionList.Expander(t("ui.fav.lines")));
      this.options.add(new LyricsPage.FavList());
      this.options.add(new LyricsPage.HistoryList());
   }

   private static int colorOf(int index) {
      return index < 0 ? -1 : WorldSky.LYRIC_COLORS[Math.floorMod(index, WorldSky.LYRIC_COLORS.length)];
   }

   private static int indexOf(int rgb) {
      for (int i = 0; i < WorldSky.LYRIC_COLORS.length; i++) {
         if (WorldSky.LYRIC_COLORS[i] == rgb) {
            return i;
         }
      }

      return -1;
   }

   private static class_2561 statusText() {
      Config c = c();
      dev.valence.cord.lyrics.NowPlaying np = SpotifyClient.now();
      String st;
      if (c.demoLyrics) {
         st = t("spotify.st.demo").getString();
      } else if (SpotifyClient.autoMode()) {
         st = switch (LocalMedia.status()) {
            case UNSUPPORTED -> t("spotify.auto.unsupported").getString();
            case STARTING -> t("spotify.auto.starting").getString();
            case NO_PLAYER -> t("spotify.auto.noplayer").getString();
            case PLAYING -> t("spotify.auto.on", LocalMedia.app()).getString();
            case FAILED -> t("spotify.auto.failed").getString();
         };
      } else {
         st = switch (SpotifyClient.status()) {
            case NOT_CONFIGURED -> t("spotify.st.setup").getString();
            case DISCONNECTED -> t("spotify.st.off").getString();
            case WAITING_LOGIN -> t("spotify.st.wait").getString();
            case CONNECTED -> t("spotify.st.on").getString();
            case ERROR -> t("spotify.st.error", SpotifyClient.lastError()).getString();
         };
      }

      String song = np.hasTrack() ? np.title + (np.artist.isBlank() ? "" : " · " + np.artist) : t("spotify.nothing").getString();
      return class_2561.method_43470(st + "\n" + song);
   }

   private static OptionList.Row autoOnly(OptionList.Row row) {
      return only(row, true);
   }

   private static OptionList.Row apiOnly(OptionList.Row row) {
      return only(row, false);
   }

   private static OptionList.Row only(OptionList.Row row, boolean auto) {
      return new OptionList.Row() {
         @Override
         public int height() {
            return row.height();
         }

         @Override
         public boolean visible() {
            return SpotifyClient.autoMode() == auto && row.visible();
         }

         @Override
         public void render(class_332 g, int x, int y, int w, int mx, int my) {
            row.render(g, x, y, w, mx, my);
         }

         @Override
         public boolean click(double mx, double my, int x, int y, int w, int button) {
            return row.click(mx, my, x, y, w, button);
         }

         @Override
         public OptionList.Row inner() {
            return row;
         }
      };
   }

   @Override
   public void render(class_332 g, int mx, int my, float pt) {
      this.nowPlaying.render(g, this.x, this.y, this.w, mx, my);
      this.options.render(g, mx, my);
   }

   @Override
   public boolean mouseClicked(double mx, double my, int button) {
      return this.nowPlaying.click(mx, my, this.x, this.y, this.w) ? true : this.options.mouseClicked(mx, my, button);
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

   @Override
   public boolean keyPressed(class_11908 event) {
      return this.options.keyPressed(event);
   }

   @Override
   public boolean charTyped(class_11905 event) {
      return this.options.charTyped(event);
   }

   @Environment(EnvType.CLIENT)
   private static class FavList extends OptionList.Row {
      private static final int ROW = 30;

      @Override
      public int height() {
         return Math.max(1, Favorites.all().size()) * 33 + 4;
      }

      @Override
      public void render(class_332 g, int x, int y, int w, int mx, int my) {
         class_327 f = font();
         List<Favorites.Entry> list = Favorites.all();
         if (list.isEmpty()) {
            Theme.card(g, x, y + 1, x + w, y + 30, 0.0F, false);
            g.method_51433(f, f.method_27523(LyricsPage.t("fav.empty").getString(), w - 16), x + 8, y + 11, -8095322, false);
         } else {
            for (int i = 0; i < list.size(); i++) {
               Favorites.Entry e = list.get(i);
               int ry = y + 1 + i * 33;
               boolean hovered = inside(mx, my, x, ry, x + w, ry + 30);
               float hv = Anim.hover(this.key(1 + i), hovered);
               Theme.card(g, x, ry, x + w, ry + 30, hv, false);
               g.method_51433(f, f.method_27523("„" + e.line + "“", w - 34), x + 8, ry + 6, -724737, false);
               g.method_51448().pushMatrix();
               g.method_51448().translate(x + 8, ry + 18);
               g.method_51448().scale(0.75F, 0.75F);
               g.method_51433(f, f.method_27523(e.title + (e.artist.isBlank() ? "" : " · " + e.artist), Math.round((w - 40) / 0.75F)), 0, 0, -8095322, false);
               g.method_51448().popMatrix();
               int bx = x + w - 22;
               boolean bh = inside(mx, my, bx, ry + 7, bx + 16, ry + 23);
               Theme.pill(g, bx, ry + 7, bx + 16, ry + 23, bh ? 1442070366 : 587202559);
               Theme.icon(g, "close", bx + 4, ry + 11, 8, bh ? -1 : -4608298);
            }
         }
      }

      @Override
      public boolean click(double mx, double my, int x, int y, int w, int button) {
         List<Favorites.Entry> list = Favorites.all();

         for (int i = 0; i < list.size(); i++) {
            int ry = y + 1 + i * 33;
            int bx = x + w - 22;
            if (mx >= bx && mx < bx + 16 && my >= ry + 7 && my < ry + 23) {
               Favorites.remove(list.get(i));
               UiSounds.toggle(false);
               return false;
            }

            if (my >= ry && my < ry + 30) {
               class_310.method_1551().field_1774.method_1455(list.get(i).line);
               Island.notify(class_2561.method_43471("valence.copied").getString(), true);
               UiSounds.click();
               return false;
            }
         }

         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   private static class FontPicker extends OptionList.Row {
      private static final int CHIP_H = 22;
      private static final int COLS = 5;

      @Override
      public int height() {
         int rows = (LyricFonts.ALL.size() + 5 - 1) / 5;
         return 14 + rows * 26 + 2;
      }

      @Override
      public void render(class_332 g, int x, int y, int w, int mx, int my) {
         class_327 f = font();
         g.method_51448().pushMatrix();
         g.method_51448().translate(x + 4, y + 3);
         g.method_51448().scale(0.85F, 0.85F);
         g.method_51439(f, class_2561.method_43471("valence.lyrics.font"), 0, 0, -4608298, false);
         g.method_51448().popMatrix();
         int cw = (w - 16) / 5;
         String current = ValenceConfig.get().lyricsFont;

         for (int i = 0; i < LyricFonts.ALL.size(); i++) {
            LyricFonts.LyricFont lf = LyricFonts.ALL.get(i);
            int cx = x + i % 5 * (cw + 4);
            int cy = y + 14 + i / 5 * 26;
            boolean sel = lf.id().equals(current);
            float hv = Anim.hover(this.key(i), inside(mx, my, cx, cy, cx + cw, cy + 22));
            Theme.card(g, cx, cy, cx + cw, cy + 22, hv, sel);
            class_2583 st = class_2583.field_24360.method_27704(lf.description());
            String plain = f.method_27523(class_2561.method_43470(lf.displayName()).method_27696(st).getString(), cw - 6);
            g.method_27534(f, class_2561.method_43470(plain).method_27696(st), cx + cw / 2, cy + 7, sel ? -1 : Theme.lerp(-4608298, -1, hv));
         }
      }

      @Override
      public boolean click(double mx, double my, int x, int y, int w, int button) {
         int cw = (w - 16) / 5;

         for (int i = 0; i < LyricFonts.ALL.size(); i++) {
            int cx = x + i % 5 * (cw + 4);
            int cy = y + 14 + i / 5 * 26;
            if (mx >= cx && mx < cx + cw && my >= cy && my < cy + 22) {
               ValenceConfig.get().lyricsFont = LyricFonts.ALL.get(i).id();
               ValenceConfig.get().lyricsPreset = "";
               sound();
               save();
               return false;
            }
         }

         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   private static class HistoryList extends OptionList.Row {
      private static final int ROW = 16;

      @Override
      public int height() {
         return Math.max(1, Math.min(8, SpotifyClient.history().size())) * 16 + 10;
      }

      @Override
      public void render(class_332 g, int x, int y, int w, int mx, int my) {
         class_327 f = font();
         List<SpotifyClient.Played> hist = SpotifyClient.history();
         Theme.card(g, x, y + 1, x + w, y + this.height() - 2, 0.0F, false);
         if (hist.isEmpty()) {
            g.method_51433(f, f.method_27523(LyricsPage.t("ui.fav.history.empty").getString(), w - 16), x + 8, y + 8, -8095322, false);
         } else {
            long now = System.currentTimeMillis();

            for (int i = 0; i < Math.min(8, hist.size()); i++) {
               SpotifyClient.Played p = hist.get(i);
               int ry = y + 5 + i * 16;
               String ago = Math.max(1L, (now - p.at()) / 60000L) + " min";
               int aw = f.method_1727(ago);
               g.method_51433(
                  f,
                  f.method_27523(p.title() + (p.artist().isBlank() ? "" : " · " + p.artist()), w - aw - 26),
                  x + 8,
                  ry + 3,
                  i == 0 ? -724737 : -4608298,
                  false
               );
               g.method_51433(f, ago, x + w - aw - 8, ry + 3, -8095322, false);
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   private static class PresetPicker extends OptionList.Row {
      private static final int CHIP_H = 24;
      private static final int COLS = 3;

      @Override
      public int height() {
         int rows = (LyricStyles.ALL.size() + 3 - 1) / 3;
         return rows * 28 + 4;
      }

      @Override
      public void render(class_332 g, int x, int y, int w, int mx, int my) {
         class_327 f = font();
         int cw = (w - 8) / 3;
         String current = ValenceConfig.get().lyricsPreset;

         for (int i = 0; i < LyricStyles.ALL.size(); i++) {
            LyricStyles.Preset p = LyricStyles.ALL.get(i);
            int cx = x + i % 3 * (cw + 4);
            int cy = y + 2 + i / 3 * 28;
            boolean sel = p.id().equals(current);
            float hv = Anim.hover(this.key(i), inside(mx, my, cx, cy, cx + cw, cy + 24));
            Theme.card(g, cx, cy, cx + cw, cy + 24, hv, sel);
            class_2583 st = class_2583.field_24360.method_27704(LyricFonts.byId(p.font()).description()).method_36139(p.colorA() & 16777215);
            String name = class_2561.method_43471("valence.lyrics.preset." + p.id()).getString();
            if (p.uppercase()) {
               name = name.toUpperCase(Locale.ROOT);
            }

            class_2561 label = class_2561.method_43470(name).method_27696(st);
            g.method_51448().pushMatrix();
            g.method_51448().translate(cx + cw / 2.0F, cy + 12.0F - 4.0F - hv);
            float sc = 1.1F + 0.06F * hv;
            g.method_51448().scale(sc, sc);
            g.method_27534(f, label, 0, 0, -1);
            g.method_51448().popMatrix();
            Theme.pill(g, cx + cw / 2 - 12, cy + 24 - 5, cx + cw / 2, cy + 24 - 3, p.colorA());
            Theme.pill(g, cx + cw / 2, cy + 24 - 5, cx + cw / 2 + 12, cy + 24 - 3, p.colorB());
         }
      }

      @Override
      public boolean click(double mx, double my, int x, int y, int w, int button) {
         int cw = (w - 8) / 3;

         for (int i = 0; i < LyricStyles.ALL.size(); i++) {
            int cx = x + i % 3 * (cw + 4);
            int cy = y + 2 + i / 3 * 28;
            if (mx >= cx && mx < cx + cw && my >= cy && my < cy + 24) {
               LyricStyles.ALL.get(i).apply();
               sound();
               return false;
            }
         }

         return false;
      }
   }
}
