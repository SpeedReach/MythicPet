package net.brian.mythicpet.petinteraction;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.utils.PetUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RightClickPetListener implements Listener {

    private final MythicPets plugin;
    public RightClickPetListener(MythicPets plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event){
        if(!MythicPets.getInteractionGUIConfig().isEnabled()){
            return;
        }
        if(event.getPlayer().isSneaking()){
            PetUtils.getOwner(event.getRightClicked())
                    .filter(uuid->event.getPlayer().getUniqueId().equals(uuid))
                    .ifPresent(uuid -> {
                        event.setCancelled(true);
                        MythicPets.getInteractionGUIService().open(event.getPlayer());
                    });
        }
    }

}
