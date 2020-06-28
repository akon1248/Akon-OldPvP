package com.akon.oldpvp.utils;

import com.akon.oldpvp.OldPvP;

public class ConfigManager {
    
    public static Object get(String path) {
        return OldPvP.getInstance().getConfig().get(path);
    }

    public static Object get(String path, Object def) {
        return OldPvP.getInstance().getConfig().get(path, def);
    }

    public static boolean getBoolean(String path) {
        return OldPvP.getInstance().getConfig().getBoolean(path, false);
    }

    public static boolean getBoolean(String path, boolean def) {
        return OldPvP.getInstance().getConfig().getBoolean(path, def);
    }

    public static Number getNumber(String path) {
        return OldPvP.getInstance().getConfig().getDouble(path);
    }

    public static Number getNumber(String path,  Number def) {
        return OldPvP.getInstance().getConfig().getDouble(path, def.doubleValue());
    }

    public static String getString(String path) {
        return OldPvP.getInstance().getConfig().getString(path);
    }

    public static String getString(String path, String def) {
        return OldPvP.getInstance().getConfig().getString(path, def);
    }

}
