package net.brian.mythicpet.util;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.pet.Pet;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

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

    public static Pet getPet(ItemStack itemStack){
        if(itemStack == null) return null;
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if(container.has(new NamespacedKey(MythicPet.inst(),"type"),PersistentDataType.STRING)){
            int exp = container.getOrDefault(new NamespacedKey(MythicPet.inst(),"exp"), PersistentDataType.INTEGER,0);
            int level = container.getOrDefault(new NamespacedKey(MythicPet.inst(),"level"), PersistentDataType.INTEGER,0);
            String type = container.get(new NamespacedKey(MythicPet.inst(),"type"), PersistentDataType.STRING);
            double health = container.getOrDefault(new NamespacedKey(MythicPet.inst(),"health"), PersistentDataType.DOUBLE,20.0);
            int saturation = container.getOrDefault(new NamespacedKey(MythicPet.inst(),"saturation"), PersistentDataType.INTEGER,20);
            long deathTime = container.getOrDefault(new NamespacedKey(MythicPet.inst(),"deathTime"),PersistentDataType.LONG,0L);
            double maxHealth = container.getOrDefault(new NamespacedKey(MythicPet.inst(),"maxHealth"), PersistentDataType.DOUBLE,20.0);
            return new Pet(type,level,exp,maxHealth,health,saturation,deathTime);
        }
        return null;
    }

    public static boolean isPet(ItemStack item){
        if(item!=null){
            if(item.hasItemMeta()){
                return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(MythicPet.inst(), "type"), PersistentDataType.STRING);
            }
        }
        return false;
    }

}
