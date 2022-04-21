package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.utils.PetDirectory;
import net.brian.mythicpet.pets.PetImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GivePet extends SubCommand{
    /**
     * /mythicpet givepet @player @pet<br>
     */
    MythicPets plugin = MythicPets.inst();

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("mythicpet.admin")){
            sender.sendMessage(Message.NoPermission);
            return;
        }
        if(args.length>2){
            if(PetDirectory.getModels().contains(args[2])){
                Player player = Bukkit.getPlayer(args[1]);
                Pet pet = new PetImpl(PetDirectory.getModel(args[2]));
                if(args.length> 3){
                    pet.setLevel(Integer.parseInt(args[3]));
                }
                player.getInventory().addItem(pet.getIcon(player));
            }
        }
    }

    @Override
    public String name() {
        return "givePet";
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
        List<String> list = new ArrayList<>();
        if(args.length == 3) {
            list.addAll(PetDirectory.getModels());
            return list;
        }
        return null;
    }
}
