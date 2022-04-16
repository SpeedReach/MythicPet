package net.brian.mythicpet.config;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.util.IridiumColorAPI;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Message {
    public static String StorageTitle;
    public static String ExpRecieve;
    public static String[] Help;
    public static String NoPermission;
    public static String InRespawnCooldown;
    public static String PetDied;
    public static String SwtichedToAttack;
    public static String SwitchedToDefense;
    public static String SwitchedToFollow;
    public static String InventoryFull;
    public static String Active;
    public static String MaxLevel;
    public static String Dead;
    public static String StorageFull;
    public static String Teleport;
    public static String Despawn;
    public static String Spawned;
    public static String PetInventoryTitle;
    public static String CannotEdit;
    public static String DataLoading;
    public static String PutToStorage;
    public static String CantRide;

    public static void reload(){
        File file = new File(MythicPet.inst().getDataFolder(), "message.yml");
        if(!file.exists()){
            MythicPet.inst().saveResource("message.yml",false);
        }
        Configuration config = YamlConfiguration.loadConfiguration(file);
        MaxLevel = IridiumColorAPI.process(config.getString("Max_Level","&e最大等級"));
        StorageTitle = IridiumColorAPI.process(config.getString("StorageTitle","&ePet Storage"));
        ExpRecieve = IridiumColorAPI.process(config.getString("ExpRecieve","+ #Exp#"));
        Help = IridiumColorAPI.process(config.getStringList("Help")).toArray(new String[0]);
        NoPermission = IridiumColorAPI.process(config.getString("NoPermission","&cYou dont have permission"));
        InRespawnCooldown = IridiumColorAPI.process(config.getString("InRespawnCooldown","Pet respawning"));
        PetDied = IridiumColorAPI.process(config.getString("PetDied","ur pet died"));
        SwtichedToAttack = IridiumColorAPI.process(config.getString("SwtichedToAttack","switched to attack"));
        SwitchedToDefense = IridiumColorAPI.process(config.getString("SwitchedToDefense","switched to defense"));
        SwitchedToFollow = IridiumColorAPI.process(config.getString("SwitchedToFollow","switched to follow"));
        InventoryFull = IridiumColorAPI.process(config.getString("InventoryFull","Inventory is full , can't add pet"));
        Active = IridiumColorAPI.process(config.getString("Active","Pet in use, click again to disable"));
        Dead = IridiumColorAPI.process(config.getString("Dead","&cpet is dead"));
        Despawn = IridiumColorAPI.process(config.getString("Despawn","You despawned your pet"));
        PetInventoryTitle = IridiumColorAPI.process(config.getString("PetInventoryTitle","&e%player_name% 的寵物背包"));
        StorageFull = IridiumColorAPI.process(config.getString("StorageFull","&cStorage Full"));
        Teleport = IridiumColorAPI.process(config.getString("Teleport","You teleported your pet to you"));
        Spawned = IridiumColorAPI.process(config.getString("Spawned","pet Spawned"));
        CannotEdit = IridiumColorAPI.process(config.getString("CannotEdit","This pet can't be taken off"));
        DataLoading = IridiumColorAPI.process(config.getString("DataLoading","&eYour data is still loading"));
        PutToStorage = IridiumColorAPI.process(config.getString("PutToStorage","You placed #pet_name# to storage"));
        CantRide = IridiumColorAPI.process(config.getString("CantRide","You can't ride this pet"));
    }



}
