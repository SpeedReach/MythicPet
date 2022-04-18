package net.brian.mythicpet.storage;

import net.brian.mythicpet.MythicPets;
import org.bukkit.entity.HumanEntity;

import java.util.HashMap;
import java.util.UUID;

public class StorageManager {

    public static HashMap<UUID,PetStorage> storageMap = new HashMap<>();

    public static void openPlayer(HumanEntity player,int thePage){
        MythicPets.getPlayer(player.getUniqueId()).ifPresent(playerData->{
            int page = thePage;
            if(page < 1) page = 1;
            PetStorage storage = new PetStorage(playerData);
            storageMap.put(player.getUniqueId(),storage);
            while(true){
                if(storage.window.containsKey(page)){
                    player.openInventory(storage.window.get(page));
                    storage.page = page;
                    break;
                }
                else{
                    page--;
                }
            }
        });
    }


}
