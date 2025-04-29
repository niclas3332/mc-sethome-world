package xyz.niclas.setHomeNew;

import org.bukkit.plugin.java.JavaPlugin;


import org.bukkit.plugin.java.JavaPlugin;
import xyz.niclas.setHomeNew.commands.DelHomeCommand;
import xyz.niclas.setHomeNew.commands.HomeCommand;
import xyz.niclas.setHomeNew.commands.SetHomeCommand;

public class SetHomeNew extends JavaPlugin {
    private HomeManager homeManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.homeManager = new HomeManager(this);
        this.getCommand("sethome").setExecutor(new SetHomeCommand(this.homeManager));
        this.getCommand("home").setExecutor(new HomeCommand(this.homeManager));
        this.getCommand("delhome").setExecutor(new DelHomeCommand(this.homeManager));
        this.getCommand("home").setTabCompleter(new HomeTabCompleter(this.homeManager));
        this.getCommand("delhome").setTabCompleter(new HomeTabCompleter(this.homeManager));
    }


    @Override

    public void onDisable() {
        this.homeManager.saveHomes();
    }
}
