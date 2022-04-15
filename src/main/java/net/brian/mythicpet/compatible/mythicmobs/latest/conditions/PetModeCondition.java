package net.brian.mythicpet.compatible.mythicmobs.latest.conditions;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.core.skills.SkillCondition;
import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.player.Mode;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.util.PetUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PetModeCondition extends SkillCondition implements IEntityCondition {
    
    final String mode;
    public PetModeCondition(MythicLineConfig mlc) {
        super(mlc.getLine());
        mode = mlc.getString("m", Mode.ATTACK);
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        Entity bukkitEntity = abstractEntity.getBukkitEntity();
        if(bukkitEntity instanceof Player){
            return PlayerPetProfile.get(bukkitEntity.getUniqueId()).map(profile -> mode.equals(profile.mode)).orElse(false);
        }
        else {
            return PetUtils.getOwnerProfile(abstractEntity.getBukkitEntity()).map(profile -> mode.equals(profile.mode)).orElse(false);
        }
    }
}
