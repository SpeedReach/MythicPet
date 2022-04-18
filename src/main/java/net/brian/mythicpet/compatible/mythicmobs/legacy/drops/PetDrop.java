package net.brian.mythicpet.compatible.mythicmobs.legacy.drops;

import io.lumine.xikage.mythicmobs.adapters.AbstractItemStack;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.pet.PetImpl;


public class PetDrop extends Drop implements IItemDrop {
    String type;
    public PetDrop(String line, MythicLineConfig config) {
        super(line, config);
        type = config.getString("type");
    }

    @Override
    public AbstractItemStack getDrop(DropMetadata dropMetadata) {
        Pet pet = new PetImpl(PetDirectory.getModel(type));
        return BukkitAdapter.adapt(pet.getIcon(null));
    }
}
