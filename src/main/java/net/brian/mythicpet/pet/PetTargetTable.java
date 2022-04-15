package net.brian.mythicpet.pet;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.*;

public class PetTargetTable {

    private final HashMap<LivingEntity,Double> targetMap;
    private final Optional<Mob> optionalMob;
    private final Boolean active;

    private Damageable target;

    public PetTargetTable(Entity entity){
        targetMap = new HashMap<>();
        if(entity instanceof Mob){
            optionalMob = Optional.of((Mob) entity);
            active = true;
        }
        else{
            active = false;
            optionalMob = Optional.empty();
        }
    }

    public void addScore(Entity entity, double amount){
        optionalMob.ifPresent(mob->{
            if(!mob.equals(entity) && entity instanceof Damageable){
                targetMap.put((LivingEntity) entity,amount);
                getHighest().ifPresent(mob::setTarget);
            }
        });
    }

    public Optional<LivingEntity> getHighest(){
        if(!active){
            return Optional.empty();
        }
        else {
            LivingEntity target = null;
            double score = 0;
            for (Map.Entry<LivingEntity, Double> entry : targetMap.entrySet()) {
                if(entry.getValue() > score){
                    score = entry.getValue();
                    target = entry.getKey();
                }
            }
            return Optional.ofNullable(target);
        }
    }

    public void clear(){
        targetMap.clear();
        optionalMob.ifPresent(mob -> {
            mob.setTarget(null);
        });
    }

    public boolean hasTarget(){
        targetMap.entrySet().removeIf(entry -> entry.getKey().isDead());
        return targetMap.isEmpty();
    }

}
