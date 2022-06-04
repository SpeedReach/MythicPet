package net.brian.mythicpet.compatible.mythicmobs.latest;

import io.lumine.mythic.api.MythicPlugin;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.print.DocFlavor;
import java.util.Optional;
import java.util.UUID;

public class NewMythicUtil implements MythicUtil {


    @Override
    public Optional<Entity> spawnMob(String id, Location location, double level) {
        Optional<MythicMob> optMob = MythicProvider.get().getMobManager().getMythicMob(id);
        if(optMob.isPresent()){
            ActiveMob activeMob = optMob.get().spawn(BukkitAdapter.adapt(location),level);
            return Optional.of(activeMob.getEntity().getBukkitEntity());
        }
        else return Optional.empty();
    }

    @Override
    public double getMobLevel(Entity entity) {
        return MythicProvider.get().getMobManager()
                .getSkillCaster(entity.getUniqueId())
                .map(SkillCaster::getLevel).orElse(0.0);
    }

    @Override
    public void setMobLevel(Entity entity, double level) {
        MythicProvider.get().getMobManager().getSkillCaster(entity.getUniqueId())
                .map(skillCaster -> (ActiveMob) skillCaster)
                .ifPresent(activeMob -> activeMob.setLevel(level));
    }

    @Override
    public boolean mythicmobExists(String id) {
        return MythicProvider.get().getMobManager().getMythicMob(id).isPresent();
    }

    @Override
    public double getMobDefaultHealth(String id,int level) {
        return MythicProvider.get().getMobManager().getMythicMob(id)
                .map(mythicMob -> {

                    double health = mythicMob.getHealth().get();

                    if (mythicMob.getPerLevelHealth() > 0.0D) {
                        if (level > 1.0D) {
                            health += mythicMob.getPerLevelHealth() * (level - 1);
                        }
                    }
                    else if (MythicBukkit.inst().getConfiguration().getScalingEquationHealth() != null) {
                        health = MythicBukkit.inst().getConfiguration().getScalingEquationHealth().setVariable("v", health).setVariable("l",level).evaluate();
                    }

                    return health;
                }).orElse(0.0);
    }

    @Override
    public void setMobOwner(Entity entity, UUID uuid) {
        MythicProvider.get().getMobManager().getSkillCaster(entity.getUniqueId())
                .map(skillCaster -> (ActiveMob)skillCaster)
                .ifPresent(activeMob -> activeMob.setOwner(uuid));
    }

    @Override
    public void navigateMobTo(Entity entity, Location location) {
        MythicProvider.get().getVolatileCodeHandler().getAIHandler()
                .navigateToLocation(BukkitAdapter.adapt(entity),BukkitAdapter.adapt(location),5);
    }

    @Override
    public Location findSafeLocation( Location location, double height) {
        return BukkitAdapter.adapt(MobExecutor.findSafeSpawnLocation(BukkitAdapter.adapt(location),5,3,(int) height,false,true));
    }

}
