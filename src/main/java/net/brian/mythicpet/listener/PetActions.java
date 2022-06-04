package net.brian.mythicpet.listener;


import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.config.Settings;
import net.brian.mythicpet.event.PetLevelUpEvent;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.api.PetUtils;
import net.brian.mythicpet.utils.MythicPetLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Optional;

public class PetActions implements Listener {
    MythicPets plugin = MythicPets.inst();

    @EventHandler
    public void onLevelUp(PetLevelUpEvent event){
        event.getPet().getPetEntity().ifPresent(petEntity->{
            MythicUtil.setLevel(petEntity,MythicUtil.getLevel(petEntity)+1);

            if(petEntity instanceof LivingEntity){
                LivingEntity livingEntity = ((LivingEntity) petEntity);
                if(Settings.HealOnlevelUp){
                    livingEntity.setHealth(livingEntity.getMaxHealth());
                }
            }

            if(petEntity.getCustomName() != null){
                petEntity.setCustomName(petEntity.getCustomName().replace("<mythicpet.owner>",event.getProfile().getPlayer().getName()));
            }
        });
    }


    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onPetKill(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        PetUtils.getOwner(damager).ifPresent(uuid -> {
            if(uuid.equals(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
            else{
                Player player = Bukkit.getPlayer(uuid);
                if(player != null){
                    LivingEntity entity = (LivingEntity) event.getEntity();
                    double finalDamage = event.getFinalDamage();
                    if(finalDamage >= ((LivingEntity) event.getEntity()).getHealth()){
                        event.setCancelled(true);
                        entity.damage(finalDamage, player);
                    }
                }
            }
        });

    }



    @EventHandler(ignoreCancelled = true)
    public void onDeath(EntityDeathEvent event){
        PetUtils.getOwnerProfile(event.getEntity()).ifPresent(profile -> {
            Pet pet = profile.getCurrentPetWithOutCheck();
            if(pet != null){
                pet.getPetEntity().ifPresent(entity -> {
                    if(entity.equals(event.getEntity())){
                        String message = String.valueOf(Message.PetDied);
                        message = message.replace("#pet_name#", pet.getPetType().getDisplayName());
                        Bukkit.getPlayer(profile.getUUID()).sendMessage(message);
                        pet.setDead();
                    }
                });
            }
        });
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onPetDamaged(EntityDamageByEntityEvent event){
        PetUtils.getOwnerProfile(event.getEntity())
                .flatMap(PlayerPetProfile::getCurrentPet)
                .ifPresent(pet->{
                    Entity damager = event.getDamager();
                    if(damager instanceof Projectile){
                        ProjectileSource projectileSource = ((Projectile) event.getDamager()).getShooter();
                        if(projectileSource instanceof LivingEntity){
                            pet.getTargetTable().addScore((LivingEntity) projectileSource,event.getFinalDamage());
                        }
                    }
                    else if(damager instanceof LivingEntity){
                        pet.getTargetTable().addScore(damager,event.getFinalDamage());
                    }
                });
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event){
        if(PetUtils.isPet(event.getEntity())){
            if(event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMount(EntityMountEvent event){
        if(event.getEntity() instanceof Player){
            PetUtils.getOwner(event.getMount())
                    .filter(uuid -> !event.getEntity().getUniqueId().equals(uuid))
                    .ifPresent(uuid -> event.setCancelled(true));
        }
    }

}
