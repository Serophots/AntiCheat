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

public class JesusB implements Listener {

    double LastY;
    int threshhold = 20;
    int counter = 0;

    private Main plugin;
    public JesusB(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.jesusb.enabled")) {
            Player p = e.getPlayer();
            if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                if (p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {

                    double y = p.getLocation().getY();
                    double LastY = this.LastY;
                    this.LastY = y;

                    double dist = y - LastY;

                    if (dist == 0) return;
                    if (threshhold != 0) {
                        threshhold -= 1;
                        if (dist < 0) {
                            if (dist > 0 - 0.02D) {
                                counter += 1;
                            }
                        } else {
                            if (dist < 0.02D) {
                                counter += 1;
                            }
                        }
                    } else {
                        if (counter >= plugin.getConfig().getInt("checks.jesusb.maxCounter") && !e.getPlayer().isInsideVehicle()) {
                            if(plugin.getConfig().getBoolean("checks.jesusb.cancelMovement")) {
                                e.setCancelled(true);
                            }
                            p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Jesus (B) &6Counter: &7" + counter + " &6Regular: &74"));
                            plugin.getFlagManager().violate(p, FlagManager.check.JESUSB);
                        }
                        threshhold = 20;
                        counter = 0;
                    }
                }
            }
        }
    }
}