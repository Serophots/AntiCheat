package me.Serophots.AntiCheat;

import me.Serophots.AntiCheat.Checks.Movement.Fly.*;
import me.Serophots.AntiCheat.Checks.Movement.Jesus.*;
import me.Serophots.AntiCheat.Checks.Movement.FastLadder.*;
import me.Serophots.AntiCheat.Checks.Movement.NoFall.*;
import me.Serophots.AntiCheat.Checks.Movement.Speed.*;
import me.Serophots.AntiCheat.Flags.FlagManager;
import me.Serophots.AntiCheat.Commands.getFlags;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private FlagManager flagManager;
    private ConfigManager configManager;
    @Override
    public void onEnable(){
        loadConfig();
        getLogger().info("An anticheat made by Serophots!");

        configManager = new ConfigManager(this);
        configManager.setup();

        new getFlags(this);

        new SpeedA(this);
        new SpeedB(this);
        new SpeedC(this);
        new SpeedD(this);

        new FlyA(this);
        new FlyB(this);
        new FlyC(this);

        new FastLadderA(this);

        new NoFallA(this);

        new JesusA(this);
        new JesusB(this);
        new JesusC(this);

        flagManager = new FlagManager(this);
    }

    public void loadConfig(){
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public FlagManager getFlagManager() {
        return flagManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
