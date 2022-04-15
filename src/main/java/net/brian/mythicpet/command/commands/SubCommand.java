package net.brian.mythicpet.command.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    public SubCommand() {

    }

    public abstract void onCommand(CommandSender sender,String[] args);

    public abstract String name();

    public abstract String info();

    public abstract String[] aliases();

    public abstract List<String> onTabComplete(CommandSender sender,String[] args);

}
