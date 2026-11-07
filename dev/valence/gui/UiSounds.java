package dev.valence.gui;

import dev.valence.config.ValenceConfig;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;

@Environment(EnvType.CLIENT)
public final class UiSounds {
   public static final List<String> PACKS = List.of("soft", "creamy", "glass");
   private static final UiSounds.Pack GLASS = new UiSounds.Pack(
      event("ui.hover"), event("ui.click"), event("ui.toggle_on"), event("ui.toggle_off"), event("ui.open")
   );
   private static final UiSounds.Pack SOFT = pack("soft");
   private static final UiSounds.Pack CREAMY = pack("creamy");
   private static long lastHover;

   private UiSounds() {
   }

   private static class_3414 event(String name) {
      return class_3414.method_47908(class_2960.method_60655("valence", name));
   }

   private static UiSounds.Pack pack(String id) {
      return new UiSounds.Pack(
         event("ui." + id + ".hover"),
         event("ui." + id + ".click"),
         event("ui." + id + ".toggle_on"),
         event("ui." + id + ".toggle_off"),
         event("ui." + id + ".open")
      );
   }

   private static UiSounds.Pack current() {
      return switch (ValenceConfig.get().uiSoundPack) {
         case "creamy" -> CREAMY;
         case "glass" -> GLASS;
         default -> SOFT;
      };
   }

   private static void play(class_3414 event, float pitch, float volume) {
      ValenceConfig cfg = ValenceConfig.get();
      if (cfg.uiSounds && !(cfg.uiSoundVolume <= 0.01F) && event != null) {
         class_310.method_1551().method_1483().method_4873(class_1109.method_4757(event, pitch, Math.min(1.0F, volume * cfg.uiSoundVolume * 2.0F)));
      }
   }

   public static void hover() {
      long now = System.currentTimeMillis();
      if (now - lastHover >= 45L) {
         lastHover = now;
         play(current().hover(), 0.97F + (float)Math.random() * 0.06F, 0.3F);
      }
   }

   public static void click() {
      play(current().click(), 0.97F + (float)Math.random() * 0.06F, 0.5F);
   }

   public static void toggle(boolean on) {
      play(on ? current().on() : current().off(), 1.0F, 0.45F);
   }

   public static void open() {
      play(current().open(), 1.0F, 0.35F);
   }

   public static void preview() {
      click();
   }

   @Environment(EnvType.CLIENT)
   private record Pack(class_3414 hover, class_3414 click, class_3414 on, class_3414 off, class_3414 open) {
   }
}
