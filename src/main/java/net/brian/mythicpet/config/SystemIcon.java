package net.brian.mythicpet.config;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.utils.ItemReader;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;

public class SystemIcon {
    public static HashMap<String,ItemStack> icons = new HashMap<>();



    public static void reload(){
        File file = new File(MythicPets.inst().getDataFolder(), "message.yml");
        Configuration config = YamlConfiguration.loadConfiguration(file);

        for(String key: config.getConfigurationSection("SystemIcon").getKeys(false)){
            icons.put(key, ItemReader.getItem(config.getConfigurationSection("SystemIcon."+key)));
        }
    }

    public static ItemStack get(String string){
        ItemStack itemStack = icons.get(string);
        if(itemStack != null){
            return itemStack.clone();
        }
        itemStack= new ItemStack(Material.STONE);
        ItemMeta meta =itemStack.getItemMeta();
        meta.setDisplayName(string);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
