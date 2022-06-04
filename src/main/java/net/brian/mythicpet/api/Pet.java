package net.brian.mythicpet.api;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.pets.PetTargetTable;
import net.brian.mythicpet.utils.time.TimeInfo;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface Pet {

    NamespacedKey ownerKey = new NamespacedKey(MythicPets.inst(),"owner");

    default SpawnResult spawn(Player owner){
       return spawn(owner.getLocation(),owner);
    }

    abstract SpawnResult spawn(Location location, Player owner);

    abstract void despawn();

    default ItemStack getIcon(){
        return getIcon(null);
    }
    abstract ItemStack getIcon(Player owner);

    abstract void setDead();

    abstract Optional<Entity> getPetEntity();

    abstract void addExp(int amount);

    abstract void setLevel(int level);

    abstract int getLevel();

    abstract boolean isActive();

    abstract boolean canSpawn();

    abstract TimeInfo getRecoverTime();

    abstract void refreshUpstream();

    abstract MythicPet getPetType();
    abstract String getPetID();

    abstract PetTargetTable getTargetTable();



}
