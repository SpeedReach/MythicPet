package net.brian.mythicpet.config;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.util.ItemReader;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public class SystemIcon {
    public static HashMap<String,ItemStack> icons = new HashMap<>();


    public SystemIcon(){
        load();
    }

    public void load(){
        File file = new File(MythicPet.inst().getDataFolder(), "message.yml");
        Configuration config = YamlConfiguration.loadConfiguration(file);

        for(String key: config.getConfigurationSection("SystemIcon").getKeys(false)){
            icons.put(key, ItemReader.getItem(config.getConfigurationSection("SystemIcon."+key)));
        }
    }

    public static ItemStack get(String string){
        return icons.get(string).clone();
    }

}
