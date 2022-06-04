package net.brian.mythicpet.scheduler;


import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
                                    if(isSafeLocation(player.getLocation())){
                                        entity.teleport(player.getLocation());
                                    }
                                }
                                else if(entity.getLocation().distance(player.getLocation())>15){
                                    if(isSafeLocation(player.getLocation())){
                                        entity.teleport(player.getLocation());
                                    }
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
                                    if(entity.getLocation().distance(player.getLocation()) > 4){
                                        MythicUtil.navigateTo(entity,player.getLocation());
                                    }
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


    public static boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
            return false; // not transparent (will suffocate)
        }
        Block head = feet.getRelative(BlockFace.UP);
        if (!head.getType().isTransparent()) {
            return false; // not transparent (will suffocate)
        }
        Block ground = feet.getRelative(BlockFace.DOWN);
        if (!ground.getType().isSolid()) {
            return false; // not solid
        }
        return true;
    }

}
