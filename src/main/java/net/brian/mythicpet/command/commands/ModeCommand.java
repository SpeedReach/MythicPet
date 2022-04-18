package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.player.Mode;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.brian.mythicpet.MythicPets.*;

public class ModeCommand extends SubCommand{

    MythicPets plugin = inst();

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length < 1) return;
        if(!sender.hasPermission("mythicpet.player")) {
            sender.sendMessage(Message.NoPermission);
            return;
        }
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!isLoaded(player)){

                return;
            }
            PlayerPetProfile.get(player.getUniqueId()).ifPresentOrElse(data->{
                if(args[1].equalsIgnoreCase(Mode.FOLLOW)){
                    data.setMode(Mode.FOLLOW);
                    sender.sendMessage(Message.SwitchedToFollow);
                }
                if(args[1].equalsIgnoreCase(Mode.DEFENSE)){
                    data.setMode(Mode.DEFENSE);
                    sender.sendMessage(Message.SwitchedToDefense);
                }
                if(args[1].equalsIgnoreCase(Mode.ATTACK)){
                    data.setMode(Mode.ATTACK);
                    sender.sendMessage(Message.SwtichedToAttack);
                }
            },()->{
                sender.sendMessage(Message.DataLoading);
            });
        }
    }

    @Override
    public String name() {
        return "mode";
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[]{"m"};
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(Mode.DEFENSE);
        arrayList.add(Mode.FOLLOW);
        arrayList.add(Mode.ATTACK);
        if(args.length==2) return arrayList;
        return null;
    }
}
