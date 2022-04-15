package net.brian.mythicpet.petinteraction;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.util.PetUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RightClickPetListener implements Listener {

    private final MythicPet plugin;
    public RightClickPetListener(MythicPet plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event){
        if(!MythicPet.getInteractionGUIConfig().isEnabled()){
            return;
        }
        if(event.getPlayer().isSneaking()){
            PetUtils.getOwner(event.getRightClicked())
                    .filter(uuid->event.getPlayer().getUniqueId().equals(uuid))
                    .ifPresent(uuid -> {
                        event.setCancelled(true);
                        MythicPet.getInteractionGUIService().open(event.getPlayer());
                    });
        }
    }

}
