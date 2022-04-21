package net.brian.mythicpet.legacy;

import net.brian.mythicpet.pets.Skin;
import net.brian.mythicpet.utils.ItemReader;
import net.brian.mythicpet.utils.pattern.IridiumColorAPI;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetModel {

    HashMap<Integer,Integer> levels = new HashMap<>();
    private boolean mountable = true;
    String mythicMob;
    public String display;
    Long respawnTime;
    int maxLevel;
    private ItemStack icon;
    boolean editable;
    private final String internalName;

    final List<Skin> skinList;

    boolean mountOnly = false;
    String mountType;


    public PetModel(String internalName,ConfigurationSection section){
        this.internalName = internalName;
        maxLevel = section.getInt("MaxLevel",50);
        setLevelRequire(section.getString("Level-RequireMent","10*#level#"));
        mythicMob = section.getString("MythicMob");
        display = IridiumColorAPI.process(section.getString("Display",""));
        respawnTime = section.getLong("RespawnTime",0);
        editable = section.getBoolean("Editable",true);
        icon = ItemReader.getItem(section.getConfigurationSection("Icon"));
        ConfigurationSection skinSection = section.getConfigurationSection("Skin");
        mountable = section.getBoolean("Mountable");
        skinList = new ArrayList<>();
        mountOnly = section.getBoolean("MountOnly");
        mountType = section.getString("MountType","walking");
        if(skinSection != null){
            for(String key:skinSection.getKeys(false)){
                ConfigurationSection tSection = skinSection.getConfigurationSection(key);
                Skin skin = new Skin(internalName,key,IridiumColorAPI.process(tSection.getString("name","")),ItemReader.getItem(tSection.getConfigurationSection("Icon")),tSection.getBoolean("NeedPermission",true));
                skinList.add(skin);
            }
        }
    }

    public void setLevelRequire(String s){
        levels.put(1,0);
        for(int i=2;i<=maxLevel;i++){
            String formula = String.valueOf(s).replaceAll("#level#",String.valueOf(i));
            levels.put(i, (int) new ExpressionBuilder(formula).build().evaluate());
        }
    }

    public int getRequire(int level){
        return levels.getOrDefault(level+1,-1);
    }

    public ItemStack getIcon(){
        return icon.clone();
    }

    public boolean canEdit(){
        return editable;
    }

    public List<Skin> getSkinList(){
        return skinList;
    }

    public boolean isMountable() {
        return mountable;
    }

    public String getInternalName() {
        return internalName;
    }

    public boolean isMountOnly() {
        return mountOnly;
    }

    public String getMountType() {
        return mountType;
    }
}
