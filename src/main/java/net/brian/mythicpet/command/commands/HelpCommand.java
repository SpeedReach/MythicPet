package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.config.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends SubCommand{
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("mythicpet.player")) {
            sender.sendMessage(Message.NoPermission);
            return;
        }
        sender.sendMessage(Message.Help);
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[]{"h"};
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
