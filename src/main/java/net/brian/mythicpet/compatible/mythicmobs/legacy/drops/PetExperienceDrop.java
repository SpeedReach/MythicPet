package net.brian.mythicpet.compatible.mythicmobs.legacy.drops;

import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IIntangibleDrop;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.config.Message;

import java.util.UUID;


public class PetExperienceDrop extends Drop implements IIntangibleDrop {

    public PetExperienceDrop(String line, MythicLineConfig config) {
        super(line, config);
    }

    @Override
    public void giveDrop(AbstractPlayer abstractPlayer, DropMetadata dropMetadata) {
        UUID uuid = abstractPlayer.getUniqueId();
        MythicPets.getPlayer(uuid).ifPresent(playerPetProfile -> {
            playerPetProfile.addExp((int) getAmount());
            String message = String.valueOf(Message.ExpRecieve);
            message = message.replace("#Exp#",String.valueOf(getAmount()));
            abstractPlayer.sendActionBarMessage(message);
        });
    }

}
