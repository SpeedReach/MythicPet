package net.brian.mythicpet.compatible.libdisguise;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.brian.mythicpet.event.PetSpawnEvent;
import net.brian.mythicpet.pet.Pet;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisguiseListener implements Listener {

    @EventHandler
    public void onPetSpawn(PetSpawnEvent event) {
        Pet pet = event.getPet();
        disguise(pet.getPetEntity());
    }

    public static void disguise(Entity entity){
        Disguise disguise = DisguiseAPI.getDisguise(entity);
        if (disguise != null) {
            if (disguise instanceof PlayerDisguise) {
                PlayerDisguise playerDisguise = (PlayerDisguise)disguise;
                playerDisguise.setName(entity.getCustomName());
            } else {
                disguise.getWatcher().setCustomName(entity.getCustomName());
            }
        }
    }

}
