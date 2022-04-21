package net.brian.mythicpet.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.PetUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class IgnoreProjectile implements Listener {

    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOWEST)
    public void onShot(ProjectileHitEvent event){
        if(MythicPets.worldGuard){
            if(event.getHitEntity() != null){
                if(PetUtils.isPet(event.getHitEntity())){
                    Projectile projectile = event.getEntity();
                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(event.getHitEntity().getLocation()));
                    if(projectile.getShooter() instanceof Player){
                        if(!set.testState(null, MythicPets.inst().worldGuardFlag.pet_damaged_by_player)){
                            event.setCancelled(true);
                        } else if (((Player) projectile.getShooter()).getUniqueId().equals(PetUtils.getOwner(event.getHitEntity()).get())) {
                            event.setCancelled(true);
                        } else if(!set.testState(null, MythicPets.inst().worldGuardFlag.pet_hurt)){
                            event.setCancelled(true);
                        }
                    }
                    else if(projectile.getShooter() instanceof Entity){
                        Entity entity = (Entity) projectile.getShooter();
                        if(PetUtils.isPet(entity)){
                            if(!set.testState(null, MythicPets.inst().worldGuardFlag.pet_damage_pet)){
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
