package xyz.niclas.setHomeNew.commands;


import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.niclas.setHomeNew.Home;
import xyz.niclas.setHomeNew.HomeManager;
import xyz.niclas.setHomeNew.SetHomeNew;

public class HomeCommand implements CommandExecutor {
    private final HomeManager homeManager;
    private final SetHomeNew plugin;

    public HomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
        this.plugin = homeManager.getPlugin();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("sethome.home")) {
            sender.sendMessage("§cYou don't have permission to use this command");
        }

        if (sender instanceof Player && args.length == 1) {
            final Player player = (Player)sender;
            final String name = args[0];
            final Home home = this.homeManager.getHome(player, name);
            if (home == null) {
                player.sendMessage("§cYou have no home with that name");
                return true;
            } else {
                (new BukkitRunnable() {
                    int seconds;
                    final Location startLocation;

                    {
                        this.seconds = HomeCommand.this.homeManager.getCooldown();
                        this.startLocation = player.getLocation();
                    }

                    public void run() {
                        if (this.seconds > 0) {
                            player.sendTitle("", "§e" + this.seconds + " seconds left to teleport", 0, 20, 0);
                            --this.seconds;
                            if (HomeCommand.this.homeManager.isCancelOnMove() && !player.getLocation().equals(this.startLocation)) {
                                this.cancel();
                                player.sendTitle("", "§cTeleportation canceled!", 0, 20, 0);
                            }
                        } else {
                            player.teleport(home.getLocation());
                            player.sendMessage("§aYou have teleported to your home with the name " + name);
                            this.cancel();
                        }

                    }
                }).runTaskTimer(this.plugin, 0L, 20L);
                return true;
            }
        } else {
            sender.sendMessage("§cThis command can only be used by players with a home name");
            return true;
        }
    }
}
