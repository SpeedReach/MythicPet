package net.brian.mythicpet.utils;

import net.brian.mythicpet.config.Message;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class AddPetStatus {

    public static void inUse(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();

        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,false);
        if(meta.hasLore()){
            List<String > lore = meta.getLore();
            lore.add(Message.Active);
            meta.setLore(lore);
        }
        else{
            List<String> lore = Collections.singletonList(Message.Active);
            meta.setLore(lore);
        }
        itemStack.setItemMeta(meta);
    }


}
