package net.brian.mythicpet.compatible.mythicmobs.latest.drops;

import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IIntangibleDrop;
import io.lumine.mythic.api.drops.IItemDrop;
import net.brian.mythicpet.player.PlayerPetProfile;

public class PetExpDrop implements IIntangibleDrop {


    @Override
    public void giveDrop(AbstractPlayer abstractPlayer, DropMetadata dropMetadata, double v) {
        PlayerPetProfile.get(abstractPlayer.getUniqueId()).ifPresent(profile -> profile.addExp((int) v));
    }
}
