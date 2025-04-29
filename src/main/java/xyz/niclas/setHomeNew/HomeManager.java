package xyz.niclas.setHomeNew;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class HomeManager {
    private final int cooldown;
    private final int maxHomes;
    private final boolean cancelOnMove;
    private final SetHomeNew plugin;
    private final FileConfiguration config;
    private final Map<UUID, List<Home>> homes = new HashMap();

    public HomeManager(SetHomeNew plugin) {
        this.cooldown = plugin.getConfig().getInt("cooldown");
        this.maxHomes = plugin.getConfig().getInt("max-homes");
        this.cancelOnMove = plugin.getConfig().getBoolean("cancel-on-move");
        this.plugin = plugin;
        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "homes.yml"));
        this.loadHomes();
    }

    public boolean isCancelOnMove() {
        return this.cancelOnMove;
    }

    public int getMaxHomes() {
        return this.maxHomes;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public SetHomeNew getPlugin() {
        return this.plugin;
    }

    public Home getHome(Player player, String name) {
        UUID uuid = player.getUniqueId();
        List<Home> homeList = (List)this.homes.get(uuid);
        if (homeList != null) {
            for(Home home : homeList) {
                if (home.getName().equalsIgnoreCase(name)) {
                    return home;
                }
            }
        }

        return null;
    }

    public List<Home> getHomes(Player player) {
        UUID uuid = player.getUniqueId();
        return (List)this.homes.getOrDefault(uuid, new ArrayList());
    }

    public void loadHomes() {
        this.config.getKeys(false).forEach((uuidString) -> {
            UUID uuid = UUID.fromString(uuidString);
            List<Home> homeList = new ArrayList();
            this.config.getConfigurationSection(uuidString).getKeys(false).forEach((name) -> {
                Location location = this.loadLocation(this.config, uuidString, name);
                Home home = new Home(name, location);
                homeList.add(home);
            });
            this.homes.put(uuid, homeList);
        });
    }

    private Location loadLocation(FileConfiguration config, String uuid, String name) {
        String worldName = config.getString(uuid + "." + name + ".world");
        double x = config.getDouble(uuid + "." + name + ".x");
        double y = config.getDouble(uuid + "." + name + ".y");
        double z = config.getDouble(uuid + "." + name + ".z");
        float yaw = (float)config.getDouble(uuid + "." + name + ".yaw");
        float pitch = (float)config.getDouble(uuid + "." + name + ".pitch");
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public void saveHomes() {
        this.homes.forEach((uuid, homeList) -> homeList.forEach((home) -> {
            this.config.set(uuid.toString() + "." + home.getName() + ".world", home.getLocation().getWorld().getName());
            this.config.set(uuid + "." + home.getName() + ".x", home.getLocation().getX());
            this.config.set(uuid + "." + home.getName() + ".y", home.getLocation().getY());
            this.config.set(uuid + "." + home.getName() + ".z", home.getLocation().getZ());
            this.config.set(uuid + "." + home.getName() + ".yaw", home.getLocation().getYaw());
            this.config.set(uuid + "." + home.getName() + ".pitch", home.getLocation().getPitch());
        }));

        try {
            this.config.save(new File(this.plugin.getDataFolder(), "homes.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean addHome(Player player, String name) {
        UUID uuid = player.getUniqueId();
        List<Home> homeList = (List)this.homes.getOrDefault(uuid, new ArrayList());

        for(Home home : homeList) {
            if (home.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        Location location = player.getLocation();
        Home newHome = new Home(name, location);
        homeList.add(newHome);
        this.homes.put(uuid, homeList);
        return true;
    }

    public boolean removeHome(Player player, String name) {
        UUID uuid = player.getUniqueId();
        List<Home> homeList = (List)this.homes.get(uuid);
        if (homeList != null) {
            for(Home home : homeList) {
                if (home.getName().equalsIgnoreCase(name)) {
                    homeList.remove(home);
                    this.config.set(uuid + "." + name, (Object)null);
                    return true;
                }
            }
        }

        return false;
    }
}
