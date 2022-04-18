package net.brian.mythicpet.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.util.Base64;

public class ItemStackBase64 {
    public static String getString(ItemStack item) {
        String itemString = null;
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();

            if (io.toByteArray().length > 65535) {
                throw new UTFDataFormatException();
            }

            itemString = Base64.getEncoder().encodeToString(io.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemString;
    }

    public static ItemStack getItem(String base64) {
        ItemStack item = null;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            item = (ItemStack) is.readObject();
        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();
        }
        return item;
    }

}
