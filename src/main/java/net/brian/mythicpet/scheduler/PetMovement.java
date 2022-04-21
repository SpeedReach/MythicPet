package net.brian.mythicpet.scheduler;


import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PetMovement {

    MythicPets plugin = MythicPets.inst();

    public void onTick(){
        Bukkit.getScheduler().runTaskTimer(plugin,()->{
            for(Player player : Bukkit.getOnlinePlayers()){
                PlayerPetProfile.get(player.getUniqueId())
                        .flatMap(PlayerPetProfile::getCurrentPet)
                        .ifPresent(pet->{
                            pet.getPetEntity().ifPresent(entity -> {
                                if(!player.getWorld().equals(entity.getWorld())){
                                    entity.teleport(MythicUtil.safeLocation(player.getLocation(),entity.getHeight()));
                                }
                                else if(entity.getLocation().distance(player.getLocation())>15){
                                    entity.teleport(MythicUtil.safeLocation(player.getLocation(),entity.getHeight()));
                                }
                                getPlayerPassenger(entity).ifPresentOrElse(passenger->{
                                    Location targetLocation = player.getLocation().add(passenger.getEyeLocation().getDirection().normalize().multiply(100));
                                    MythicUtil.navigateTo(entity,targetLocation);
                                },()-> pet.getTargetTable().getHighest().ifPresentOrElse(target->{
                                    if(entity instanceof Mob mob){
                                        mob.setTarget(target);
                                    }
                                },()-> {
                                    if(entity instanceof Mob mob){
                                        mob.setTarget(null);
                                    }
                                    MythicUtil.navigateTo(entity,player.getLocation());
                                }));
                            });
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
