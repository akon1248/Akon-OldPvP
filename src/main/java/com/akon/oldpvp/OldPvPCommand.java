package com.akon.oldpvp;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class OldPvPCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("oldpvp")) {
            if (args.length == 1 && args[0].equals("reload")) {
                OldPvP.getInstance().reloadConfig();
                sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "Akon's OldPvP" + ChatColor.AQUA + "] " + ChatColor.YELLOW + "コンフィグをリロードしました");
            } else {
                sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "Akon's OldPvP" + ChatColor.AQUA + "] " + ChatColor.RED + "/oldpvp reloadでコンフィグをリロードします");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> result = Lists.newArrayList();
        if (command.getName().equals("oldpvp") && args.length == 1 && "reload".startsWith(args[0].toLowerCase())) {
            result.add("reload");
        }
        return result;
    }

}
