package net.brian.mythicpet.compatible.mythicmobs.legacy.condition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.api.PetUtils;
import org.bukkit.entity.Mob;

public class HasTarget extends SkillCondition implements IEntityCondition {
    public HasTarget(String line) {
        super(line);
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        if(PetUtils.isPet(abstractEntity.getBukkitEntity())){
            return PetUtils.getOwnerProfile(abstractEntity.getBukkitEntity())
                    .flatMap(PlayerPetProfile::getCurrentPet)
                    .map(pet -> pet.getTargetTable().hasTarget())
                    .orElse(false);
        }
        if(abstractEntity.getBukkitEntity() instanceof Mob){
            Mob mob = (Mob) abstractEntity.getBukkitEntity();
            return mob.getTarget() != null;
        }
        return false;
    }
}
