package net.brian.mythicpet.listener;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.compatible.libdisguise.DisguiseListener;
import net.brian.mythicpet.pet.Pet;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.spigotmc.event.entity.EntityDismountEvent;


public class OwnerAction implements Listener {

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event){
        PlayerPetProfile.get(event.getPlayer().getUniqueId()).ifPresent(profile->{
            if(profile.hasActive()){
                profile.getCurrentPet().map(Pet::getPetEntity).ifPresent(entity -> {
                    entity.teleport(event.getPlayer().getLocation());
                    Bukkit.getScheduler().runTaskLater(MythicPet.inst(),()->{
                        DisguiseListener.disguise(entity);
                    },20L);
                });
            }
        });
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onDismount(EntityDismountEvent event){
        PlayerPetProfile.get(event.getEntity().getUniqueId()).ifPresent(profile->{
            profile.getCurrentPet().ifPresent(pet->{
                if(pet.getType().isMountOnly()){
                    profile.despawnPet();
                }
            });
        });
    }


}
