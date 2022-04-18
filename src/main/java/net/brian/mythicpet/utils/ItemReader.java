package net.brian.mythicpet.utils;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.utils.pattern.IridiumColorAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemReader {

    public static ItemStack getItem(ConfigurationSection section){
        if(section == null) return new ItemStack(Material.BARRIER);
        ItemStack itemStack;
        Material mat = Material.valueOf(section.getString("Material","STONE").toUpperCase());
        itemStack = new ItemStack(mat);
        if(mat.equals(Material.PLAYER_HEAD)||mat.equals(Material.LEGACY_SKULL_ITEM)){
            itemStack = SkullBuilder.getSkull(section.getString("Texture",""));
        }
        ItemMeta meta = itemStack.getItemMeta();
        String display = section.getString("Display","");
        meta.setLore(IridiumColorAPI.process(section.getStringList("Lore")));
        meta.setDisplayName(IridiumColorAPI.process(display));
        meta.setCustomModelData(section.getInt("Model",0));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    public static boolean isPet(ItemStack item){
        if(item!=null){
            if(item.hasItemMeta()){
                return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(MythicPets.inst(), "type"), PersistentDataType.STRING);
            }
        }
        return false;
    }

}
