package com.cactt4ck.qsui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QsUITabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String lbl, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pshop")) {
            if (args.length == 1)
                return new ArrayList<String>(Arrays.asList("setwarp", "delwarp"));
        }
        return new ArrayList<String>();
    }
}
