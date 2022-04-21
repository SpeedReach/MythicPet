package net.brian.mythicpet.compatible.mythicmobs.legacy.condition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import net.brian.mythicpet.api.PetUtils;

public class IsPetCondition extends SkillCondition implements IEntityCondition {
    public IsPetCondition(String line) {
        super(line);
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        return PetUtils.isPet(abstractEntity.getBukkitEntity());
    }
}
