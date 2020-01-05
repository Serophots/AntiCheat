package me.Serophots.AntiCheat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    private Main plugin;

    public ConfigManager(Main plugin){
        this.plugin = plugin;
    }

    public FileConfiguration messagescfg;
    public File messagesfile;

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        messagesfile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesfile.exists()) {
            plugin.saveResource("messages.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "messages.yml has been created");
        }
        messagescfg = YamlConfiguration.loadConfiguration(messagesfile);
    }

    public FileConfiguration getMessages() {
        return messagescfg;
    }

    public void reloadMessages() {
        messagescfg = YamlConfiguration.loadConfiguration(messagesfile);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "The messages.yml file has been reload");

    }
}
