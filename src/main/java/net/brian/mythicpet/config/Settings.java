package net.brian.mythicpet.config;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.mythicpet.MythicPets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class Settings {

    public Settings(){
        setUp();
    }
    public static boolean Mysql;
    public static boolean HealOnlevelUp;
    public static int DefaultPages;
    public static boolean FollowModeDamage;
    private int inventoryRows;

    private boolean inventoryEnabled = false;

    public void setUp(){
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


    public Inventory createPetInventory(UUID uuid){
        String name = Message.PetInventoryTitle;
        if(MythicPets.placeHolderAPI){
            Player player = Bukkit.getPlayer(uuid);
            if(player != null){
                name = PlaceholderAPI.setPlaceholders(player,name);
            }
        }
        return Bukkit.createInventory(null,inventoryRows*9,name);
    }

    public boolean inventoryEnabled(){
        return inventoryEnabled;
    }

}
