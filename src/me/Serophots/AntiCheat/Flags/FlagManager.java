package me.Serophots.AntiCheat.Flags;

import me.Serophots.AntiCheat.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FlagManager {

    public static HashMap<UUID, List<Object>> violationData = new HashMap<UUID, List<Object>>();

    private static Main plugin;
    public FlagManager(Main plugin){
        this.plugin = plugin;
    }
    public enum check{
        FASTLADDERA,
        FLYA,
        FLYB,
        FLYC,
        JESUSA,
        JESUSB,
        JESUSC,
        NOFALLA,
        SPEEDA,
        SPEEDB,
        SPEEDC,
        SPEEDD
    }

    public static void violate(Player p, FlagManager.check check){
        //Adding violation
        if(!violationData.containsKey(p.getUniqueId())){
            List<Object> list = new ArrayList<>();
            violationData.put(p.getUniqueId(), list);
        }
        List<Object> list = violationData.get(p.getUniqueId());
        List<Object> list2 = new ArrayList<>();
        list2.add(check);
        list2.add(System.currentTimeMillis());
        list.add(list2);

        //Checking violation thresholds
        int FASTLADDERA = 0;
        int FLYA = 0;
        int FLYB = 0;
        int FLYC = 0;
        int JESUSA = 0;
        int JESUSB = 0;
        int JESUSC = 0;
        int NOFALLA = 0;
        int SPEEDA = 0;
        int SPEEDB = 0;
        int SPEEDC = 0;
        int SPEEDD = 0;
        for(Object o : list){
            try{
                List<Object> l2 = (List<Object>) o;
                FlagManager.check flagCheck = (FlagManager.check) l2.get(0);
                Long time = (Long) l2.get(1);
                if(time + 10000 > System.currentTimeMillis()){
                    if(flagCheck == FlagManager.check.FASTLADDERA){
                        FASTLADDERA+=1;
                    }
                    if(flagCheck == FlagManager.check.FLYA){
                        FLYA+=1;
                    }
                    if(flagCheck == FlagManager.check.FLYB){
                        FLYB+=1;
                    }
                    if(flagCheck == FlagManager.check.FLYC){
                        FLYC+=1;
                    }
                    if(flagCheck == FlagManager.check.JESUSA){
                        JESUSA+=1;
                    }
                    if(flagCheck == FlagManager.check.JESUSB){
                        JESUSB+=1;
                    }
                    if(flagCheck == FlagManager.check.JESUSC){
                        JESUSC+=1;
                    }
                    if(flagCheck == FlagManager.check.NOFALLA){
                        NOFALLA+=1;
                    }
                    if(flagCheck == FlagManager.check.SPEEDA){
                        SPEEDA+=1;
                    }
                    if(flagCheck == FlagManager.check.SPEEDB){
                        SPEEDB+=1;
                    }
                    if(flagCheck == FlagManager.check.SPEEDC){
                        SPEEDC+=1;
                    }
                    if(flagCheck == FlagManager.check.SPEEDD){
                        SPEEDD+=1;
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        if(FASTLADDERA > plugin.getConfig().getInt("checks.fastladdera.threshold")){
            p.kickPlayer("Fast Ladder (A)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.fastladdera.command"));
        }
        if(FLYA > plugin.getConfig().getInt("checks.flya.threshold")){
            p.kickPlayer("Fly (A)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.flya.command"));
        }
        if(FLYB > plugin.getConfig().getInt("checks.flyb.threshold")){
            p.kickPlayer("Fly (B)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.flyb.command"));
        }
        if(FLYC > plugin.getConfig().getInt("checks.flyc.threshold")){
            p.kickPlayer("Fly (C)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.flyc.command"));
        }
        if(JESUSA > plugin.getConfig().getInt("checks.jesusa.threshold")){
            p.kickPlayer("Jesus (A)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.jesusa.command"));
        }
        if(JESUSB > plugin.getConfig().getInt("checks.jesusb.threshold")){
            p.kickPlayer("Jesus (B)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.jesusb.command"));
        }
        if(JESUSC > plugin.getConfig().getInt("checks.jesusc.threshold")){
            p.kickPlayer("Jesus (C)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.jesusc.command"));
        }
        if(NOFALLA > plugin.getConfig().getInt("checks.nofalla.threshold")){
            p.kickPlayer("No Fall (A)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.nofalla.command"));
        }
        if(SPEEDA > plugin.getConfig().getInt("checks.speeda.threshold")){
            p.kickPlayer("Speed (A)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.speeda.command"));
        }
        if(SPEEDB > plugin.getConfig().getInt("checks.speedb.threshold")){
            p.kickPlayer("Speed (B)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.speedb.command"));
        }
        if(SPEEDC > plugin.getConfig().getInt("checks.speedc.threshold")){
            p.kickPlayer("Speed (C)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.speedc.command"));
        }
        if(SPEEDD > plugin.getConfig().getInt("checks.speedd.threshold")){
            p.kickPlayer("Speed (D)");
            violationData.remove(p.getUniqueId());
            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.getConfig().getString("checks.speedd.command"));
        }
    }
}
