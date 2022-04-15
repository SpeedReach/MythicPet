package net.brian.mythicpet.util;

import net.brian.mythicpet.pet.Pet;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;

public class PetUtils {

    public static Optional<Pet> getPetInstance(Entity entity){
        return getOwner(entity)
                .flatMap(PlayerPetProfile::get)
                .flatMap(PlayerPetProfile::getCurrentPet);
    }

    public static Optional<PlayerPetProfile> getPlayer(UUID uuid){
        return PlayerPetProfile.get(uuid);
    }

    public static Optional<UUID> getOwner(Entity entity){
        String uuid = entity.getPersistentDataContainer().get(Pet.ownerKey, PersistentDataType.STRING);
        if(uuid != null){
            return Optional.of(UUID.fromString(uuid));
        }
        return Optional.empty();
    }

    public static boolean isPet(Entity entity){
        return entity.getPersistentDataContainer().has(Pet.ownerKey,PersistentDataType.STRING);
    }

    public static Optional<PlayerPetProfile> getOwnerProfile(Entity entity){
        return getOwner(entity).flatMap(PetUtils::getPlayer);
    }


}
