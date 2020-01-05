package me.Serophots.AntiCheat.Checks.Movement.Fly;

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

public class FlyA implements Listener {

    List<Player> disbaledPlayers = new ArrayList<>();

    private double lastDistY;
    boolean lastOnGround, lastLastOnGround;

    private Main plugin;
    public FlyA(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.flya.enabled")) {
            Player p = e.getPlayer();
            if (!disbaledPlayers.contains(p)) {
                if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                    if(e.getTo().getX() - e.getFrom().getX() == 0) return;
                    if(e.getTo().getZ() - e.getFrom().getZ() == 0) return;

                    if(isOnSlime(e.getTo())) {
                        disbaledPlayers.add(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                disbaledPlayers.remove(p);
                            }
                        }, 60);
                        return;
                    }

                    double distY = e.getTo().getY() - e.getFrom().getY();
                    double lastDisY = this.lastDistY;
                    this.lastDistY = distY;

                    double predictedDist = (lastDisY - 0.08D) * 0.9800000190734863D;

                    boolean onGround = isNearGround(e.getTo());

                    boolean lastOnGround = this.lastOnGround;
                    this.lastOnGround = onGround;

                    boolean lastLastOnGround = this.lastLastOnGround;
                    this.lastLastOnGround = lastOnGround;


                    if (!onGround && !lastOnGround && !lastLastOnGround && Math.abs(predictedDist) >= 0.005D) {
                        if(!isInWeb(e.getTo())) {
                            if (!isRoughlyEqual(distY, predictedDist) && !e.getPlayer().isInsideVehicle() && distY != 0) {
                                if (plugin.getConfig().getBoolean("checks.flya.cancelMovement")) {
                                    e.setCancelled(true);
                                }
                                p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Fly (A) &6Movement: &7" + distY + " &6Predicted: &7" + predictedDist));
                                plugin.getFlagManager().violate(p, FlagManager.check.FLYA);
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

    public boolean isRoughlyEqual(double d1, double d2){
        return Math.abs(d1 - d2) < 0.001;
    }

    public boolean isNearGround(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR){
                    return true;
                }
                if(location.clone().add(x, -0.1, z).getBlock().getType() != Material.AIR){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInWeb(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, 0, z).getBlock().getType() == Material.WEB || location.clone().add(x, +1, z).getBlock().getType() == Material.WEB  || location.clone().add(x, +2.01, z).getBlock().getType() == Material.WEB){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnSlime(Location location){
        try {
            double expand = 0.3;
            for (double x = -expand; x <= expand; x += expand) {
                for (double z = -expand; z <= expand; z += expand) {
                    if (location.clone().add(x, -1, z).getBlock().getType() == Material.SLIME_BLOCK || location.clone().add(x, +2, z).getBlock().getType() == Material.SLIME_BLOCK || location.clone().add(x, +3, z).getBlock().getType() == Material.SLIME_BLOCK) {
                        return true;
                    }
                }
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }
}
