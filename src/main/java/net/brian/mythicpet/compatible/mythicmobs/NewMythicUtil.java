package net.brian.mythicpet.compatible.mythicmobs;

import io.lumine.mythic.api.MythicPlugin;
import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.api.mobs.MobManager;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import javax.print.DocFlavor;
import java.util.Optional;
import java.util.UUID;

public class NewMythicUtil implements MythicUtil{


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
    public boolean mythicmobFound(String id) {
        return MythicProvider.get().getMobManager().getMythicMob(id).isPresent();
    }

    @Override
    public double getMobDefaultHealth(String id) {
        return MythicProvider.get().getMobManager().getMythicMob(id)
                .map(mythicMob -> mythicMob.getHealth().get())
                .orElse(0.0);
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
        return BukkitAdapter.adapt(MobExecutor.findSafeSpawnLocation(BukkitAdapter.adapt(location),5,(int) height));
    }

}
