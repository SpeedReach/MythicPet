package net.brian.mythicpet.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.config.Settings;
import net.brian.mythicpet.player.Mode;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.utils.PetUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class BlockDamage implements Listener {
    MythicPets plugin = MythicPets.inst();

    /**
     *Blocks Damage when a pet is hurt
     */
    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOW)
    public void onPetHurt(EntityDamageEvent event){
        if(PetUtils.isPet(event.getEntity())){
            if(!Settings.FollowModeDamage){
                PetUtils.getOwnerProfile(event.getEntity()).ifPresent(profile -> {
                    if(profile.mode.equals(Mode.FOLLOW)){
                        event.setCancelled(true);
                    }
                });
            }
            if(MythicPets.worldGuard){
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(event.getEntity().getLocation()));
                if(!set.testState(null,plugin.worldGuardFlag.pet_hurt)){
                    event.setCancelled(true);
                }
            }
        }
    }

    //Blocks damage from pets and players
    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOW)
    public void onPetHitByOthers(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            if(PetUtils.isPet(event.getEntity())){
                UUID playerUUID = event.getDamager().getUniqueId();
                if(PetUtils.getOwner(event.getEntity()).get().equals(playerUUID)){
                    event.setCancelled(true);
                    return;
                }
                if(MythicPets.worldGuard){
                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(event.getEntity().getLocation()));
                    if(!set.testState(null,plugin.worldGuardFlag.pet_damaged_by_player)){
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(PetUtils.isPet(event.getDamager())){
            if(MythicPets.worldGuard){
                if(!PetUtils.isPet(event.getEntity())) return;
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(event.getEntity().getLocation()));
                if(!set.testState(null,plugin.worldGuardFlag.pet_damage_pet)){
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void petDamagePlayer(EntityDamageByEntityEvent event){
        if(!MythicPets.worldGuard) return;
        if(event.getEntity() instanceof Player){
            if(PetUtils.isPet(event.getDamager())){
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(event.getEntity().getLocation()));
                if(!set.testState(null,plugin.worldGuardFlag.pet_damage_player)){
                    event.setCancelled(true);
                }
            }
        }
    }


}
