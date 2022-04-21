package net.brian.mythicpet.utils;


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


    public static MythicPet getModel(String id){
        return MythicPets.getPetManager().getMythicPet(id).get();
    }

    public static Set<String > getModels(){
        return MythicPets.getPetManager().getPetKeys();
    }

}
