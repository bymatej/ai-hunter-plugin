package com.bymatej.minecraft.plugins.aihunter;

import com.bymatej.minecraft.plugins.aihunter.commands.AiHunterCommand;
import com.bymatej.minecraft.plugins.aihunter.listeners.HungerDropEventListener;
import com.bymatej.minecraft.plugins.aihunter.listeners.HunterToggleEventListener;
import com.bymatej.minecraft.plugins.aihunter.listeners.RespawnPlayerToDeathPointEventListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.util.Objects.requireNonNull;

public final class AiHunterPlugin extends JavaPlugin {

    private static AiHunterPlugin plugin;

    public static AiHunterPlugin getPluginReference() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        registerConfig();
        registerCommands();
        registerEventListeners();
        log("Plugin loaded!");
    }

    private void registerConfig() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void registerCommands() {
        requireNonNull(getCommand("aihunter")).setExecutor(new AiHunterCommand());
        requireNonNull(getCommand("test")).setExecutor(new AiHunterCommand());
    }

    private void registerEventListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new HunterToggleEventListener(), this);
        pluginManager.registerEvents(new HungerDropEventListener(), this);
        pluginManager.registerEvents(new RespawnPlayerToDeathPointEventListener(), this);
    }

    @Override
    public void onDisable() {
        log("Plugin un-loaded!");
    }

}
