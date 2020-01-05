package me.Serophots.AntiCheat.Checks.Movement.Speed;

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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class SpeedC implements Listener {
    List<Player> disbaledPlayers = new ArrayList<>();
    boolean lastTick = false, lastLastTick = false;
    double lastTickY;

    private Main plugin;
    public SpeedC(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.speedc.enabled")) {
            Player p = e.getPlayer();
            if (!disbaledPlayers.contains(p)) {
                if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                    double y = e.getTo().getY();
                    boolean onGround = isNearGround(e.getTo());

                    boolean onGroundLastTick = this.lastTick;
                    this.lastTick = onGround;

                    boolean onGroundLastLastTick = this.lastLastTick;
                    this.lastLastTick = onGroundLastTick;

                    double LastTick = lastTickY;
                    lastTickY = y;

                    if (onGround && onGroundLastTick && onGroundLastLastTick) {
                        if (!isNearWater(e.getFrom()) && !isNearWater(e.getTo())) {
                            if (y == Math.floor(y) && !isOnTrapdoor(e.getTo())) {
                                if (LastTick > (y + 0.2) && !e.getPlayer().isInsideVehicle()) {
                                    if(isRoughlyEqual(LastTick - (y + 0.2), 0.031)) return;
                                    if (plugin.getConfig().getBoolean("checks.speedc.cancelMovement")) {
                                        e.setCancelled(true);
                                    }
                                    p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Speed (C) &6Movement: &7" + (LastTick - (y + 0.2))));
                                    plugin.getFlagManager().violate(p, FlagManager.check.SPEEDC);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void handle2(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            disbaledPlayers.add(p);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    disbaledPlayers.remove(p);
                }
            }, 10);
        }
    }

    public boolean isNearGround(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnTrapdoor(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.99, z).getBlock().getType() == Material.TRAP_DOOR || location.clone().add(x, -0.99, z).getBlock().getType() == Material.IRON_TRAPDOOR){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNearWater(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.5001, z).getBlock().getType() == Material.STATIONARY_WATER){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRoughlyEqual(double d1, double d2){
        return Math.abs(d1 - d2) < 0.001;
    }
}
