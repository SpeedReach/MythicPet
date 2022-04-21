package net.brian.mythicpet.compatible.mythicmobs;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Optional;
import java.util.UUID;

public interface MythicUtil {

    MythicUtil[] instance = new MythicUtil[]{null};


    Optional<Entity> spawnMob(String id, Location location, double level);

    double getMobLevel(Entity entity);

    void setMobLevel(Entity entity, double level);

    boolean mythicmobExists(String id);

    double getMobDefaultHealth(String id,int level);

    void setMobOwner(Entity entity,UUID uuid);

    void navigateMobTo(Entity entity,Location location);

    Location findSafeLocation(Location location,double height);


    static Optional<Entity> spawn(String id, Location location, double level){
        return instance[0].spawnMob(id,location,level);
    }



    static double getLevel(Entity entity){
        return instance[0].getMobLevel(entity);
    }

    static void setLevel(Entity entity,double level){
        instance[0].setMobLevel(entity,level);
    }

    static boolean mobExist(String id){
        return instance[0].mythicmobExists(id);
    }

    static double getDefaultHealth(String id,int level){
        return instance[0].getMobDefaultHealth(id,level);
    }

    static void setOwner(Entity entity,UUID uuid){
        instance[0].setMobOwner(entity,uuid);
    }

    static void navigateTo(Entity entity,Location location){
        instance[0].navigateMobTo(entity,location);
    }

    static Location safeLocation(Location location,double height){
        return instance[0].findSafeLocation(location,height);
    }

}
