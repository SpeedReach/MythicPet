package net.brian.mythicpet.scheduler;


import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.pet.Pet;
import net.brian.mythicpet.player.Mode;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PetMovement {

    MythicPet plugin = MythicPet.inst();

    public void onTick(){
        Bukkit.getScheduler().runTaskTimer(plugin,()->{
            for(Player player : Bukkit.getOnlinePlayers()){
                PlayerPetProfile.get(player.getUniqueId())
                        .filter(profile -> !profile.mode.equals(Mode.FOLLOW))
                        .flatMap(PlayerPetProfile::getCurrentPet)
                        .filter(pet -> pet.getTargetTable().hasTarget())
                        .map(Pet::getPetEntity)
                        .ifPresent(entity -> {
                            if(!player.getWorld().equals(entity.getWorld())){
                                entity.teleport(MythicUtil.safeLocation(player.getLocation(),entity.getHeight()));
                            }
                            else if(entity.getLocation().distance(player.getLocation())>15){
                                entity.teleport(MythicUtil.safeLocation(player.getLocation(),entity.getHeight()));
                            }
                            else{
                                getPlayerPassenger(entity).ifPresentOrElse(passenger->{
                                    Location targetLocation = player.getLocation().add(passenger.getEyeLocation().getDirection().normalize().multiply(100));
                                    MythicUtil.navigateTo(entity,targetLocation);
                                },()-> MythicUtil.navigateTo(entity,player.getLocation()));
                            }
                        });
            }
        },0,50);
    }


    Optional<Player> getPlayerPassenger(Entity entity){
        for (Entity passenger : entity.getPassengers()) {
            if(passenger instanceof Player player){
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

}
