package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetLevel extends SubCommand{


    /*
    /mythicpet setlevel @player @level
     */
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("mythicpet.admin")) {
            sender.sendMessage(Message.NoPermission);
            return;
        }
            if(args.length>=3){
            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                sender.sendMessage("Can't find player");
                return;
            }
            PlayerPetProfile.get(player.getUniqueId())
                    .flatMap(PlayerPetProfile::getCurrentPet)
                    .ifPresentOrElse(pet -> {
                        pet.setLevel(Integer.parseInt(args[2]));
                        sender.sendMessage(("Successfully set players ? to "+args[2]).replace("?",pet.getPetType().getDisplayName()));
                    }, ()->{
                        sender.sendMessage("Player doesn't have active pet");
                    });
        }
    }

    @Override
    public String name() {
        return "setlevel";
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
