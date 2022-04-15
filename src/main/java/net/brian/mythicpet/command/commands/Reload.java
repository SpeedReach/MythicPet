package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.config.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Reload extends SubCommand{


    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("mythicpet.admin")){
            MythicPet.reload();
            sender.sendMessage("Config reloaded");
            return;
        }
        sender.sendMessage(Message.NoPermission);
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
