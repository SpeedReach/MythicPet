package net.brian.mythicpet.pet;

import net.brian.mythicpet.MythicPets;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Skin {

    private String mob;
    private String name;
    private ItemStack itemStack;
    private boolean needPerm;
    public static final NamespacedKey skinKey = new NamespacedKey(MythicPets.inst(),"skinKey");
    public static final NamespacedKey petKey = new NamespacedKey(MythicPets.inst(),"petKey");
    public static final NamespacedKey nameKey = new NamespacedKey(MythicPets.inst(),"nameKey");

    public Skin(String petType,String mob,String name,ItemStack itemStack,boolean needPerm){
        this.mob = mob;
        this.name = name;
        this.needPerm =needPerm;
        this.itemStack = itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(skinKey, PersistentDataType.STRING,mob);
        meta.getPersistentDataContainer().set(petKey,PersistentDataType.STRING,petType);
        meta.getPersistentDataContainer().set(nameKey,PersistentDataType.STRING,name);
        itemStack.setItemMeta(meta);
    }

    public boolean needPerm() {
        return needPerm;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getMob() {
        return mob;
    }

    public String getName() {
        return name;
    }
}
