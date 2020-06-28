package com.akon.oldpvp;

import com.akon.oldpvp.listener.*;
import com.akon.oldpvp.listener.packet.*;
import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OldPvP extends JavaPlugin {

    private static OldPvP instance;
    private static Object strength;
    private static Object strengthAttributeModifier;
    private static Object weakness;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        OldPvPCommand commandExecutor = new OldPvPCommand();
        this.getCommand("oldpvp").setExecutor(commandExecutor);
        this.getCommand("oldpvp").setTabCompleter(commandExecutor);
        new OldPvPRunnable().runTaskTimer(this, 0, 1);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConsumeListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnchantListener(), this);
        Bukkit.getPluginManager().registerEvents(new FishingRodListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new SplashPotionListener(), this);
        Bukkit.getPluginManager().registerEvents(new SwapHandItemsListener(), this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new EntityStatusListener());
        ProtocolLibrary.getProtocolManager().addPacketListener(new ItemPacketListener());
        ProtocolLibrary.getProtocolManager().addPacketListener(new ParticleListener());
        ProtocolLibrary.getProtocolManager().addPacketListener(new SoundListener());
        ProtocolLibrary.getProtocolManager().addPacketListener(new WindowItemsListener());
        try {
            Object effectRegistry = ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("MobEffectList"), "REGISTRY");
            strength = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("RegistryMaterials"), effectRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"strength"})});
            strengthAttributeModifier = ((Map<Object, Object>)ReflectionUtil.getField(ReflectionUtil.getNMSClass("MobEffectList"), strength, "a")).get(ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("GenericAttributes"), "ATTACK_DAMAGE"));
            weakness = ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("RegistryMaterials"), effectRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"weakness"})});
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        this.reloadConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach((p) -> p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4));
        updateAllPlayersInventory();
        PotionDurationMapping.newPotionDuration();
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        ArrayList<Recipe> recipeBackup = Lists.newArrayList();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (!(recipe instanceof ShapedRecipe && ((ShapedRecipe)recipe).getKey().equals(new NamespacedKey(this, "enchanted_golden_apple")))) {
                recipeBackup.add(recipe);
            }
        }
        Bukkit.clearRecipes();
        recipeBackup.forEach(Bukkit::addRecipe);
        try {
            for (String id: ItemMapping.getAllToolIds()) {
                ReflectionUtil.setField(ItemMapping.getItem(id), id.contains("sword") ? "a" : "b", ItemMapping.getNewDamage(id));
            }
            for (String id: ItemMapping.getAllDiamondPieceIds()) {
                ReflectionUtil.setField(ItemMapping.getItem(id), "e", 2);
            }
            ReflectionUtil.setField(strength, "a", 3.0);
            ReflectionUtil.setField(strengthAttributeModifier, "b", 0);
            ReflectionUtil.setField(weakness, "a", -4.0);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (p.getScoreboard().getTeam("collision") != null && p.getScoreboard().getTeam("collision").hasEntry(p.getName())) {
                p.getScoreboard().getTeam("collision").removeEntry(p.getName());
            }
        }
    }

    public static OldPvP getInstance() {
        return instance;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        updateAllPlayersInventory();
        if (ConfigManager.getBoolean("Combat.disable-attack-speed")) {
            Bukkit.getOnlinePlayers().forEach((p) -> p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(30));
        } else {
            Bukkit.getOnlinePlayers().forEach((p) -> p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4));
        }
        if (ConfigManager.getBoolean("Potion.old-potion-duration")) {
            PotionDurationMapping.oldPotionDuration();
        } else {
            PotionDurationMapping.newPotionDuration();
        }
        if (ConfigManager.getBoolean("GoldenApple.god-apple-recipe")) {
            List<Recipe> recipes = Bukkit.getRecipesFor(new ItemStack(Material.GOLDEN_APPLE, 1, (short)1));
            boolean flag = recipes.isEmpty();
            if (!flag) {
                flag = true;
                for (Recipe recipe: recipes) {
                    if (recipe instanceof ShapedRecipe && ((ShapedRecipe)recipe).getKey().equals(new NamespacedKey(this, "enchanted_golden_apple"))) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                Bukkit.addRecipe(new ShapedRecipe(new NamespacedKey(this, "enchanted_golden_apple"), new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1)).shape("XXX", "XYX", "XXX").setIngredient('X', Material.GOLD_BLOCK).setIngredient('Y', Material.APPLE));
            }
        } else {
            Iterator<Recipe> iterator = Bukkit.recipeIterator();
            ArrayList<Recipe> recipeBackup = Lists.newArrayList();
            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                if (!(recipe instanceof ShapedRecipe && ((ShapedRecipe)recipe).getKey().equals(new NamespacedKey(this, "enchanted_golden_apple")))) {
                    recipeBackup.add(recipe);
                }
            }
            Bukkit.clearRecipes();
            recipeBackup.forEach(Bukkit::addRecipe);
        }
        try {
            if (ConfigManager.getBoolean("Item.old-tool-damage")) {
                for (String id: ItemMapping.getAllToolIds()) {
                    ReflectionUtil.setField(ItemMapping.getItem(id), id.contains("sword") ? "a" : "b", ItemMapping.getOldDamage(id));
                }
            } else {
                for (String id: ItemMapping.getAllToolIds()) {
                    ReflectionUtil.setField(ItemMapping.getItem(id), id.contains("sword") ? "a" : "b", ItemMapping.getNewDamage(id));
                }
            }
            if (ConfigManager.getBoolean("Item.old-diamond-armors")) {
                for (String id: ItemMapping.getAllDiamondPieceIds()) {
                    ReflectionUtil.setField(ItemMapping.getItem(id), "e", 0);
                }
            } else {
                for (String id: ItemMapping.getAllDiamondPieceIds()) {
                    ReflectionUtil.setField(ItemMapping.getItem(id), "e", 2);
                }
            }
            if (ConfigManager.getBoolean("Potion.old-strength")) {
                ReflectionUtil.setField(strength, "a", 1.3);
                ReflectionUtil.setField(strengthAttributeModifier, "b", 2);
            } else {
                ReflectionUtil.setField(strength, "a", 3.0);
                ReflectionUtil.setField(strengthAttributeModifier, "b", 0);
            }
            if (ConfigManager.getBoolean("Potion.old-weakness")) {
                ReflectionUtil.setField(weakness, "a", -0.5);
            } else {
                ReflectionUtil.setField(weakness, "a", -4.0);
            }
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (ConfigManager.getBoolean("Utilities.disable-collision")) {
                if (p.getScoreboard().getTeam("collision") == null) {
                    Team team = p.getScoreboard().registerNewTeam("collision");
                    team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                }
                if (!p.getScoreboard().getTeam("collision").hasEntry(p.getName())) {
                    p.getScoreboard().getTeam("collision").addEntry(p.getName());
                }
            } else {
                if (p.getScoreboard().getTeam("collision") != null && p.getScoreboard().getTeam("collision").hasEntry(p.getName())) {
                    p.getScoreboard().getTeam("collision").removeEntry(p.getName());
                }
            }
        }
    }

    private static void updateAllPlayersInventory() {
        Bukkit.getOnlinePlayers().forEach(Player::updateInventory);
    }

}
