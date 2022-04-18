package net.brian.mythicpet.compatible.placeholder.subplaceholder;

import net.brian.mythicpet.player.PlayerPetProfile;
import org.jetbrains.annotations.NotNull;

public class PetName extends SubPlaceholder{

    public PetName() {
        super("name");
    }

    @Override
    public String onPlaceholderRequest(@NotNull PlayerPetProfile playerProfile, @NotNull String[] params) {
        return playerProfile.getCurrentPet().map(pet -> pet .getPetType().getDisplayName()).orElse("當前無寵物");
    }

}
