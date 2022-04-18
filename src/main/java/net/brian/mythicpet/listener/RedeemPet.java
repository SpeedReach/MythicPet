package net.brian.mythicpet.listener;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.pet.PetImpl;
import net.brian.mythicpet.utils.ItemReader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class RedeemPet implements Listener {


    @EventHandler
    public void onPlace(PlayerInteractEvent event){
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(ItemReader.isPet(event.getItem())){
                Player player = event.getPlayer();
                event.setCancelled(true);
                MythicPets.getPlayer(event.getPlayer().getUniqueId()).ifPresent(playerData->{
                    if(playerData.pets.size() < playerData.maxPage()*28){

                        if(ItemReader.isPet(player.getInventory().getItemInMainHand())){
                            ItemStack item = player.getInventory().getItemInMainHand();
                            playerData.pets.add(new PetImpl(item));
                            item.setAmount(item.getAmount()-1);
                            player.getInventory().setItemInMainHand(item);
                        }
                        else if(ItemReader.isPet(player.getInventory().getItemInOffHand())){
                            ItemStack item = player.getInventory().getItemInOffHand();
                            playerData.pets.add(new PetImpl(item));
                            item.setAmount(item.getAmount()-1);
                            player.getInventory().setItemInOffHand(item);
                        }
                    }
                    else{
                        player.sendMessage(Message.StorageFull);
                    }
                });
            }
        }

    }


    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        if(ItemReader.isPet(event.getItem())){
            event.setCancelled(true);
        }
    }

}
