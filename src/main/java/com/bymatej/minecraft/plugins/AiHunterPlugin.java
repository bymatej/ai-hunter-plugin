package com.bymatej.minecraft.plugins;

import com.bymatej.minecraft.plugins.listeners.HungerDropEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.bymatej.minecraft.plugins.commands.AiHunterCommand;

import static com.bymatej.minecraft.plugins.utils.CommonUtils.log;
import static java.util.Objects.requireNonNull;

public final class AiHunterPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        log("Plugin loaded!");
        registerCommands();
        registerEventListeners();
    }

    private void registerCommands() {
        requireNonNull(getCommand("aihunter")).setExecutor(new AiHunterCommand());
    }

    private void registerEventListeners() {
        getServer().getPluginManager().registerEvents(new HungerDropEvent(), this);
    }

    @Override
    public void onDisable() {
        log("Plugin un-loaded!");
    }

}
