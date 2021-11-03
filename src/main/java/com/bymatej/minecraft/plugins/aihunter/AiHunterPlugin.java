package com.bymatej.minecraft.plugins.aihunter;

import com.bymatej.minecraft.plugins.aihunter.commands.AiHunterCommand;
import com.bymatej.minecraft.plugins.aihunter.listeners.HungerDropEvent;
import com.bymatej.minecraft.plugins.aihunter.listeners.RespawnPlayerToDeathPointEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.killDb;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.removeCurrentHunter;
import static java.util.Objects.requireNonNull;

public final class AiHunterPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
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
    }

    private void registerEventListeners() {
        getServer().getPluginManager().registerEvents(new HungerDropEvent(), this);
        getServer().getPluginManager().registerEvents(new RespawnPlayerToDeathPointEvent(), this);
    }

    @Override
    public void onDisable() {
        removeCurrentHunter();
        killDb(); //todo consider if this is needed
        log("Plugin un-loaded!");
    }

}
