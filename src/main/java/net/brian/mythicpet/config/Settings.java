package net.brian.mythicpet.config;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.mythicpet.MythicPets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class Settings {


    public static boolean Mysql;
    public static boolean HealOnlevelUp;
    public static int DefaultPages;
    public static boolean FollowModeDamage;
    private static int inventoryRows;

    private static boolean inventoryEnabled = false;


    public static void reload(){
        MythicPets.inst().saveDefaultConfig();
        MythicPets.inst().reloadConfig();
        FileConfiguration config = MythicPets.inst().getConfig();
        HealOnlevelUp = config.getBoolean("HealOnlevelUp",true);
        DefaultPages = config.getInt("DefaultPages",1);
        Mysql = config.getBoolean("Mysql",false);
        FollowModeDamage = config.getBoolean("FollowModeDamage",false);
        inventoryRows = config.getInt("InventoryRows",1);

        inventoryEnabled = inventoryRows > 0 && inventoryRows<=6;
    }


    public static Inventory createPetInventory(UUID uuid){
        String name = Message.PetInventoryTitle;
        if(MythicPets.placeHolderAPI){
            Player player = Bukkit.getPlayer(uuid);
            if(player != null){
                name = PlaceholderAPI.setPlaceholders(player,name);
            }
        }
        return Bukkit.createInventory(null,inventoryRows*9,name);
    }

    public static boolean inventoryEnabled(){
        return inventoryEnabled;
    }

}
