package com.akon.oldpvp;

import com.akon.oldpvp.utils.ReflectionUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

//アップデートで1.8と比べて効果時間が変更されたポーション
public class PotionDurationMapping {

	private static final HashBasedTable<Boolean, String, Integer> OLD_POTION_DURATION_TABLE = HashBasedTable.create();
	private static final HashBasedTable<Boolean, String, Integer> NEW_POTION_DURATION_TABLE = HashBasedTable.create();

	static {
		//ReflectionでNMSのメソッドにアクセスして効果時間を変更をするためPotionDataではなくStringで管理する
		OLD_POTION_DURATION_TABLE.put(false, "long_regeneration", 2400);   NEW_POTION_DURATION_TABLE.put(false, "long_regeneration", 1800);
		OLD_POTION_DURATION_TABLE.put(true, "regeneration", 660);          NEW_POTION_DURATION_TABLE.put(true, "regeneration", 900);
		OLD_POTION_DURATION_TABLE.put(true, "strong_regeneration", 320);   NEW_POTION_DURATION_TABLE.put(true, "strong_regeneration", 440);
		OLD_POTION_DURATION_TABLE.put(true, "swiftness", 2700);            NEW_POTION_DURATION_TABLE.put(true, "swiftness", 3600);
		OLD_POTION_DURATION_TABLE.put(true, "long_swiftness", 7200);       NEW_POTION_DURATION_TABLE.put(true, "long_swiftness", 9600);
		OLD_POTION_DURATION_TABLE.put(true, "strong_swiftness", 1340);     NEW_POTION_DURATION_TABLE.put(true, "strong_swiftness", 1800);
		OLD_POTION_DURATION_TABLE.put(true, "fire_resistance", 2700);      NEW_POTION_DURATION_TABLE.put(true, "fire_resistance", 3600);
		OLD_POTION_DURATION_TABLE.put(true, "long_fire_resistance", 7200); NEW_POTION_DURATION_TABLE.put(true, "long_fire_resistance", 9600);
		OLD_POTION_DURATION_TABLE.put(false, "strong_poison", 440);        NEW_POTION_DURATION_TABLE.put(false, "strong_poison", 420);
		OLD_POTION_DURATION_TABLE.put(true, "poison", 660);                NEW_POTION_DURATION_TABLE.put(true, "poison", 900);
		OLD_POTION_DURATION_TABLE.put(true, "strong_poison", 320);         NEW_POTION_DURATION_TABLE.put(true, "strong_poison", 420);
		OLD_POTION_DURATION_TABLE.put(true, "night_vision", 2700);         NEW_POTION_DURATION_TABLE.put(true, "night_vision", 3600);
		OLD_POTION_DURATION_TABLE.put(true, "long_night_vision", 7200);    NEW_POTION_DURATION_TABLE.put(true, "long_night_vision", 9600);
		OLD_POTION_DURATION_TABLE.put(true, "weakness", 1340);             NEW_POTION_DURATION_TABLE.put(true, "weakness", 1800);
		OLD_POTION_DURATION_TABLE.put(true, "long_weakness", 3600);        NEW_POTION_DURATION_TABLE.put(true, "long_weakness", 4800);
		OLD_POTION_DURATION_TABLE.put(true, "strength", 2700);             NEW_POTION_DURATION_TABLE.put(true, "strength", 3600);
		OLD_POTION_DURATION_TABLE.put(true, "long_strength", 7200);        NEW_POTION_DURATION_TABLE.put(true, "long_strength", 9600);
		OLD_POTION_DURATION_TABLE.put(true, "strong_strength", 1340);      NEW_POTION_DURATION_TABLE.put(true, "strong_strength", 1800);
		OLD_POTION_DURATION_TABLE.put(true, "slowness", 1340);             NEW_POTION_DURATION_TABLE.put(true, "slowness", 1800);
		OLD_POTION_DURATION_TABLE.put(true, "long_slowness", 3600);        NEW_POTION_DURATION_TABLE.put(true, "long_slowness", 4800);
		OLD_POTION_DURATION_TABLE.put(true, "leaping", 2700);              NEW_POTION_DURATION_TABLE.put(true, "leaping", 3600);
		OLD_POTION_DURATION_TABLE.put(true, "long_leaping", 7200);         NEW_POTION_DURATION_TABLE.put(true, "long_leaping", 9600);
		OLD_POTION_DURATION_TABLE.put(true, "strong_leaping", 1340);       NEW_POTION_DURATION_TABLE.put(true, "strong_leaping", 1800);
		OLD_POTION_DURATION_TABLE.put(true, "water_breathing", 2700);      NEW_POTION_DURATION_TABLE.put(true, "water_breathing", 3600);
		OLD_POTION_DURATION_TABLE.put(true, "long_water_breathing", 7200); NEW_POTION_DURATION_TABLE.put(true, "long_water_breathing", 9600);
		OLD_POTION_DURATION_TABLE.put(true, "invisibility", 2700);         NEW_POTION_DURATION_TABLE.put(true, "invisibility", 3600);
		OLD_POTION_DURATION_TABLE.put(true, "long_invisibility", 7200);    NEW_POTION_DURATION_TABLE.put(true, "long_invisibility", 9600);
	}

	public static int getOldPotionDuration(boolean isSplash, String id) {
		if (OLD_POTION_DURATION_TABLE.contains(isSplash, id)) {
			return OLD_POTION_DURATION_TABLE.get(isSplash, id);
		} else {
			return -1;
		}
	}

	public static int getNewPotionDuration(boolean isSplash, String id) {
		if (NEW_POTION_DURATION_TABLE.contains(isSplash, id)) {
			return NEW_POTION_DURATION_TABLE.get(isSplash, id);
		} else {
			return -1;
		}
	}

	public static String getPotionTypeId(ItemStack potion) {
		if (potion.getType() == Material.POTION || potion.getType() == Material.SPLASH_POTION || potion.getType() == Material.LINGERING_POTION) {
			try {
				Object nmsItemStack = ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{potion});
				Object nbt = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "getTag");
				if (nbt != null && (Boolean)ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("NBTTagCompound"), nbt, "hasKey", new Class[]{String.class}, new Object[]{"Potion"})) {
					return ((String)ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("NBTTagCompound"), nbt, "getString", new Class[]{String.class}, new Object[]{"Potion"})).split(":")[1];
				}
				return "empty";
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
			}

		}
		return null;
	}

	public static ItemStack convertOldPotionDuration(ItemStack potion) {
		String potionType;
		if ((potion.getType() == Material.POTION || potion.getType() == Material.SPLASH_POTION) && OLD_POTION_DURATION_TABLE.contains(potion.getType() == Material.SPLASH_POTION, potionType = getPotionTypeId(potion))) {
			PotionMeta meta = (PotionMeta)potion.getItemMeta();
			PotionData potionData = meta.getBasePotionData();
			PotionEffectType effectType = potionData.getType().getEffectType();
			if (effectType != null) {
				boolean extended = potionData.isExtended();
				boolean upgraded = potionData.isUpgraded();
				meta.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
				boolean colorChanged = !meta.hasColor();
				if (colorChanged) {
					meta.setColor(effectType.getColor());
				}
				boolean nameChanged = !meta.hasDisplayName() && !meta.hasLocalizedName();
				if (nameChanged) {
					meta.setLocalizedName((potion.getType() == Material.SPLASH_POTION ? "splash_" : "") + "potion.effect." + (potionType).replaceAll("(long_|strong_)", ""));
				}
				ArrayList<PotionEffect> effects = Lists.newArrayList(meta.getCustomEffects());
				effects.add(0, new PotionEffect(effectType, getOldPotionDuration(potion.getType() == Material.SPLASH_POTION, potionType), upgraded ? 1 : 0, false, true));
				meta.clearCustomEffects();
				effects.forEach(effect -> meta.addCustomEffect(effect, false));
				potion = potion.clone();
				potion.setItemMeta(meta);
				try {
					Object nmsItemStack = ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{potion});
					Object nbt = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "getTag");
					if (nbt != null) {
						ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("NBTTagCompound"), nbt, "setString", new Class[]{String.class, String.class}, new Object[]{"OriginalPotionType", potionType});
						ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("NBTTagCompound"), nbt, "setBoolean", new Class[]{String.class, boolean.class}, new Object[]{"ColorChanged", colorChanged});
						ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("NBTTagCompound"), nbt, "setBoolean", new Class[]{String.class, boolean.class}, new Object[]{"NameChanged", nameChanged});
						ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "setTag", new Class[]{ReflectionUtil.getNMSClass("NBTTagCompound")}, new Object[]{nbt});
						potion = (ItemStack)ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
					}
				} catch (ReflectiveOperationException ex) {
					ex.printStackTrace();
				}
				return potion;
			}
		}
		return potion;
	}

	public static ItemStack convertNewPotionDuration(ItemStack potion) {
		if (potion.getType() == Material.POTION || potion.getType() == Material.SPLASH_POTION) {
			try {
				Object nmsItemStack = ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{potion});
				Object nbt = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "getTag");
				Class<?> nbtClass = ReflectionUtil.getNMSClass("NBTTagCompound");
				if (nbt != null && (Boolean)ReflectionUtil.invokeMethod(nbtClass, nbt, "hasKey", new Class[]{String.class}, new Object[]{"OriginalPotionType"})) {
					ReflectionUtil.invokeMethod(nbtClass, nbt, "setString", new Class[]{String.class, String.class}, new Object[]{"Potion", "minecraft:" + ReflectionUtil.invokeMethod(nbtClass, nbt, "getString", new Class[]{String.class}, new Object[]{"OriginalPotionType"})});
					boolean colorChanged = (Boolean)ReflectionUtil.invokeMethod(nbtClass, nbt, "getBoolean", new Class[]{String.class}, new Object[]{"ColorChanged"});
					boolean nameChanged = (Boolean)ReflectionUtil.invokeMethod(nbtClass, nbt, "getBoolean", new Class[]{String.class}, new Object[]{"NameChanged"});
					if (colorChanged) {
						ReflectionUtil.invokeMethod(nbtClass, nbt, "remove", new Class[]{String.class}, new Object[]{"CustomPotionColor"});
					}
					ReflectionUtil.invokeMethod(nbtClass, nbt, "remove", new Class[]{String.class}, new Object[]{"OriginalPotionType"});
					ReflectionUtil.invokeMethod(nbtClass, nbt, "remove", new Class[]{String.class}, new Object[]{"ColorChanged"});
					ReflectionUtil.invokeMethod(nbtClass, nbt, "remove", new Class[]{String.class}, new Object[]{"NameChanged"});
					ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "setTag", new Class[]{nbtClass}, new Object[]{nbt});
					potion = (ItemStack)ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
					PotionMeta meta = (PotionMeta)potion.getItemMeta();
					ArrayList<PotionEffect> effects = Lists.newArrayList(meta.getCustomEffects());
					PotionEffectType base = effects.get(0).getType();
					effects.remove(0);
					meta.clearCustomEffects();
					effects.forEach((effect) -> meta.addCustomEffect(effect, false));
					if (nameChanged) {
						meta.setLocalizedName(null);
					}
					potion.setItemMeta(meta);
				}
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
			}
		}
		return potion;
	}

	//これら2つのメソッドが変更するのは飲むポーションの効果時間だけ
	//スプラッシュポーション -> com.akon.oldpvp.listener.SplashPotionListener

	public static void oldPotionDuration() {
		try {
			Object potionRegistry = ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("PotionRegistry"), "a");
			for (Table.Cell<Boolean, String, Integer> cell: OLD_POTION_DURATION_TABLE.cellSet()) {
				if (!cell.getRowKey()) {
					Object minecraftKey = ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{cell.getColumnKey()});
					Object potionType = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("RegistryBlocks"), potionRegistry, "get", new Class[]{Object.class}, new Object[]{minecraftKey});
					ReflectionUtil.setField(ReflectionUtil.getNMSClass("MobEffect"), ((List<?>)ReflectionUtil.getField(ReflectionUtil.getNMSClass("PotionRegistry"), potionType, "e")).get(0), "duration", cell.getValue());
				}
			}
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
	}

	public static void newPotionDuration() {
		try {
			Object potionRegistry = ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("PotionRegistry"), "a");
			for (Table.Cell<Boolean, String, Integer> cell: NEW_POTION_DURATION_TABLE.cellSet()) {
				if (!cell.getRowKey()) {
					Object minecraftKey = ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{cell.getColumnKey()});
					Object potionType = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("RegistryBlocks"), potionRegistry, "get", new Class[]{Object.class}, new Object[]{minecraftKey});
					ReflectionUtil.setField(ReflectionUtil.getNMSClass("MobEffect"), ((List<?>)ReflectionUtil.getField(ReflectionUtil.getNMSClass("PotionRegistry"), potionType, "e")).get(0), "duration", cell.getValue());
				}
			}
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
	}

}
