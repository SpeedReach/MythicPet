package net.brian.mythicpet.compatible.mythicmobs.latest.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillCondition;
import net.brian.mythicpet.api.PetUtils;

public class IsPetCondition extends SkillCondition implements IEntityCondition{


    public IsPetCondition(String line) {
        super(line);
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        return PetUtils.isPet(BukkitAdapter.adapt(abstractEntity));
    }
}
