package com.announcer.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class AnnouncerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Load the configuration file
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        // Register the command executor, passing 'this' plugin instance to it
        if (this.getCommand("announce") != null) {
            this.getCommand("announce").setExecutor(new AnnouncerCommand(this));
        }
    }

    @Override
    public void onDisable() {

    }
}