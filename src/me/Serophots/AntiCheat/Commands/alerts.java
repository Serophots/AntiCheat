package me.Serophots.AntiCheat.Commands;

import me.Serophots.AntiCheat.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;

public class alerts implements CommandExecutor {
    private Main plugin;
    private static HashMap<UUID, Boolean> alerts;
    public alerts(Main plugin, HashMap<UUID, Boolean> alerts){
        this.plugin = plugin;
        this.alerts = alerts;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        return false;
    }
}
