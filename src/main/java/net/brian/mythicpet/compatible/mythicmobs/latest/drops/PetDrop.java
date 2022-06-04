package net.brian.mythicpet.compatible.mythicmobs.latest.drops;

import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.MythicPet;
import net.brian.mythicpet.utils.PetDirectory;
import net.brian.mythicpet.pets.PetImpl;
import org.bukkit.Material;

public class PetDrop implements IItemDrop {

    String id;
    public PetDrop(MythicLineConfig mlc){
        id = mlc.getString("type","");

    }

    @Override
    public AbstractItemStack getDrop(DropMetadata dropMetadata, double amount) {
        return MythicPets.getPetManager().getMythicPet(id)
                .map(petType->new BukkitItemStack(new PetImpl(petType).getIcon(null)))
                .orElse(new BukkitItemStack(Material.AIR));
    }

}
