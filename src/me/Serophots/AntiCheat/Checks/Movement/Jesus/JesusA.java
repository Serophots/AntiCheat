package me.Serophots.AntiCheat.Checks.Movement.Jesus;

import me.Serophots.AntiCheat.Flags.FlagManager;
import me.Serophots.AntiCheat.Main;
import me.Serophots.AntiCheat.utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class JesusA implements Listener {
    boolean lastOnGround, lastLastOnGround, lastInWater, lastLastInWater;

    private Main plugin;
    public JesusA(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.jesusa.enabled")) {
            Player p = e.getPlayer();
            if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                boolean onGround = isNearGround(e.getTo());
                boolean inWater = isNearWater(e.getTo());

                boolean lastOnGround = this.lastOnGround;
                this.lastOnGround = onGround;

                boolean lastLastOnGround = this.lastLastOnGround;
                this.lastLastOnGround = lastOnGround;

                boolean lastInWater = this.lastInWater;
                this.lastInWater = inWater;

                boolean lastLastInWater = this.lastLastInWater;
                this.lastLastInWater = lastInWater;


                if (!onGround && !lastOnGround && !lastLastOnGround && !e.getPlayer().isInsideVehicle()) {
                    if (inWater && lastInWater && lastLastInWater) {
                        if (e.getTo().getY() < Math.floor(e.getTo().getY()) + 1.1) {
                            if(plugin.getConfig().getBoolean("checks.jesusa.cancelMovement")) {
                                e.setCancelled(true);
                            }
                            p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Jesus (A) &6Level: &7" + e.getTo().getY() + " &6Regular: &7>" + (Math.floor(e.getTo().getY()) + 0.8D)));
                            plugin.getFlagManager().violate(p, FlagManager.check.JESUSA);
                        }
                    }
                }
            }
        }
    }

    public boolean isNearGround(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR && location.clone().add(x, -0.5001, z).getBlock().getType() != Material.STATIONARY_WATER){
                    return true;
                }
            }
        }
        return false;
    }

    /*public boolean isNearWater(Location location){
        double expand = 1;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.8001, z).getBlock().getType() != Material.STATIONARY_WATER){
                    return false;
                }
            }
        }
        return true;
    }*/
    public boolean isNearWater(Location location){
        double expand = 1;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.8001, z).getBlock().getType() == Material.STATIONARY_WATER){
                    if(location.clone().add(x, 0.25, z).getBlock().getType() != Material.STATIONARY_WATER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
