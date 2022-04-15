package net.brian.mythicpet.compatible.mythicmobs.legacy.target;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import io.lumine.xikage.mythicmobs.util.annotations.MythicTargeter;
import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.util.PetUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@MythicTargeter(
        author = "SleepAllDay",
        version = "1.0",
        name = "PetTargets"
)
public class PetTargets extends IEntitySelector {

    double r;

    public PetTargets(MythicLineConfig mlc) {
        super(mlc);
        r = mlc.getDouble(new String[]{"r","radius"});
    }

    @Override
    public Collection<AbstractEntity> getEntities(SkillMetadata skillMetadata) {
        SkillCaster am = skillMetadata.getCaster();
        Location loc = BukkitAdapter.adapt(am.getLocation());
        List<Entity> entityList = am.getEntity().getBukkitEntity().getNearbyEntities(r,r,r);
        return entityList.stream().filter(entity -> {
           if(MythicPet.worldGuard){
               if(MythicPet.inst().getWorldGuardSupport().petCanHitPet(loc)){
                  if(PetUtils.isPet(entity)) return false;
               }
               if(MythicPet.inst().getWorldGuardSupport().petCanHitPlayer(loc)){
                   if(PetUtils.isPet(entity)) return false;
               }
           }
           if(entity.getUniqueId().equals(PetUtils.getOwner(am.getEntity().getBukkitEntity()).get())){
               return false;
           }
           return entity instanceof LivingEntity;
        }).map(BukkitAdapter::adapt).collect(Collectors.toList());
    }


}
