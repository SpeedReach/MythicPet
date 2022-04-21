package net.brian.mythicpet.api;

import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface PetManager {



    Optional<MythicPet> getMythicPet(String id);

    Set<String> getPetKeys();

    Collection<MythicPet> getPets();

    void reload();




    static Optional<Pet> getPetInstance(Entity entity){
        return PetUtils.getPetInstance(entity);
    }


    static Optional<UUID> getOwner(Entity entity){
        return PetUtils.getOwner(entity);
    }

    static boolean isPet(Entity entity){
        return PetUtils.isPet(entity);
    }

    static Optional<PlayerPetProfile> getOwnerProfile(Entity entity){
        return PetUtils.getOwnerProfile(entity);
    }

}
