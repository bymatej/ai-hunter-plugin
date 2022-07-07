package com.bymatej.minecraft.plugins.aihunter;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bymatej.minecraft.plugins.aihunter.commands.AiHunterCommand;
import com.bymatej.minecraft.plugins.aihunter.listeners.EntityDeathEventListener;
import com.bymatej.minecraft.plugins.aihunter.listeners.HunterFreezeEventListener;
import com.bymatej.minecraft.plugins.aihunter.listeners.HunterToggleEventListener;

import net.citizensnpcs.api.npc.NPC;

import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.util.Objects.requireNonNull;

public final class AiHunterPlugin extends JavaPlugin {

    private static AiHunterPlugin plugin;

    private List<NPC> aiHunters;

    private int hunterId = 0;

    public static AiHunterPlugin getPluginReference() {
        return plugin;
    }

    @Override
    public void onEnable() {
        this.plugin = this;
        this.aiHunters = new LinkedList<>();
        this.hunterId = 0;
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
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new HunterToggleEventListener(), this);
        pluginManager.registerEvents(new EntityDeathEventListener(), this);
        pluginManager.registerEvents(new HunterFreezeEventListener(), this);
    }

    @Override
    public void onDisable() {
        log("Plugin un-loaded!");
    }

    public static AiHunterPlugin getPlugin() {
        return plugin;
    }

    public List<NPC> getAiHunters() {
        return aiHunters;
    }

    public int getHunterId() {
        return hunterId;
    }

    public void setAiHunters(List<NPC> aiHunters) {
        this.aiHunters = aiHunters;
    }

    public void setHunterId(int hunterId) {
        this.hunterId = hunterId;
    }
}
