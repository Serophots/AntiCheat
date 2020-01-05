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

public class SpeedD implements Listener {
    List<Player> disbaledPlayers = new ArrayList<>();
    boolean lastOnGround, LastLastOnGround;
    double lastTickY = 0;

    private Main plugin;
    public SpeedD(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.speedd.enabled")) {
            Player p = e.getPlayer();
            if (!disbaledPlayers.contains(p)) {
                if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                    double y = e.getTo().getY();
                    boolean onGround = isNearGround(e.getTo());

                    boolean lastOnGround = this.lastOnGround;
                    this.lastOnGround = onGround;

                    boolean LastLastOnGround = this.LastLastOnGround;
                    this.LastLastOnGround = lastOnGround;

                    double lastTickY = this.lastTickY;
                    this.lastTickY = y;

                    double difference = lastTickY - y;

                    if (onGround && lastOnGround && LastLastOnGround) {
                        if(isOnIce(e.getTo())) return;
                        if(lastTickY == 0) return;
                        if (!isNearWater(e.getFrom()) && !isNearWater(e.getTo())) {
                            if(difference > 0.1){
                                if(difference < plugin.getConfig().getDouble("checks.speedd.maxMove") || isRoughlyEqual(difference, 0.3993)){
                                    //if(difference == 0.0625) return;
                                    //if(isRoughlyEqual(difference, 0.0784)) return;
                                    //if(isRoughlyEqual(difference, 0.0465)) return;
                                    //if(isRoughlyEqual(difference, 0.0155)) return;
                                    //if(isRoughlyEqual(difference, 0.0358)) return;
                                    //if(isRoughlyEqual(difference, 0.0415)) return;
                                    //if(isRoughlyEqual(difference, 0.0275)) return;
                                    if(e.getTo().getBlock().getType() == Material.STATIONARY_WATER) return;
                                    if (plugin.getConfig().getBoolean("checks.speedd.cancelMovement")) {
                                        e.setCancelled(true);
                                    }
                                    p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Speed (D) &6Movement: &7" + difference + " &6Regular: &7>" + plugin.getConfig().getDouble("checks.speedd.maxMove")));
                                    plugin.getFlagManager().violate(p, FlagManager.check.SPEEDD);
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

    public boolean isRoughlyEqual(double d1, double d2){
        return Math.abs(d1 - d2) < 0.001;
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

    public boolean isOnLadder(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.1, z).getBlock().getType() == Material.LADDER){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnIce(Location location){
        double expand = 0.3;
        double vertical = -3;
        double x = -expand;
        double z = -expand;
        while(x <= expand){
            while(z <= expand){
                while(vertical < 0){
                    if(location.clone().add(x, vertical, z).getBlock().getType() == Material.ICE || location.clone().add(x, vertical, z).getBlock().getType() == Material.PACKED_ICE){
                        return true;
                    }
                    vertical = vertical + 0.3D;
                }
                z = z + 0.3;
            }
            x = x + 0.3;
        }
        return false;
    }
}
