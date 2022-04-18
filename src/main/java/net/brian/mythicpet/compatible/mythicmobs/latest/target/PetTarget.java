package net.brian.mythicpet.compatible.mythicmobs.latest.target;

import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.targeters.IEntitySelector;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.utils.PetUtils;

import java.util.*;

public class PetTarget extends IEntitySelector {


    public PetTarget(MythicLineConfig mlc) {
        super((SkillExecutor) MythicProvider.get().getSkillManager(),mlc);
    }

    @Override
    public Collection<AbstractEntity> getEntities(SkillMetadata skillMetadata) {
        return PetUtils.getOwnerProfile(skillMetadata.getCaster().getEntity().getBukkitEntity())
                .flatMap(PlayerPetProfile::getCurrentPet)
                .filter(pet -> pet.getTargetTable().hasTarget())
                .flatMap(pet -> pet.getTargetTable().getHighest())
                .map(entity-> Collections.singleton(BukkitAdapter.adapt(entity)))
                .orElse(new HashSet<>());
    }
}
