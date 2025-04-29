package xyz.niclas.setHomeNew.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.niclas.setHomeNew.HomeManager;

public class DelHomeCommand implements CommandExecutor {
    private final HomeManager homeManager;

    public DelHomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("sethome.delhome")) {
            sender.sendMessage("§cYou don't have permission to use this command");
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players");
            return true;
        } else {
            Player player = (Player)sender;
            if (args.length != 1) {
                player.sendMessage("§cYou must enter a name for your home");
                return true;
            } else {
                String name = args[0];
                if (this.homeManager.removeHome(player, name)) {
                    player.sendMessage("§aYou have removed your home with the name " + name);
                } else {
                    player.sendMessage("§cYou have no home with that name");
                }

                return true;
            }
        }
    }
}