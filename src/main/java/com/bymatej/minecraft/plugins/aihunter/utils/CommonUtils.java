package com.bymatej.minecraft.plugins.aihunter.utils;

import com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPlugin;
import static java.util.logging.Level.INFO;

public class CommonUtils {

    public static AiHunterPlugin getPluginReference() {
        return getPlugin((AiHunterPlugin.class));
    }

    public static void log(String message) {
        log(INFO, message);
    }

    public static void log(Level level, String message) {
        Bukkit.getLogger().log(level, message);
    }

    public static void log(Level level, String message, Throwable throwable) {
        Bukkit.getLogger().log(level, message, throwable);
    }

}
