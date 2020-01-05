package me.Serophots.AntiCheat.Checks.Movement.FastLadder;

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

public class FastLadderA implements Listener {
    boolean lastOnGround, lastLastOnGround;
    double lastY, lastLastY;

    private Main plugin;
    public FastLadderA(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handle(PlayerMoveEvent e){
        if(plugin.getConfig().getBoolean("checks.fastladdera.enabled")) {
            if (e.getFrom().getBlock().getType() == Material.LADDER) {
                Player p = e.getPlayer();
                if (p.getGameMode() != GameMode.CREATIVE && !p.getAllowFlight()) {
                    double distY = e.getTo().getY() - e.getFrom().getY();

                    boolean isOnGround = isNearGround(e.getTo());
                    double y = e.getTo().getY();

                    boolean lastOnGround = this.lastOnGround;
                    this.lastOnGround = isOnGround;

                    double lastY = this.lastY;
                    this.lastY = y;

                    boolean lastLastOnGround = this.lastLastOnGround;
                    this.lastLastOnGround = lastOnGround;

                    double lastLastY = this.lastLastY;
                    this.lastLastY = lastY;

                    if(!isOnGround && !lastOnGround & !lastLastOnGround) {
                        if (distY > 0) {
                            if (distY > plugin.getConfig().getDouble("checks.fastladdera.yLimit")) {
                                if (plugin.getConfig().getBoolean("checks.fastladdera.cancelMovement")) {
                                    e.setCancelled(true);
                                }
                                p.sendMessage(utils.chat("&8[&6AntiCheat&8] &6Check: &7Fast Ladder (A) &6Movement: &7" + distY + " &6Regular: &70.1167"));
                                plugin.getFlagManager().violate(p, FlagManager.check.FASTLADDERA);
                            }
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
                if(location.clone().add(x, -1, z).getBlock().getType() != Material.AIR && location.clone().add(x, -1, z).getBlock().getType() != Material.LADDER){
                    return true;
                }
            }
        }
        return false;
    }
}
