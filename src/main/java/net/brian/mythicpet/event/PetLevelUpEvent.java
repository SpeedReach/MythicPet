package net.brian.mythicpet.event;

import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PetLevelUpEvent extends Event{

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final PlayerPetProfile profile;
    private final Pet pet;

    public PetLevelUpEvent(PlayerPetProfile profile, Pet pet){
        this.profile = profile;
        this.pet = pet;
    }

    @Override
    public @NotNull
    HandlerList getHandlers() {
        return HANDLERS_LIST;
    }


    public static HandlerList getHandlerList(){
        return HANDLERS_LIST;
    }

    public PlayerPetProfile getProfile() {
        return profile;
    }

    public Pet getPet() {
        return pet;
    }
}