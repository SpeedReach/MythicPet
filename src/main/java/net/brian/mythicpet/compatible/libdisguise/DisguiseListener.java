package net.brian.mythicpet.compatible.libdisguise;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.brian.mythicpet.api.event.PetSpawnEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DisguiseListener implements Listener {

    @EventHandler
    public void onPetSpawn(PetSpawnEvent event) {
        event.getPet().getPetEntity().ifPresent(DisguiseListener::disguise);
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
