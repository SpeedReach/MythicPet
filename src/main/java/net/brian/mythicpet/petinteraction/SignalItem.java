package net.brian.mythicpet.petinteraction;

import net.brian.mythicpet.MythicPets;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SignalItem implements Listener {
    public static final NamespacedKey KEY = new NamespacedKey(MythicPets.inst(),"CommandItemKey");

    public static void apply(ItemStack itemStack,String signal){
        if(itemStack == null) return;
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING,signal);
        itemStack.setItemMeta(meta);
    }

    public static String readSignal(ItemStack itemStack){
        if(itemStack == null) return null;
        if(!itemStack.hasItemMeta()) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(KEY,PersistentDataType.STRING);
    }

    /*
    @EventHandler
    public void onClickPet(PlayerInteractEntityEvent event){
        Entity entity = event.getRightClicked();
        if(PetManager.isPet(entity)){
            String signal = readSignal(event.getPlayer().getInventory().getItem(event.getHand()));
            if(signal != null){
                ActiveMob activeMob = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
                activeMob.signalMob(BukkitAdapter.adapt(event.getPlayer()),signal);
            }
        }
    }

     */

}
