package net.brian.mythicpet.compatible.placeholder.subplaceholder;

import net.brian.mythicpet.pet.Pet;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PetHealth extends SubPlaceholder{

    public PetHealth() {
        super("health");
    }

    @Override
    public String onPlaceholderRequest(@NotNull PlayerPetProfile profile, @NotNull String[] params) {
        return profile.getCurrentPet().map(pet -> {
            if(pet.getPetEntity() instanceof LivingEntity livingEntity && livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH) !=null){
                int maxHealth = (int) livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                return (int) livingEntity.getHealth() + "/" + maxHealth;
            }
            return "當前無寵物";
        }).orElse("當前無寵物");
    }
}
