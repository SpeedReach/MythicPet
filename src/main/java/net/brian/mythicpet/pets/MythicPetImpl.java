package net.brian.mythicpet.pets;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.brian.mythicpet.api.MythicPet;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.utils.pattern.IridiumColorAPI;
import net.brian.mythicpet.utils.time.TimeInfo;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

public class MythicPetImpl implements MythicPet {

    private final String petID;
    private final HashMap<Integer,Integer> levels = new HashMap<>();
    private final boolean mountOnly,mountAble;
    private final String mountType;
    private final Icon icon;
    private final long respawnTime;
    private final boolean editable;
    private final int maxLevel;
    private final String display;
    private final String mythicMob;

    public MythicPetImpl(String petID, ConfigurationSection section){
        this.petID = petID;

        mountType = section.getString("MountType","walking");
        mountOnly = section.getBoolean("MountOnly",false);
        icon = new Icon(section.getConfigurationSection("Icon"));
        respawnTime = section.getLong("RespawnTime",0);
        editable = section.getBoolean("Editable",true);
        maxLevel = section.getInt("MaxLevel",50);
        display = IridiumColorAPI.process(section.getString("Display",""));
        mountAble = section.getBoolean("Mountable",true);
        mythicMob = section.getString("MythicMob");
        setLevelRequire(section.getString("Level-RequireMent","10*#level#"));
    }

    private void setLevelRequire(String s){
        levels.put(1,0);
        for(int i=2;i<=maxLevel;i++){
            String formula = String.valueOf(s).replaceAll("#level#",String.valueOf(i));
            levels.put(i, (int) new ExpressionBuilder(formula).build().evaluate());
        }
    }



    @Override
    public String getID() {
        return petID;
    }

    @Override
    public String getMythicMob() {
        return mythicMob;
    }

    @Override
    public int getRequire(int level) {
        return levels.getOrDefault(level,-1);
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public TimeInfo getRespawnCooldown() {
        return new TimeInfo(respawnTime*1000);
    }

    @Override
    public int getMaxHealth(int level) {
        return (int) MythicUtil.getDefaultHealth(mythicMob,level);
    }

    @Override
    public boolean isMountOnly() {
        return mountOnly;
    }

    @Override
    public String getMountType() {
        return mountType;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public String getDisplayName() {
        return display;
    }

    @Override
    public boolean isMountable() {
        return mountAble;
    }

    @Override
    public boolean canEdit() {
        return editable;
    }

    public static class Icon {
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
            this.lores = net.brian.playerdatasync.util.IridiumColorAPI.process(section.getStringList("Lore")).toArray(new String[0]);
            this.texture = section.getString("Texture");
            this.model = section.getInt("Model",0);


            meta  = new ItemStack(mat).getItemMeta();
            meta.setCustomModelData(model);
            meta.setDisplayName(net.brian.playerdatasync.util.IridiumColorAPI.process(display));
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

}
