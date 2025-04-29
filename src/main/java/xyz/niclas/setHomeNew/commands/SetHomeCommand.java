package xyz.niclas.setHomeNew.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.niclas.setHomeNew.HomeManager;

public class SetHomeCommand implements CommandExecutor {
    private final HomeManager homeManager;

    public SetHomeCommand(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("sethome.sethome")) {
            sender.sendMessage("§cYou don't have permission to use this command");
        }

        if (sender instanceof Player && args.length == 1) {
            Player player = (Player)sender;
            String name = args[0];
            if (this.homeManager.getHomes(player).size() >= this.homeManager.getMaxHomes()) {
                player.sendMessage("§cYou can't have more than " + this.homeManager.getMaxHomes() + " homes");
                return true;
            } else {
                if (this.homeManager.addHome(player, name)) {
                    player.sendMessage("§aYou have created a home with the name " + name);
                } else {
                    player.sendMessage("§cYou already have a home with that name");
                }

                return true;
            }
        } else {
            sender.sendMessage("§cThis command can only be used by players with a home name");
            return true;
        }
    }
}
