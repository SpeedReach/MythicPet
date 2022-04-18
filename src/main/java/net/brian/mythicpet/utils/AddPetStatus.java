package net.brian.mythicpet.utils;

import net.brian.mythicpet.config.Message;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AddPetStatus {

    public static void inUse(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        List<String > lore = meta.getLore();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,false);
        lore.add(Message.Active);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

    }

    public static void isDead(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        List<String > lore = meta.getLore();
        lore.add(Message.Dead);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

}
