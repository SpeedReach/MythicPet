package net.brian.mythicpet.compatible.mythicmobs.legacy;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.placeholders.PlaceholderMeta;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Optional;
import java.util.UUID;

public class LegacyMythicUtil implements MythicUtil {


    @Override
    public Optional<Entity> spawnMob(String id, Location location, double level) {
        ActiveMob mob = MythicMobs.inst().getMobManager().spawnMob(id,location,level);
        if(mob != null){
            return Optional.of(mob.getEntity().getBukkitEntity());
        }
        return Optional.empty();
    }

    @Override
    public double getMobLevel(Entity entity) {
        ActiveMob activeMob = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
        if(activeMob == null){
            return 0;
        }
        else return activeMob.getLevel();
    }

    @Override
    public void setMobLevel(Entity entity, double level) {
        ActiveMob activeMob = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
        if(activeMob != null) {
            activeMob.setLevel(level);
        }
    }

    @Override
    public boolean mythicmobFound(String id) {
        return MythicMobs.inst().getMobManager().getMythicMob(id) != null;
    }

    @Override
    public double getMobDefaultHealth(String id) {
        MythicMob mythicMob = MythicMobs.inst().getAPIHelper().getMythicMob(id);
        if(mythicMob != null){
            return mythicMob.getHealth().get();
        }
        return 0;
    }

    @Override
    public void setMobOwner(Entity entity, UUID uuid) {

    }

    @Override
    public void navigateMobTo(Entity entity, Location location) {

    }

    @Override
    public Location findSafeLocation(Location location, double height) {
        return null;
    }


}
