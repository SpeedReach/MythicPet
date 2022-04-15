package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.pet.PetDirectory;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand{
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("mythicpet.admin")){
            for(String key: PetDirectory.getModels()){
                sender.sendMessage(key+" : "+ PetDirectory.getModel(key).display);
            }
        }
        else{
            sender.sendMessage(Message.NoPermission);
        }
    }

    @Override
    public String name() {
        return "list";
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
    public java.util.List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
