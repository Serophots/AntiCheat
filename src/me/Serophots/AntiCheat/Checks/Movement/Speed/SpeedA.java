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

public class SpeedA implements Listener {
    List<Player> disbaledPlayers = new ArrayList<>();
    private double lastDist;
    private boolean lastOnGround, lastLastOnGround;

    private Main plugin;

    public SpeedA(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void event(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.speeda.enabled")) {
            Player p = e.getPlayer();
            if (!disbaledPlayers.contains(p)) {
                if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                    double distX = e.getTo().getX() - e.getFrom().getX();
                    double distZ = e.getTo().getZ() - e.getFrom().getZ();
                    double dist = (distX * distX) + (distZ * distZ);
                    this.lastDist = dist;

                    boolean onGround = isNearGround(e.getTo());
                    boolean isOnIce = isOnIce(e.getTo());

                    if(isOnIce){
                        disbaledPlayers.add(p);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                disbaledPlayers.remove(p);
                            }
                        }, 30);
                        return;
                    }
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

                    boolean lastOnGround = this.lastOnGround;
                    this.lastOnGround = onGround;

                    boolean lastLastOnGroud = this.lastLastOnGround;
                    this.lastLastOnGround = lastOnGround;

                    float friction = 0.91F;
                    double shiftedLastDist = lastDist * friction;
                    double equalness = dist - shiftedLastDist;

                    if (!onGround && !lastOnGround && !lastLastOnGroud) {
                        if (equalness >= 0.0115 && !e.getPlayer().isInsideVehicle()) {
                            if(plugin.getConfig().getBoolean("checks.speeda.cancelMovement")) {
                                e.setCancelled(true);
                            }
                            p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Speed (A) &6Movement: &7" + equalness + " &6Regular: &70.008"));
                            plugin.getFlagManager().violate(p, FlagManager.check.SPEEDA);
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
            }, 20);
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
