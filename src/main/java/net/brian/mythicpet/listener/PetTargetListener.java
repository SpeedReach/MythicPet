package net.brian.mythicpet.listener;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.compatible.worldguard.WorldGuardSupport;
import net.brian.mythicpet.player.Mode;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.utils.PetUtils;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PetTargetListener implements Listener {

    private final MythicPets plugin;
    public PetTargetListener(MythicPets plugin){
        this.plugin=plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTargetChange(EntityTargetEvent event){
        if(PetUtils.isPet(event.getEntity())){
            Entity currentTarget = event.getTarget();
            if(currentTarget != null){
                if(MythicPets.worldGuard){
                    Location location = currentTarget.getLocation();
                    WorldGuardSupport worldGuardSupport =plugin.getWorldGuardSupport();
                    if(currentTarget instanceof Player){
                        if(!worldGuardSupport.petCanHitPlayer(location)){
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if(PetUtils.isPet(currentTarget)){
                        if(!worldGuardSupport.petCanHitPet(location)){
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }

            PetUtils.getPetInstance(event.getEntity())
                    .flatMap(pet -> pet.getTargetTable().getHighest())
                    .ifPresent(target->{
                        if (event.getTarget() == null || !event.getTarget().equals(target)) {
                            event.setTarget(target);
                            event.setCancelled(false);
                        }
                    });
        }
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onOwnerHitOther(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Damageable)) return;
        if(event.getDamager() instanceof Player){
            PetUtils.getPlayer(event.getDamager().getUniqueId())
                    .filter(profile -> profile.mode.equals(Mode.ATTACK))
                    .flatMap(PlayerPetProfile::getCurrentPet)
                    .ifPresent(pet -> pet.getTargetTable().addScore(event.getEntity(),event.getFinalDamage()));
        }
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onOwnerGetHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            PlayerPetProfile.get(event.getEntity().getUniqueId())
                    .filter(profile -> !profile.mode.equals(Mode.FOLLOW))
                    .flatMap(PlayerPetProfile::getCurrentPet)
                    .ifPresent(pet -> {
                        if(event.getDamager() instanceof Projectile){
                            ProjectileSource source = ((Projectile) event.getDamager()).getShooter();
                            if(source instanceof Entity){
                                pet.getTargetTable().addScore((Entity) source,event.getFinalDamage());
                            }
                        }
                        if(event.getDamager() instanceof  Damageable){
                            pet.getTargetTable().addScore(event.getDamager(),event.getFinalDamage());
                        }
                    });
        }
    }


}
