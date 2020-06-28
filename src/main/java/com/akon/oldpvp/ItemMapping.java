package com.akon.oldpvp;

import com.akon.oldpvp.utils.ReflectionUtil;
import com.google.common.collect.Maps;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemMapping {

	private static Object itemRegistry = null;
	private static final List<String> TOOL_ID = Arrays.asList("iron_sword", "wooden_sword", "stone_sword", "diamond_sword", "golden_sword", "iron_shovel", "iron_pickaxe", "iron_axe", "wooden_shovel", "wooden_pickaxe", "wooden_axe", "stone_shovel", "stone_pickaxe", "stone_axe", "diamond_shovel", "diamond_pickaxe", "diamond_axe", "golden_shovel", "golden_pickaxe", "golden_axe");
	private static final List<String> DIAMOND_ARMOR_PIECES = Arrays.asList("diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots");
	private static final HashMap<String, Float> OLD_TOOL_DAMAGE_MAP = Maps.newHashMap();
	private static final HashMap<String, Float> NEW_TOOL_DAMAGE_MAP = Maps.newHashMap();
	private static final HashMap<Material, String> MATERIAL_TO_TOOL_ID = Maps.newHashMap();

	static {
		NEW_TOOL_DAMAGE_MAP.put("iron_sword", 5.0F);      OLD_TOOL_DAMAGE_MAP.put("iron_sword", 6.0F);
		NEW_TOOL_DAMAGE_MAP.put("wooden_sword", 3.0F);    OLD_TOOL_DAMAGE_MAP.put("wooden_sword", 4.0F);
		NEW_TOOL_DAMAGE_MAP.put("stone_sword", 4.0F);     OLD_TOOL_DAMAGE_MAP.put("stone_sword", 5.0F);
		NEW_TOOL_DAMAGE_MAP.put("diamond_sword", 6.0F);   OLD_TOOL_DAMAGE_MAP.put("diamond_sword", 7.0F);
		NEW_TOOL_DAMAGE_MAP.put("golden_sword", 3.0F);    OLD_TOOL_DAMAGE_MAP.put("golden_sword", 4.0F);
		NEW_TOOL_DAMAGE_MAP.put("iron_shovel", 2.0F);     OLD_TOOL_DAMAGE_MAP.put("iron_shovel", 3.0F);
		NEW_TOOL_DAMAGE_MAP.put("iron_pickaxe", 3.0F);    OLD_TOOL_DAMAGE_MAP.put("iron_pickaxe", 4.0F);
		NEW_TOOL_DAMAGE_MAP.put("iron_axe", 8.0F);        OLD_TOOL_DAMAGE_MAP.put("iron_axe", 5.0F);
		NEW_TOOL_DAMAGE_MAP.put("wooden_shovel", 0.0F);   OLD_TOOL_DAMAGE_MAP.put("wooden_shovel", 1.0F);
		NEW_TOOL_DAMAGE_MAP.put("wooden_pickaxe", 1.0F);  OLD_TOOL_DAMAGE_MAP.put("wooden_pickaxe", 2.0F);
		NEW_TOOL_DAMAGE_MAP.put("wooden_axe", 6.0F);      OLD_TOOL_DAMAGE_MAP.put("wooden_axe", 3.0F);
		NEW_TOOL_DAMAGE_MAP.put("stone_shovel", 1.0F);    OLD_TOOL_DAMAGE_MAP.put("stone_shovel", 2.0F);
		NEW_TOOL_DAMAGE_MAP.put("stone_pickaxe", 2.0F);   OLD_TOOL_DAMAGE_MAP.put("stone_pickaxe", 3.0F);
		NEW_TOOL_DAMAGE_MAP.put("stone_axe", 8.0F);       OLD_TOOL_DAMAGE_MAP.put("stone_axe", 4.0F);
		NEW_TOOL_DAMAGE_MAP.put("diamond_shovel", 3.0F);  OLD_TOOL_DAMAGE_MAP.put("diamond_shovel", 4.0F);
		NEW_TOOL_DAMAGE_MAP.put("diamond_pickaxe", 4.0F); OLD_TOOL_DAMAGE_MAP.put("diamond_pickaxe", 5.0F);
		NEW_TOOL_DAMAGE_MAP.put("diamond_axe", 8.0F);     OLD_TOOL_DAMAGE_MAP.put("diamond_axe", 6.0F);
		NEW_TOOL_DAMAGE_MAP.put("golden_shovel", 0.0F);   OLD_TOOL_DAMAGE_MAP.put("golden_shovel", 1.0F);
		NEW_TOOL_DAMAGE_MAP.put("golden_pickaxe", 1.0F);  OLD_TOOL_DAMAGE_MAP.put("golden_pickaxe", 2.0F);
		NEW_TOOL_DAMAGE_MAP.put("golden_axe", 6.0F);      OLD_TOOL_DAMAGE_MAP.put("golden_axe", 3.0F);

		MATERIAL_TO_TOOL_ID.put(Material.IRON_SWORD, "iron_sword");
		MATERIAL_TO_TOOL_ID.put(Material.WOOD_SWORD, "wooden_sword");
		MATERIAL_TO_TOOL_ID.put(Material.STONE_SWORD, "stone_sword");
		MATERIAL_TO_TOOL_ID.put(Material.DIAMOND_SWORD, "diamond_sword");
		MATERIAL_TO_TOOL_ID.put(Material.GOLD_SWORD, "golden_sword");
		MATERIAL_TO_TOOL_ID.put(Material.IRON_SPADE, "iron_shovel");
		MATERIAL_TO_TOOL_ID.put(Material.IRON_PICKAXE, "iron_pickaxe");
		MATERIAL_TO_TOOL_ID.put(Material.IRON_AXE, "iron_axe");
		MATERIAL_TO_TOOL_ID.put(Material.WOOD_SPADE, "wooden_shovel");
		MATERIAL_TO_TOOL_ID.put(Material.WOOD_PICKAXE, "wooden_pickaxe");
		MATERIAL_TO_TOOL_ID.put(Material.WOOD_AXE, "wooden_axe");
		MATERIAL_TO_TOOL_ID.put(Material.STONE_SPADE, "stone_shovel");
		MATERIAL_TO_TOOL_ID.put(Material.STONE_PICKAXE, "stone_pickaxe");
		MATERIAL_TO_TOOL_ID.put(Material.STONE_AXE, "stone_axe");
		MATERIAL_TO_TOOL_ID.put(Material.DIAMOND_SPADE, "diamond_shovel");
		MATERIAL_TO_TOOL_ID.put(Material.DIAMOND_PICKAXE, "diamond_pickaxe");
		MATERIAL_TO_TOOL_ID.put(Material.DIAMOND_AXE, "diamond_axe");
		MATERIAL_TO_TOOL_ID.put(Material.GOLD_SPADE, "golden_shovel");
		MATERIAL_TO_TOOL_ID.put(Material.GOLD_PICKAXE, "golden_pickaxe");
		MATERIAL_TO_TOOL_ID.put(Material.GOLD_AXE, "golden_axe");
		try {
			itemRegistry = ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("Item"), "REGISTRY");
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
	}

	public static Object getItem(String id) {
		try {
			return ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("RegistryMaterials"), itemRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{id})});
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static float getOldDamage(String id) {
		return OLD_TOOL_DAMAGE_MAP.get(id);
	}

	public static float getNewDamage(String id) {
		return NEW_TOOL_DAMAGE_MAP.get(id);
	}

	public static String getToolIdFromMaterial(Material material) {
		return MATERIAL_TO_TOOL_ID.get(material);
	}

	public static List<String> getAllToolIds() {
		return TOOL_ID;
	}

	public static List<String> getAllDiamondPieceIds() {
		return DIAMOND_ARMOR_PIECES;
	}

}
