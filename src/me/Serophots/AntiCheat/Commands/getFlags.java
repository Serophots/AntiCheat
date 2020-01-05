package me.Serophots.AntiCheat.Commands;

import me.Serophots.AntiCheat.Flags.FlagManager;
import me.Serophots.AntiCheat.Main;
import me.Serophots.AntiCheat.utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class getFlags implements CommandExecutor {
    private Main plugin;
    private FileConfiguration messages;
    public getFlags(Main plugin){
        this.plugin = plugin;
        messages = plugin.getConfigManager().getMessages();
        Bukkit.getPluginCommand("getflags").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("anticheat.command.getflags")){
                if(args.length == 1) {
                    Player t = Bukkit.getPlayer(args[0]);
                    FlagManager flagManager = plugin.getFlagManager();
                    if(t == null){p.sendMessage(utils.chat(messages.getString("messages.invalidPlayer")));return false;}
                    List<Object> list = flagManager.violationData.get(t.getUniqueId());
                    if(list == null ||list.isEmpty()){
                        p.sendMessage(utils.chat(messages.getString("messages.getflags.noFlags")));
                    }else{
                        int FASTLADDERA = 0;
                        int FLYA = 0;
                        int FLYB = 0;
                        int FLYC = 0;
                        int JESUSA = 0;
                        int JESUSB = 0;
                        int NOFALLA = 0;
                        int SPEEDA = 0;
                        int SPEEDB = 0;
                        int SPEEDC = 0;
                        int SPEEDD = 0;
                        for(Object o : list){
                            try{
                                List<Object> l2 = (List<Object>) o;
                                FlagManager.check check = (FlagManager.check) l2.get(0);
                                Long time = (Long) l2.get(1);
                                if(time + 10000 > System.currentTimeMillis()){
                                    if(check == FlagManager.check.FASTLADDERA){
                                        FASTLADDERA+=1;
                                    }
                                    if(check == FlagManager.check.FLYA){
                                        FLYA+=1;
                                    }
                                    if(check == FlagManager.check.FLYB){
                                        FLYB+=1;
                                    }
                                    if(check == FlagManager.check.FLYC){
                                        FLYC+=1;
                                    }
                                    if(check == FlagManager.check.JESUSA){
                                        JESUSA+=1;
                                    }
                                    if(check == FlagManager.check.JESUSB){
                                        JESUSB+=1;
                                    }
                                    if(check == FlagManager.check.NOFALLA){
                                        NOFALLA+=1;
                                    }
                                    if(check == FlagManager.check.SPEEDA){
                                        SPEEDA+=1;
                                    }
                                    if(check == FlagManager.check.SPEEDB){
                                        SPEEDB+=1;
                                    }
                                    if(check == FlagManager.check.SPEEDC){
                                        SPEEDC+=1;
                                    }
                                    if(check == FlagManager.check.SPEEDD){
                                        SPEEDD+=1;
                                    }
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        List<String> message = new ArrayList<>();
                        for(String s1 : messages.getStringList("messages.getflags.header")){
                            message.add(s1.replace("%player%", t.getDisplayName()));
                        }
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Fast Ladder (A)").replace("%violations%", Integer.toString(FASTLADDERA)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Fly (A)").replace("%violations%", Integer.toString(FLYA)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Fly (B)").replace("%violations%", Integer.toString(FLYB)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Fly (C)").replace("%violations%", Integer.toString(FLYC)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Jesus (A)").replace("%violations%", Integer.toString(JESUSA)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Jesus (B)").replace("%violations%", Integer.toString(JESUSB)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "No Fall (A)").replace("%violations%", Integer.toString(NOFALLA)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Speed (A)").replace("%violations%", Integer.toString(SPEEDA)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Speed (B)").replace("%violations%", Integer.toString(SPEEDB)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Speed (C)").replace("%violations%", Integer.toString(SPEEDC)));
                        message.add(messages.getString("messages.getflags.checkDisplay").replace("%check%", "Speed (D)").replace("%violations%", Integer.toString(SPEEDD)));

                        String message1 = "";

                        for(String s1 : message){
                            String prefix = messages.getString("messages.getflags.prefix") + " ";
                             message1+= prefix + s1 + "\n";
                        }
                        p.sendMessage(utils.chat(message1));
                    }
                }else{
                    p.sendMessage(utils.chat(messages.getString("messages.incorrectArgs")));
                }
            }else{
                p.sendMessage(utils.chat(messages.getString("messages.noPerms")));
            }
        }
        return false;
    }
}
