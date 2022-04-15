package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Exp extends SubCommand{

    /**
     * /mythicpet exp give <player> <amount>
     */
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("mythicpet.admin")){
            if(args.length < 4){
                sender.sendMessage("Wrong command usage");
                return;
            }
            Player player = Bukkit.getPlayer(args[2]);
            if(player == null){
                sender.sendMessage("can't find player");
                return;
            }
            MythicPet.getPlayer(player.getUniqueId()).ifPresent(data->{
                data.addExp(Integer.parseInt(args[3]));
                sender.sendMessage(ChatColor.GREEN +"Gave pet exp "+args[3]+" to "+args[2]);
            });
        }
        else sender.sendMessage(Message.NoPermission);
    }

    @Override
    public String name() {
        return "exp";
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
