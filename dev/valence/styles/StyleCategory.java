package dev.valence.styles;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1792;
import net.minecraft.class_1802;

@Environment(EnvType.CLIENT)
public record StyleCategory(String id, class_1792 icon, String preview, List<String> textures) {
   public static final List<StyleCategory> ALL = List.of(
      new StyleCategory("crystal", class_1802.field_8301, "crystal", List.of("textures/entity/end_crystal/end_crystal.png")),
      new StyleCategory("obsidian", class_1802.field_8281, "cube", List.of("textures/block/obsidian.png", "textures/block/crying_obsidian.png")),
      new StyleCategory(
         "anchor",
         class_1802.field_23141,
         "anchor",
         List.of(
            "textures/block/respawn_anchor_side4.png",
            "textures/block/respawn_anchor_top.png",
            "textures/block/respawn_anchor_top_off.png",
            "textures/block/respawn_anchor_bottom.png",
            "textures/block/respawn_anchor_side0.png",
            "textures/block/respawn_anchor_side1.png",
            "textures/block/respawn_anchor_side2.png",
            "textures/block/respawn_anchor_side3.png"
         )
      ),
      new StyleCategory(
         "armor",
         class_1802.field_22028,
         "armor",
         List.of(
            "textures/entity/equipment/humanoid/netherite.png",
            "textures/entity/equipment/humanoid_leggings/netherite.png",
            "textures/entity/equipment/humanoid/diamond.png",
            "textures/entity/equipment/humanoid_leggings/diamond.png",
            "textures/item/netherite_helmet.png",
            "textures/item/netherite_chestplate.png",
            "textures/item/netherite_leggings.png",
            "textures/item/netherite_boots.png",
            "textures/item/diamond_helmet.png",
            "textures/item/diamond_chestplate.png",
            "textures/item/diamond_leggings.png",
            "textures/item/diamond_boots.png"
         )
      ),
      new StyleCategory("swords", class_1802.field_22022, "item", List.of("textures/item/netherite_sword.png", "textures/item/diamond_sword.png")),
      new StyleCategory(
         "tools",
         class_1802.field_22024,
         "item",
         List.of(
            "textures/item/netherite_pickaxe.png",
            "textures/item/diamond_pickaxe.png",
            "textures/item/netherite_axe.png",
            "textures/item/diamond_axe.png",
            "textures/item/netherite_shovel.png",
            "textures/item/diamond_shovel.png"
         )
      ),
      new StyleCategory("mace", class_1802.field_49814, "item", List.of("textures/item/mace.png")),
      new StyleCategory("gapples", class_1802.field_8463, "item", List.of("textures/item/golden_apple.png")),
      new StyleCategory("pearls", class_1802.field_8634, "item", List.of("textures/item/ender_pearl.png")),
      new StyleCategory("totem", class_1802.field_8288, "item", List.of("textures/item/totem_of_undying.png")),
      new StyleCategory("xp", class_1802.field_8287, "item", List.of("textures/item/experience_bottle.png")),
      new StyleCategory("shield", class_1802.field_8255, "shield", List.of("textures/entity/shield_base_nopattern.png", "textures/entity/shield_base.png")),
      new StyleCategory("cobweb", class_1802.field_8786, "cross", List.of("textures/block/cobweb.png"))
   );

   public String nameKey() {
      return "valence.styles.category." + this.id;
   }

   public static StyleCategory byId(String id) {
      for (StyleCategory c : ALL) {
         if (c.id.equals(id)) {
            return c;
         }
      }

      return null;
   }
}
