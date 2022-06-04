package net.brian.mythicpet.storage;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.config.InteractionGUIConfig;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.utils.MountPetUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InteractionGUIService implements Listener {

    List<HumanEntity> viewers = new ArrayList<>();



    public InteractionGUIService(){

    }



    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(viewers.contains(event.getWhoClicked())){
            event.setCancelled(true);
            if(!event.getClickedInventory().getType().equals(InventoryType.CHEST)) return;
            int slot =event.getSlot();
            PlayerPetProfile.get(event.getWhoClicked().getUniqueId()).ifPresent(petProfile->{
                petProfile.getCurrentPet().ifPresent(pet -> {
                    if(slot == 37){
                        if(pet.getPetType().isMountable()){
                            MountPetUtil.mountPet((Player) event.getWhoClicked(),pet.getPetEntity().get(),pet.getPetType().getMountType());
                        }
                        else event.getWhoClicked().sendMessage(Message.CantRide);
                    }
                    else if(slot == 40){
                        if(!MythicPets.over17){
                            event.getWhoClicked().openInventory(petProfile.getPetInventory());
                        }
                        else{
                            event.getWhoClicked().sendMessage("此分流無法使用寵物背包");
                        }
                    }
                    else if(slot == 43){
                        petProfile.despawnPet();
                        event.getWhoClicked().closeInventory();
                    }
                });

            });
        }
    }

    public void open(HumanEntity player){
        Inventory inv = InteractionGUIConfig.createGUI(player);
        player.openInventory(inv);
        viewers.add(player);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        viewers.remove(event.getPlayer());
    }

}
