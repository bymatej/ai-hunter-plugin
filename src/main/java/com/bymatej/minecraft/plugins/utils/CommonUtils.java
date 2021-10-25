package com.bymatej.minecraft.plugins.utils;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import static java.util.logging.Level.INFO;

public class CommonUtils {

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
