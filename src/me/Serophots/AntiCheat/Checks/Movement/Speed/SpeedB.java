package me.Serophots.AntiCheat.Checks.Movement.Speed;

import jdk.nashorn.internal.ir.Block;
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

public class SpeedB implements Listener {
    List<Player> disbaledPlayers = new ArrayList<>();
    boolean lastOnGround, lastLastOnGround;

    private Main plugin;
    public SpeedB(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.speedb.enabled")) {
            Player p = e.getPlayer();
            if (!disbaledPlayers.contains(p)) {
                if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                    Location aboveHead = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY() + 2, p.getLocation().getZ());
                    if (aboveHead.getBlock().getType() == Material.AIR) {
                        double distX = e.getTo().getX() - e.getFrom().getX();
                        double distZ = e.getTo().getZ() - e.getFrom().getZ();
                        double dist = (distX * distX) + (distZ * distZ);
                        double velocityX = p.getVelocity().getX();
                        double velocityZ = p.getVelocity().getZ();
                        double velocity = velocityX + velocityZ;

                        boolean onGround = isNearGround(e.getTo());

                        boolean lastOnGround = this.lastOnGround;
                        this.lastOnGround = onGround;

                        boolean lastLastOnGround = this.lastLastOnGround;
                        this.lastLastOnGround = lastOnGround;

                        if (onGround && lastOnGround && lastLastOnGround && velocity == 0 && !isOnStairs(e.getTo())) {
                            if(!isOnIce(e.getTo())) {
                                if (dist > plugin.getConfig().getDouble("checks.speedb.maxDist")) {
                                    if (e.getTo().getY() < (e.getTo().getY() + 1)) {
                                        if (isNearGround(p.getLocation())) {
                                            if (e.getTo().getY() < (Math.floor(e.getTo().getY()) + 0.419D) && !e.getPlayer().isInsideVehicle()) {
                                                if (plugin.getConfig().getBoolean("checks.speedb.cancelMovement")) {
                                                    e.setCancelled(true);
                                                }
                                                p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Speed (B) &6Movement: &7" + dist + " &6Regular: &70.107"));
                                                plugin.getFlagManager().violate(p, FlagManager.check.SPEEDB);
                                            }
                                        }
                                    }
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
        double expand = 0.5;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.5001, z).getBlock().getType() != Material.AIR){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnStairs(Location location){
        List<Material> stairs = new ArrayList<>();
        stairs.add(Material.WOOD_STAIRS);
        stairs.add(Material.COBBLESTONE_STAIRS);
        stairs.add(Material.BRICK_STAIRS);
        stairs.add(Material.SMOOTH_STAIRS);
        stairs.add(Material.NETHER_BRICK_STAIRS);
        stairs.add(Material.SANDSTONE_STAIRS);
        stairs.add(Material.SPRUCE_WOOD_STAIRS);
        stairs.add(Material.BIRCH_WOOD_STAIRS);
        stairs.add(Material.JUNGLE_WOOD_STAIRS);
        stairs.add(Material.QUARTZ_STAIRS);
        stairs.add(Material.ACACIA_STAIRS);
        stairs.add(Material.DARK_OAK_STAIRS);
        stairs.add(Material.RED_SANDSTONE_STAIRS);
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                for(Material m : stairs){
                    if(location.clone().add(x, -0.5001, z).getBlock().getType() == m){
                        return true;
                    }
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
