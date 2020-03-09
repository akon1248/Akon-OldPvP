package com.akon.oldpvp;

import com.akon.oldpvp.listeners.*;
import com.akon.oldpvp.listeners.packet.EntityStatusListener;
import com.akon.oldpvp.listeners.packet.ParticleListener;
import com.akon.oldpvp.listeners.packet.SoundListener;
import com.akon.oldpvp.listeners.packet.WindowItemsListener;
import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
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

import java.util.*;

public class OldPvP extends JavaPlugin {

    private static OldPvP instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        this.getCommand("oldpvp").setExecutor(new OldPvPCommand());
        this.getCommand("oldpvp").setTabCompleter(new OldPvPCommand());
        new OldPvPRunnable().runTaskTimer(this, 0, 1);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConsumeListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnchantListener(), this);
        Bukkit.getPluginManager().registerEvents(new FishingListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(), this);
        Bukkit.getPluginManager().registerEvents(new SwapHandItemsListener(), this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new EntityStatusListener(this, ListenerPriority.NORMAL));
        ProtocolLibrary.getProtocolManager().addPacketListener(new ParticleListener(this, ListenerPriority.NORMAL));
        ProtocolLibrary.getProtocolManager().addPacketListener(new SoundListener(this, ListenerPriority.NORMAL));
        ProtocolLibrary.getProtocolManager().addPacketListener(new WindowItemsListener(this, ListenerPriority.NORMAL));
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach((p) -> p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4));
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        ArrayList<Recipe> recipeBackup = new ArrayList<>();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (!(recipe instanceof ShapedRecipe && ((ShapedRecipe)recipe).getKey().equals(new NamespacedKey(this, "enchanted_golden_apple")))) {
                recipeBackup.add(recipe);
            }
        }
        Bukkit.clearRecipes();
        recipeBackup.forEach(Bukkit::addRecipe);
        try {
            Object effectRegistry = ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("MobEffectList"), "REGISTRY");
            Object strength = ReflectionUtil.invokeMethod(effectRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"strength"})});
            Object weakness = ReflectionUtil.invokeMethod(effectRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"weakness"})});
            ReflectionUtil.setField(strength, "a", 3.0);
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
        if (ConfigManager.getBoolean("Melee.disable-attack-speed")) {
            Bukkit.getOnlinePlayers().forEach((p) -> p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(30));
        } else {
            Bukkit.getOnlinePlayers().forEach((p) -> p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4));
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
            ArrayList<Recipe> recipeBackup = new ArrayList<>();
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
            Object itemRegistry = ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("Item"), "REGISTRY");
            Object woodenAxe = ReflectionUtil.invokeMethod(itemRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"wooden_axe"})});
            Object stoneAxe = ReflectionUtil.invokeMethod(itemRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"stone_axe"})});
            Object ironAxe = ReflectionUtil.invokeMethod(itemRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"iron_axe"})});
            Object diamondAxe = ReflectionUtil.invokeMethod(itemRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"diamond_axe"})});
            Object goldenAxe = ReflectionUtil.invokeMethod(itemRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"golden_axe"})});
            Object effectRegistry = ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("MobEffectList"), "REGISTRY");
            Object strength = ReflectionUtil.invokeMethod(effectRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"strength"})});
            Object strengthAttributeModifier = ((Map<Object, Object>)ReflectionUtil.getField(ReflectionUtil.getNMSClass("MobEffectList"), strength, "a")).get(ReflectionUtil.getStaticField(ReflectionUtil.getNMSClass("GenericAttributes"), "ATTACK_DAMAGE"));
            Object weakness = ReflectionUtil.invokeMethod(effectRegistry, "get", new Class[]{Object.class}, new Object[]{ReflectionUtil.invokeConstructor(ReflectionUtil.getNMSClass("MinecraftKey"), new Class[]{String.class}, new Object[]{"weakness"})});
            if (ConfigManager.getBoolean("Melee.old-axe-damage")) {
                ReflectionUtil.setField(woodenAxe, "b", 2.0F);
                ReflectionUtil.setField(stoneAxe, "b", 3.0F);
                ReflectionUtil.setField(ironAxe, "b", 4.0F);
                ReflectionUtil.setField(diamondAxe, "b", 5.0F);
                ReflectionUtil.setField(goldenAxe, "b", 2.0F);
            } else {
                ReflectionUtil.setField(woodenAxe, "b", 6.0F);
                ReflectionUtil.setField(stoneAxe, "b", 8.0F);
                ReflectionUtil.setField(ironAxe, "b", 8.0F);
                ReflectionUtil.setField(diamondAxe, "b", 8.0F);
                ReflectionUtil.setField(goldenAxe, "b", 6.0F);
            }
            if (ConfigManager.getBoolean("PotionEffects.old-strength")) {
                ReflectionUtil.setField(strength, "a", 1.3);
                ReflectionUtil.setField(strengthAttributeModifier, "b", 2);
            } else {
                ReflectionUtil.setField(strength, "a", 3.0);
                ReflectionUtil.setField(strengthAttributeModifier, "b", 0);
            }
            if (ConfigManager.getBoolean("PotionEffects.old-weakness")) {
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

}
