package net.brian.mythicpet.compatible.mythicmobs.latest.drops;

import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import net.brian.mythicpet.pet.Pet;
import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.pet.PetModel;

public class PetDrop implements IItemDrop {

    String id;
    public PetDrop(MythicLineConfig mlc){
        id = mlc.getString("type");

    }

    @Override
    public AbstractItemStack getDrop(DropMetadata dropMetadata, double amount) {
        PetModel petModel = PetDirectory.getModel(id);
        if(petModel != null){
            return new BukkitItemStack(new Pet(id).generateIcon(null));
        }
        return null;
    }

}
