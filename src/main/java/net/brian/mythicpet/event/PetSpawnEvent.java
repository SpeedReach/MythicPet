package net.brian.mythicpet.event;

import net.brian.mythicpet.pet.Pet;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PetSpawnEvent extends Event{

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final Pet pet;
    private final Entity entity;

    public PetSpawnEvent(Player player, Pet pet, Entity entity){
        this.player = player;
        this.pet = pet;
        this.entity = entity;
    }

    @Override
    public @NotNull
    HandlerList getHandlers() {
        return HANDLERS_LIST;
    }


    public static HandlerList getHandlerList(){
        return HANDLERS_LIST;
    }

    public Player getPlayer(){
        return player;
    }

    public Pet getPet() {
        return pet;
    }

    public Entity getEntity() {
        return entity;
    }
}