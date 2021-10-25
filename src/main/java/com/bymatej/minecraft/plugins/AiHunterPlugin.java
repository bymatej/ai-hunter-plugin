package com.bymatej.minecraft.plugins;

import org.bukkit.plugin.java.JavaPlugin;

import com.bymatej.minecraft.plugins.commands.AiHunterCommand;

import static com.bymatej.minecraft.plugins.utils.CommonUtils.log;
import static java.util.Objects.requireNonNull;

public final class AiHunterPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        log("Plugin loaded!");
        requireNonNull(getCommand("aihunter")).setExecutor(new AiHunterCommand());
    }

    @Override
    public void onDisable() {
        log("Plugin un-loaded!");
    }

}
