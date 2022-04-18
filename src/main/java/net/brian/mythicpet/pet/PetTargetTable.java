package net.brian.mythicpet.pet;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.*;

public class PetTargetTable {

    private final HashMap<LivingEntity,Double> targetMap;
    private final Mob mob;
    private final Boolean active;

    private Damageable target;

    public PetTargetTable(Entity entity){
        targetMap = new HashMap<>();
        if(entity instanceof Mob){
            mob = (Mob) entity;
            active = true;
        }
        else{
            active = false;
            mob = null;
        }
    }

    public void addScore(Entity entity, double amount){
        if(mob !=null && canDamage(entity)){
            targetMap.put((LivingEntity) entity,amount);
            getHighest().ifPresent(mob::setTarget);
        }
    }

    public Optional<LivingEntity> getHighest(){
        if(!active){
            return Optional.empty();
        }
        else {
            LivingEntity target = null;
            double score = 0;
            Iterator<Map.Entry<LivingEntity, Double>> it = targetMap.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<LivingEntity, Double> entry = it.next();
                if(entry.getKey().isDead()){
                    it.remove();
                }
                else if(entry.getValue() > score){
                    score = entry.getValue();
                    target = entry.getKey();
                }
            }
            return Optional.ofNullable(target);
        }
    }

    public void clear(){
        targetMap.clear();
        if(mob != null){
            mob.setTarget(null);
        }
    }

    public boolean hasTarget(){
        targetMap.entrySet().removeIf(entry -> entry.getKey().isDead());
        return targetMap.isEmpty();
    }

    private boolean canDamage(Entity entity){
        if(entity.equals(mob)){
            return false;
        }
        if(entity instanceof LivingEntity livingEntity){
            if(livingEntity.isInvulnerable()){
                return true;
            }
            return livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null;
        }
        return false;
    }

}
