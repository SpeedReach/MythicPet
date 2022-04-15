package net.brian.mythicpet.compatible.pandeloot;

import net.brian.mythicpet.pet.PetDirectory;
import net.brian.mythicpet.util.PetUtils;
import net.seyarada.pandeloot.damage.DamageTracker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PandeLootDamageRegister implements Listener {

    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onPetHit(EntityDamageByEntityEvent event){
        PetUtils.getOwnerProfile(event.getDamager()).ifPresent(profile -> {
            DamageTracker.addPlayerDamage(event.getEntity().getUniqueId(), profile.getPlayer(), event.getFinalDamage());
        });
    }

}
