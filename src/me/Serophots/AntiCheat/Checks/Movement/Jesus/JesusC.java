package me.Serophots.AntiCheat.Checks.Movement.Jesus;

import me.Serophots.AntiCheat.Flags.FlagManager;
import me.Serophots.AntiCheat.Main;
import me.Serophots.AntiCheat.utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class JesusC implements Listener {
    double lastY, lastLastY;

    private Main plugin;
    public JesusC(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.jesusc.enabled")) {
            Player p = e.getPlayer();
            if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                if (p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                    double distX = e.getTo().getX() - e.getFrom().getX();
                    double distZ = e.getTo().getZ() - e.getFrom().getZ();
                    double dist = (distX * distX) + (distZ * distZ);
                    double y = p.getLocation().getY();

                    boolean Flat = false;

                    double lastY = this.lastY;
                    this.lastY = y;
                    boolean lastFlat = false;

                    double lastLastY = this.lastLastY;
                    this.lastLastY = lastY;
                    boolean lastLastFlat = false;

                    if(lastY == Math.floor(lastY)){
                        lastFlat = true;
                    }
                    if(lastLastY == Math.floor(lastLastY)){
                        lastLastFlat = true;
                    }
                    if(y == Math.floor(y)){
                        Flat = true;
                    }

                    if(Flat && lastFlat && lastLastFlat){
                        if (dist > plugin.getConfig().getDouble("checks.jesusc.maxDist")) {
                            if (plugin.getConfig().getBoolean("checks.jesusb.cancelMovement")) {
                                e.setCancelled(true);
                            }
                            p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Jesus (C) &6Movement: &7" + dist + " &6Regular: &7<" + plugin.getConfig().getDouble("checks.jesusc.maxDist")));
                            plugin.getFlagManager().violate(p, FlagManager.check.JESUSC);
                        }
                    }
                }
            }
        }
    }
}