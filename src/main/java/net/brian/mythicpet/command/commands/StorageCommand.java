package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.List;

public class StorageCommand extends SubCommand{
    // mythicpet open <player>
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("mythicpet.player")) {
            sender.sendMessage(Message.NoPermission);
            return;
        }
        if(args.length==1 && sender instanceof Player){
            Player player = (Player) sender;
            if(MythicPets.isLoaded(player)){
                StorageManager.openPlayer(player,1);
            }
            else{
                player.sendMessage(Message.DataLoading);
            }
            return;
        }
        if(args.length>=2){
            if(!sender.hasPermission("mythicpet.admin")) {
                sender.sendMessage(Message.NoPermission);
                return;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null) {
                sender.sendMessage(ChatColor.RED +"Can't find player");
                return;
            }
            if(MythicPets.isLoaded(player)){
                StorageManager.openPlayer(player,1);
            }
            else{
                player.sendMessage(Message.DataLoading);
            }
            return;
        }
        sender.sendMessage(ChatColor.RED+"Wrong command usage");
    }

    @Override
    public String name() {
        return "open";
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
