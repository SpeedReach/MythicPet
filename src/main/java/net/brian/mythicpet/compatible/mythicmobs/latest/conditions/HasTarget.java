package net.brian.mythicpet.compatible.mythicmobs.latest.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.core.skills.SkillCondition;
import net.brian.mythicpet.utils.PetUtils;
import org.bukkit.entity.Mob;

public class HasTarget extends SkillCondition implements IEntityCondition {
    public HasTarget(String line) {
        super(line);
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        if(PetUtils.isPet(abstractEntity.getBukkitEntity())){
            return PetUtils.getOwnerProfile(abstractEntity.getBukkitEntity())
                    .map(profile -> profile.getCurrentPet().map(pet -> pet.getTargetTable().hasTarget()).orElse(false))
                    .orElse(false);
        }
        if(abstractEntity.getBukkitEntity() instanceof Mob){
            Mob mob = (Mob) abstractEntity.getBukkitEntity();
            return mob.getTarget() != null;
        }
        return false;
    }
}
