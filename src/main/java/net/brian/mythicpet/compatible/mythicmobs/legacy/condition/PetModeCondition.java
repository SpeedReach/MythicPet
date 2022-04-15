package net.brian.mythicpet.compatible.mythicmobs.legacy.condition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.all.CustomCondition;
import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.player.Mode;
import net.brian.mythicpet.util.PetUtils;
import org.bukkit.entity.Entity;

public class PetModeCondition extends CustomCondition implements IEntityCondition {

    String mode;

    public PetModeCondition(String condition, String line, MythicLineConfig mlc) {
        super(condition, line, mlc);
        mode = mlc.getString("m", Mode.FOLLOW).toUpperCase();
    }


    @Override
    public boolean check(AbstractEntity abstractEntity) {
        Entity entity = abstractEntity.getBukkitEntity();
        if(PetUtils.isPet(entity)){
            return PetUtils.getOwnerProfile(entity)
                    .map(profile -> profile.mode.equals(mode))
                    .orElse(false);
        }
        return false;
    }
}
