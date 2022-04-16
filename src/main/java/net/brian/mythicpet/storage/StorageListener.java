package net.brian.mythicpet.storage;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.pet.Pet;
import net.brian.mythicpet.player.Mode;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

import java.util.UUID;

public class StorageListener implements Listener {


    @EventHandler
    public void clickEvent(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(Message.StorageTitle)) return;
        UUID uuid = event.getWhoClicked().getUniqueId();
        if(!MythicPet.isLoaded((Player) event.getWhoClicked())){
            return;
        }
        PlayerPetProfile data = MythicPet.getPlayer(uuid).get();
        event.setCancelled(true);
        if (event.getClickedInventory() == null) return;
        int slot = event.getSlot();
        PetStorage storage = StorageManager.storageMap.get(uuid);
        switch (slot) {
            case 46:
                storage.previousPage(event.getWhoClicked());
                break;
            case 52:
                storage.nextPage(event.getWhoClicked());
                break;
            case 47:
                data.getCurrentPet().ifPresent(pet -> {
                    pet.getPetEntity().teleport(event.getWhoClicked());
                    pet.getTargetTable().clear();
                });
                break;
            case 48:
                storage.toggleEdit();
                break;
            case 49:
                data.mode = Mode.DEFENSE;
                event.getWhoClicked().sendMessage(Message.SwitchedToDefense);
                data.getCurrentPet().ifPresent(pet -> pet.getTargetTable().clear());
                break;
            case 50:
                data.mode = Mode.ATTACK;
                event.getWhoClicked().sendMessage(Message.SwtichedToAttack);
                data.getCurrentPet().ifPresent(pet -> pet.getTargetTable().clear());
                break;
            case 51:
                data.mode = Mode.FOLLOW;
                event.getWhoClicked().sendMessage(Message.SwitchedToFollow);
                data.getCurrentPet().ifPresent(pet -> pet.getTargetTable().clear());
                break;
            default:
                int clicked = PetStorage.getSlot(slot, storage.page);
                if (clicked != -1) {
                    if(data.pets.size()>clicked){
                        Pet clickedPet = data.pets.get(clicked);
                        if(storage.editMode){
                            if(!clickedPet.getType().canEdit()){
                                event.getWhoClicked().sendMessage(Message.CannotEdit);
                                return;
                            }
                            if(event.getWhoClicked().getInventory().addItem(clickedPet.generateIcon(null)).isEmpty()){
                                data.removePet(clickedPet);
                                StorageManager.openPlayer(event.getWhoClicked(),storage.page);
                            }
                            else{
                                event.getWhoClicked().sendMessage(Message.InventoryFull);
                                data.getCurrentPet().ifPresent(currentPet->{
                                    if(currentPet.equals(clickedPet)){
                                        data.despawnPet();
                                    }
                                });
                            }
                        }
                        else if(event.isLeftClick()){
                            data.getCurrentPet().ifPresentOrElse(pet -> {
                                data.despawnPet();
                                if(!pet.equals(clickedPet)){
                                    data.spawnPet(clicked);
                                }
                            },()-> data.spawnPet(clicked));
                            event.getWhoClicked().closeInventory();
                        }
                    }
                }
        }
    }

}