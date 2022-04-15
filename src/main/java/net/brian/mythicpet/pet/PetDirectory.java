package net.brian.mythicpet.pet;


import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public class PetDirectory {
    private static final HashMap<String, PetModel> petModels = new HashMap<>();


    public static void reload(){
        petModels.clear();

        File directory = new File(MythicPet.inst().getDataFolder()+File.separator+"pets");
        directory.mkdir();
        File exampleFile = new File(MythicPet.inst().getDataFolder()+"/pets/ExamplePet.yml");
        if(!exampleFile.exists()){
            try {
                Files.copy(MythicPet.inst().getResource("ExamplePet.yml"), exampleFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(File file : directory.listFiles()){
            Configuration config = YamlConfiguration.loadConfiguration(file);
            for(String key : config.getKeys(false)){
                ConfigurationSection section = config.getConfigurationSection(key);
                PetModel model = new PetModel(key,section);
                petModels.put(key,model);
            }
        }
    }


    public static PetModel getModel(String id){
        return petModels.get(id);
    }

    public static Set<String > getModels(){
        return petModels.keySet();
    }
}
