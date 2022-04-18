package net.brian.mythicpet.config;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.utils.pattern.IridiumColorAPI;
import net.brian.mythicpet.utils.ItemReader;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class InteractionGUIConfig {

    private final int[] skinSlots = new int[]{13,40, 43, 37};

    ItemStack ride,storage,despawn;
    String storageName;
    boolean enabled,shift;

    public InteractionGUIConfig(){
        File configFile = new File(MythicPets.inst().getDataFolder()+"/InteractionGUI.yml");
        if(!configFile.exists()){
            MythicPets.inst().saveResource("InteractionGUI.yml",false);
        }
        FileConfiguration section = YamlConfiguration.loadConfiguration(configFile);
        storageName = IridiumColorAPI.process(section.getString("GuiName",""));
        ride = ItemReader.getItem(section.getConfigurationSection("Mount"));
        storage = ItemReader.getItem(section.getConfigurationSection("BackPack"));
        despawn = ItemReader.getItem(section.getConfigurationSection("Despawn"));
        enabled = section.getBoolean("Enabled",true);
        shift = section.getBoolean("Shift",true);
    }

    public Inventory createGUI(HumanEntity player) {
        Inventory inv;
        if (MythicPets.placeHolderAPI) {
            inv = Bukkit.createInventory(null, 54, PlaceholderAPI.setPlaceholders((OfflinePlayer) player, storageName));
        } else {
            inv = Bukkit.createInventory(null, 54, storageName);
        }
        PlayerPetProfile.get(player.getUniqueId()).flatMap(PlayerPetProfile::getCurrentPet).ifPresent(pet -> {
            inv.setItem(13, pet.getIcon((Player) player));
            inv.setItem(37, ride);
            inv.setItem(40, storage);
            inv.setItem(43, despawn);
        });
        return inv;
    }


    public boolean isEnabled() {
        return enabled;
    }
}
