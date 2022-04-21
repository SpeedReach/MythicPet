package net.brian.mythicpet.compatible.mmoitems.listener;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.api.PetUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class MMOItemsHealPet implements Listener {

    private final MythicPets plugin;
    private final HashMap<Player,Long> coolDownMap;
    private final long coolDown = 100L;

    public MMOItemsHealPet(MythicPets plugin){
        this.plugin = plugin;
        coolDownMap = new HashMap<>();
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event){
        if(!(event.getRightClicked() instanceof LivingEntity) ) return;
        if(event.getRightClicked().getPersistentDataContainer().has(Pet.ownerKey, PersistentDataType.STRING)){
            ItemStack clickedItem = event.getPlayer().getInventory().getItem(event.getHand());
            if(clickedItem.getType() != Material.AIR){
                NBTItem nbtItem = NBTItem.get(clickedItem);
                boolean used =false;
                if(nbtItem.hasTag("MMOITEMS_PET_HEALTH_RESTORE")){
                    if(checkCooldown(event.getPlayer())){
                        double healPercent = nbtItem.getDouble("MMOITEMS_PET_HEALTH_RESTORE");
                        LivingEntity livingEntity = (LivingEntity) event.getRightClicked();
                        double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        livingEntity.setHealth(Math.min(livingEntity.getHealth()+maxHealth*healPercent*0.01,maxHealth));
                        clickedItem.setAmount(clickedItem.getAmount()-1);
                        used = true;
                    }
                }
                if(nbtItem.hasTag("MMOITEMS_PET_EXP_GAIN")){
                    if(checkCooldown(event.getPlayer())){
                        double exp_gain = nbtItem.getDouble("MMOITEMS_PET_EXP_GAIN");
                        boolean finalUsed = used;
                        PetUtils.getOwnerProfile(event.getRightClicked()).ifPresent(profile -> {
                            profile.addExp((int) exp_gain);
                            if(!finalUsed){
                                clickedItem.setAmount(clickedItem.getAmount()-1);
                            }
                        });
                    }
                }
            }
        }
    }

    private boolean checkCooldown(Player player){
        long lastClicked = coolDownMap.getOrDefault(player,0L);
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastClicked  > coolDown){
            coolDownMap.put(player,currentTime);
            return true;
        }
        player.sendMessage("§x§f§f§e§5§9§9寵物還在咀嚼中，請在 §e"+(int)(currentTime-lastClicked)/1000 +"§x§f§f§e§5§9§9 秒後再餵食");
        return false;
    }

}
