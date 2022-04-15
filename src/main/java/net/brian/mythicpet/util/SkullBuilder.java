package net.brian.mythicpet.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.brian.mythicpet.volatilecode.VolatileCodeHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullBuilder {
    public static ItemStack getSkull(String texture) {
        ItemStack item = null;
        if (VolatileCodeHandler.esLegacy()) {
            item = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short)3);
        } else {
            item = new ItemStack(Material.PLAYER_HEAD);
        }
        if (texture.isEmpty()) {
            return item;
        } else {
            SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
            GameProfile profile =  new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException var5) {
                var5.printStackTrace();
            }

            item.setItemMeta(skullMeta);
            return item;
        }
    }

}
