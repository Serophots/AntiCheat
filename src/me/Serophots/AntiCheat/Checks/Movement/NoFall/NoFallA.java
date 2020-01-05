package me.Serophots.AntiCheat.Checks.Movement.NoFall;

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

public class NoFallA implements Listener {

    boolean lastOnGround, lastLastOnGround;

    private Main plugin;
    public NoFallA(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.nofalla.enabled")) {
            Player p = e.getPlayer();
            if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {

                double dist = e.getFrom().getY() - e.getTo().getY();

                boolean onGround = isNearGround(e.getTo());

                boolean lastOnGround = this.lastOnGround;
                this.lastOnGround = onGround;

                boolean lastLastOnGround = this.lastLastOnGround;
                this.lastLastOnGround = lastOnGround;

                if (!onGround && !lastOnGround && !lastLastOnGround && dist != 0) {
                    if (p.isOnGround()) {
                        if(plugin.getConfig().getBoolean("checks.nofalla.cancelMovement")) {
                            e.setCancelled(true);
                        }
                        p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7No Fall (A)"));
                        plugin.getFlagManager().violate(p, FlagManager.check.NOFALLA);
                    }
                }
            }
        }
    }
    public boolean isNearGround(Location location){
        double expand = 0.3;
        for(double x = -expand; x <= expand; x+= expand){
            for(double z = -expand; z <= expand; z+= expand){
                if(location.clone().add(x, -0.7001, z).getBlock().getType() != Material.AIR){
                    return true;
                }
            }
        }
        if(location.getBlock().getType() == Material.AIR) {
            return false;
        }else{
            return true;
        }
    }
}
