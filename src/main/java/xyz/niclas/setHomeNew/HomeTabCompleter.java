package xyz.niclas.setHomeNew;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeTabCompleter implements TabCompleter {
    private final HomeManager homeManager;

    public HomeTabCompleter(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player && args.length == 1) {
            Player player = (Player)sender;
            String input = args[0].toLowerCase();
            return (List)this.homeManager.getHomes(player).stream().map(Home::getName).filter((name) -> name.toLowerCase().startsWith(input)).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
