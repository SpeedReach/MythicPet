package net.brian.mythicpet.api;

import net.brian.mythicpet.pets.MythicPetImpl;
import net.brian.mythicpet.utils.time.TimeInfo;

public interface MythicPet {


    String getID();

    String getMythicMob();

    int getRequire(int level);

    int getMaxLevel();

    TimeInfo getRespawnCooldown();

    int getMaxHealth(int level);

    boolean isMountOnly();

    String getMountType();

    MythicPetImpl.Icon getIcon();

    String getDisplayName();

    boolean isMountable();

    boolean canEdit();

}
