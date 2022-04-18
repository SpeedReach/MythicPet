package net.brian.mythicpet.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.brian.playerdatasync.util.IridiumColorAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class Icon {
    public final String display;
    public final String[] lores;
    public final String texture;
    public final int model;
    public final Material mat;

    private final ItemMeta meta;

    public Icon(ConfigurationSection section){
        if(section == null){
            meta = new ItemStack(Material.STONE).getItemMeta();
            display  ="";
            lores = new String[]{};
            texture = "";
            model = 0;
            mat = Material.STONE;
            return;
        }

        this.mat = Material.valueOf(section.getString("Material","STONE").toUpperCase());
        this.display = section.getString("Display","");
        this.lores = IridiumColorAPI.process(section.getStringList("Lores")).toArray(new String[0]);
        this.texture = section.getString("Texture");
        this.model = section.getInt("Model",0);


        meta  = new ItemStack(mat).getItemMeta();
        meta.setCustomModelData(model);
        meta.setDisplayName(IridiumColorAPI.process(display));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        if(texture != null && mat.equals(Material.PLAYER_HEAD)){
            SkullMeta skullMeta = (SkullMeta) meta;
            GameProfile profile =  new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException var5) {
                var5.printStackTrace();
            }
        }

    }

     public ItemMeta getMeta(){
        return meta.clone();
     }


}
