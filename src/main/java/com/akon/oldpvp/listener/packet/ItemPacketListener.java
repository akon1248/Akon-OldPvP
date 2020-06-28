package com.akon.oldpvp.listener.packet;

import com.akon.oldpvp.ItemMapping;
import com.akon.oldpvp.OldPvP;
import com.akon.oldpvp.PotionDurationMapping;
import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemPacketListener extends PacketAdapter {

	private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

	public ItemPacketListener() {
		super(OldPvP.getInstance(), PacketType.Play.Client.SET_CREATIVE_SLOT, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS);
	}

	@Override
	public void onPacketReceiving(PacketEvent e) {
		if (e.getPacketType() == PacketType.Play.Client.SET_CREATIVE_SLOT) {
			ItemStack item = e.getPacket().getItemModifier().read(0);
			item = newItemInfo(PotionDurationMapping.convertNewPotionDuration(item));
			e.getPacket().getItemModifier().write(0, item);
		}
	}

	@Override
	public void onPacketSending(PacketEvent e) {
		if (e.getPacketType() == PacketType.Play.Server.SET_SLOT) {
			ItemStack item = e.getPacket().getItemModifier().read(0);
			if (ConfigManager.getBoolean("Item.old-item-info")) {
				item = oldItemInfo(item);
			}
			if (ConfigManager.getBoolean("Potion.old-potion-duration")) {
				item = PotionDurationMapping.convertOldPotionDuration(item);
			}
			e.getPacket().getItemModifier().write(0, item);
		} else if (e.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
			List<ItemStack> itemList = e.getPacket().getItemListModifier().read(0);
			if (ConfigManager.getBoolean("Item.old-item-info")) {
				itemList = itemList.stream().map(ItemPacketListener::oldItemInfo).collect(Collectors.toList());
			}
			if (ConfigManager.getBoolean("Potion.old-potion-duration")) {
				itemList = itemList.stream().map(PotionDurationMapping::convertOldPotionDuration).collect(Collectors.toList());
			}
			e.getPacket().getItemListModifier().write(0, itemList);
		}
	}

	private static ItemStack oldItemInfo(ItemStack item) {
		if (item.getItemMeta() != null && !item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
			try {
				Object nmsItemStack = ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{item});
				Object nmsItem = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "getItem");
				if (ReflectionUtil.getNMSClass("ItemTool").isInstance(nmsItem) || ReflectionUtil.getNMSClass("ItemSword").isInstance(nmsItem) || ReflectionUtil.getNMSClass("ItemArmor").isInstance(nmsItem)) {
					Object nbt = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "getTag");
					Class<?> nbtClass = ReflectionUtil.getNMSClass("NBTTagCompound");
					if (nbt == null) {
						nbt = ReflectionUtil.invokeConstructor(nbtClass);
					}
					if (ReflectionUtil.getNMSClass("ItemTool").isInstance(nmsItem) || ReflectionUtil.getNMSClass("ItemSword").isInstance(nmsItem)) {
						if (!(Boolean)ReflectionUtil.invokeMethod(nbtClass, nbt, "hasKey", new Class[]{String.class}, new Object[]{"AttributeModifiers"})) {
							double toolDamage;
							if (ConfigManager.getBoolean("Item.old-tool-damage")) {
								toolDamage = ItemMapping.getOldDamage(ItemMapping.getToolIdFromMaterial(item.getType()));
							} else {
								toolDamage = ItemMapping.getNewDamage(ItemMapping.getToolIdFromMaterial(item.getType()));
							}
							int sharpnessLevel = item.getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ALL);
							if (sharpnessLevel > 0) {
								if (ConfigManager.getBoolean("Enchantment.old-sharpness")) {
									toolDamage += (double)sharpnessLevel*1.25;
								} else {
									toolDamage += (double)sharpnessLevel <= 1 ? 1 : 1+(((double)sharpnessLevel-1)*0.5);
								}
							}
							ReflectionUtil.invokeMethod(nbtClass, nbt, "setBoolean", new Class[]{String.class, boolean.class}, new Object[]{"OldItemInfo", true});
							Object attributeModifier = ReflectionUtil.invokeConstructor(nbtClass);
							ReflectionUtil.invokeMethod(nbtClass, attributeModifier, "a", new Class[]{String.class, UUID.class}, new Object[]{"UUID", ATTACK_DAMAGE_MODIFIER_UUID});
							ReflectionUtil.invokeMethod(nbtClass, attributeModifier, "setDouble", new Class[]{String.class, double.class}, new Object[]{"Amount", toolDamage});
							ReflectionUtil.invokeMethod(nbtClass, attributeModifier, "setString", new Class[]{String.class, String.class}, new Object[]{"Slot", "mainhand"});
							ReflectionUtil.invokeMethod(nbtClass, attributeModifier, "setString", new Class[]{String.class, String.class}, new Object[]{"AttributeName", "generic.attackDamage"});
							ReflectionUtil.invokeMethod(nbtClass, attributeModifier, "setInt", new Class[]{String.class, int.class}, new Object[]{"Operation", 0});
							ReflectionUtil.invokeMethod(nbtClass, attributeModifier, "setString", new Class[]{String.class, String.class}, new Object[]{"Name", ReflectionUtil.getNMSClass("ItemSword").isInstance(nmsItem) ? "Weapon modifier" : "Tool modifier"});
							Object attributeModifiers = ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("NBTTagList"));
							ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("NBTTagList"), attributeModifiers, "add", new Class[]{ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{attributeModifier});
							ReflectionUtil.invokeMethod(nbtClass, nbt, "set", new Class[]{String.class, ReflectionUtil.getNMSClass("NBTBase")}, new Object[]{"AttributeModifiers", attributeModifiers});
							ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "setTag", new Class[]{nbtClass}, new Object[]{nbt});
							item = (ItemStack)ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
						}
					} else if (ReflectionUtil.getNMSClass("ItemArmor").isInstance(nmsItem)) {
						ReflectionUtil.invokeMethod(nbtClass, nbt, "setBoolean", new Class[]{String.class, boolean.class}, new Object[]{"OldItemInfo", true});
						ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "setTag", new Class[]{nbtClass}, new Object[]{nbt});
						item = (ItemStack)ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
						ItemMeta meta = item.getItemMeta();
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						item.setItemMeta(meta);
					}
				}
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
			}
		}
		return item;
	}

	private static ItemStack newItemInfo(ItemStack item) {
		try {
			Object nmsItemStack = ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{item});
			Object nmsItem = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "getItem");
			Object nbt = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "getTag");
			Class<?> nbtClass = ReflectionUtil.getNMSClass("NBTTagCompound");
			if (nbt != null && (Boolean)ReflectionUtil.invokeMethod(nbtClass, nbt, "getBoolean", new Class[]{String.class}, new Object[]{"OldItemInfo"})) {
				ReflectionUtil.invokeMethod(nbtClass, nbt, "remove", new Class[]{String.class}, new Object[]{"OldItemInfo"});
				if (ReflectionUtil.getNMSClass("ItemTool").isInstance(nmsItem) || ReflectionUtil.getNMSClass("ItemSword").isInstance(nmsItem)) {
					ReflectionUtil.invokeMethod(nbtClass, nbt, "remove", new Class[]{String.class}, new Object[]{"AttributeModifiers"});
				} else if (ReflectionUtil.getNMSClass("ItemArmor").isInstance(nmsItem)) {
					int bitmask = (Integer)ReflectionUtil.invokeMethod(nbtClass, nbt, "getInt", new Class[]{String.class}, new Object[]{"HideFlags"});
					if ((bitmask >> 1 & 1) == 1) {
						bitmask -= 2;
					}
					ReflectionUtil.invokeMethod(nbtClass, nbt, "setInt", new Class[]{String.class, int.class}, new Object[]{"HideFlags", bitmask});
				}
				ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("ItemStack"), nmsItemStack, "setTag", new Class[]{nbtClass}, new Object[]{nbt});
				item = (ItemStack)ReflectionUtil.invokeStaticMethod(ReflectionUtil.getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[]{ReflectionUtil.getNMSClass("ItemStack")}, new Object[]{nmsItemStack});
			}
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
		return item;
	}

}
