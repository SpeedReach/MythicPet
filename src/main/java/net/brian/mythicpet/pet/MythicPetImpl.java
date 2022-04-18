package net.brian.mythicpet.pet;

import net.brian.mythicpet.api.Icon;
import net.brian.mythicpet.api.MythicPet;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.utils.time.TimeInfo;
import org.bukkit.configuration.ConfigurationSection;

public class MythicPetImpl implements MythicPet {
    private final String id;
    private final boolean mountOnly;
    private final String mountType;
    private final Icon icon;

    public MythicPetImpl(String id, ConfigurationSection section){
        this.id = id;
        mountType = section.getString("MountType","walking");
        mountOnly = section.getBoolean("MountOnly");
        icon = new Icon(section.getConfigurationSection("Icon"));


    }



    @Override
    public String getID() {
        return null;
    }

    @Override
    public int getRequire(int level) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public TimeInfo getRespawnCooldown() {
        return null;
    }

    @Override
    public int getMaxHealth(int level) {
        return (int) MythicUtil.getDefaultHealth(id,level);
    }

    @Override
    public boolean isMountOnly() {
        return mountOnly;
    }

    @Override
    public String getMountType() {
        return mountType;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public boolean isMountable() {
        return false;
    }

    @Override
    public boolean canEdit() {
        return false;
    }
}
