package net.brian.mythicpet.compatible.mythicmobs.latest.drops;

import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import net.brian.mythicpet.api.MythicPet;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.pet.PetImpl;
import net.brian.mythicpet.pet.PetTargetTable;

public class PetDrop implements IItemDrop {

    String id;
    public PetDrop(MythicLineConfig mlc){
        id = mlc.getString("type");

    }

    @Override
    public AbstractItemStack getDrop(DropMetadata dropMetadata, double amount) {
        MythicPet petType = PetDirectory.getModel(id);
        if(petType != null){
            return new BukkitItemStack(new PetImpl(petType).getIcon(null));
        }
        return null;
    }

}
