package net.brian.mythicpet.pets;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.MythicPet;
import net.brian.mythicpet.api.PetManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PetManagerImpl implements PetManager {

    private final MythicPets plugin;
    private final Map<String, MythicPet> cachedPets = new HashMap<>();


    public PetManagerImpl(MythicPets plugin){
        this.plugin = plugin;
        reload();
    }


    @Override
    public Optional<MythicPet> getMythicPet(String id) {
        return Optional.ofNullable(cachedPets.get(id));
    }

    @Override
    public Set<String> getPetKeys() {
        return cachedPets.keySet();
    }

    @Override
    public Collection<MythicPet> getPets() {
        return cachedPets.values();
    }

    @Override
    public void reload() {
        cachedPets.clear();
        File directory = new File(plugin.getDataFolder()+File.separator+"pets");
        directory.mkdirs();
        File exampleFile = new File(plugin.getDataFolder()+File.separator+"pets"+File.separator+"ExamplePet.yml");
        if(!exampleFile.exists()){
            try {
                Files.copy(plugin.getResource("ExamplePet.yml"), exampleFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(File file : directory.listFiles()){
            Configuration config = YamlConfiguration.loadConfiguration(file);
            for(String key : config.getKeys(false)){
                ConfigurationSection section = config.getConfigurationSection(key);
                MythicPet model = new MythicPetImpl(key,section);
                cachedPets.put(key,model);
            }
        }

    }


}
