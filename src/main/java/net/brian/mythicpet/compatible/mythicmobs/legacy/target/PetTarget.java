package net.brian.mythicpet.compatible.mythicmobs.legacy.target;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.api.PetUtils;
import org.bukkit.entity.Entity;

import java.util.*;

public class PetTarget extends IEntitySelector {
    public PetTarget(MythicLineConfig mlc) {
        super(mlc);
    }

    @Override
    public Collection<AbstractEntity> getEntities(SkillMetadata skillMetadata) {
        SkillCaster skillCaster = skillMetadata.getCaster();
        Entity bukkitEntity = skillCaster.getEntity().getBukkitEntity();

        Set<AbstractEntity> abstractEntitySet = new HashSet<>();

        PetUtils.getOwnerProfile(bukkitEntity)
                .flatMap(PlayerPetProfile::getCurrentPet)
                .flatMap(pet -> pet.getTargetTable().getHighest())
                .map(BukkitAdapter::adapt)
                .ifPresent(abstractEntitySet::add);

        return abstractEntitySet;
    }
}
