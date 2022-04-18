package net.brian.mythicpet.pet;


import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.MythicPet;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Set;


public class PetDirectory {
    private static final HashMap<String, MythicPet> petModels = new HashMap<>();


    public static void reload(){
        petModels.clear();

        File directory = new File(MythicPets.inst().getDataFolder()+File.separator+"pets");
        directory.mkdir();
        File exampleFile = new File(MythicPets.inst().getDataFolder()+"/pets/ExamplePet.yml");
        if(!exampleFile.exists()){
            try {
                Files.copy(MythicPets.inst().getResource("ExamplePet.yml"), exampleFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(File file : directory.listFiles()){
            Configuration config = YamlConfiguration.loadConfiguration(file);
            for(String key : config.getKeys(false)){
                ConfigurationSection section = config.getConfigurationSection(key);
                MythicPet model = new MythicPetImpl(key,section);
                petModels.put(key,model);
            }
        }
    }


    public static MythicPet getModel(String id){
        return petModels.get(id);
    }

    public static Set<String > getModels(){
        return petModels.keySet();
    }
}
