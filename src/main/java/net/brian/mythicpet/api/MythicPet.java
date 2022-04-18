package net.brian.mythicpet.api;

import net.brian.mythicpet.utils.time.TimeInfo;

import java.util.List;

public interface MythicPet {


    String getID();

    int getRequire(int level);

    int getMaxLevel();

    TimeInfo getRespawnCooldown();

    int getMaxHealth(int level);

    boolean isMountOnly();

    String getMountType();

    Icon getIcon();

    String getDisplayName();

    boolean isMountable();

    boolean canEdit();

}
