package net.brian.mythicpet.command.commands;

import net.brian.mythicpet.petinteraction.SignalItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BindSignalItem extends SubCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length < 2) return;
        if(sender instanceof Player){
            if(sender.hasPermission("mythicpet.bindsignal")){
                Player player = (Player) sender;
                SignalItem.apply(player.getInventory().getItemInMainHand(),args[1]);
            }
        }
    }

    @Override
    public String name() {
        return "BindSignal";
    }

    @Override
    public String info() {
        return "";
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
