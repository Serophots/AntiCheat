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

public class FlyC implements Listener {
    List<Player> disbaledPlayers = new ArrayList<>();
    boolean lastOnGround, lastLastOnGround;
    int threshhold = 5;
    int counter = 0;

    private Main plugin;
    public FlyC(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.flyc.enabled")) {
            Player p = e.getPlayer();
            if (!disbaledPlayers.contains(p)) {
                if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                    double distY = e.getTo().getY() - e.getFrom().getY();

                    boolean onGround = isNearGround(e.getTo());

                    boolean lastOnGround = this.lastOnGround;
                    this.lastOnGround = onGround;

                    boolean lastLastOnGround = this.lastLastOnGround;
                    this.lastLastOnGround = lastOnGround;

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

                    if (!onGround && !lastOnGround && !lastLastOnGround && !e.getPlayer().isInsideVehicle()) {
                        if (threshhold != 0) {
                            threshhold -= 1;
                            if (distY > 0) {
                                counter += 1;
                            }
                        } else {
                            if (counter > plugin.getConfig().getInt("checks.flyc.maxCounter")) {
                                if(plugin.getConfig().getBoolean("checks.flyc.cancelMovement")) {
                                    e.setCancelled(true);
                                }
                                for(int i = 0; i < 7 - counter; i++) {
                                    p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Fly (C) &6Counter: &7" + counter + " &6Regular: &77"));
                                    plugin.getFlagManager().violate(p, FlagManager.check.FLYC);
                                }
                            }
                            threshhold = 5;
                            counter = 0;
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
        if(location.clone().add(0, -0.5001, 0).getBlock().getType() != Material.AIR){
            return true;
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
