package net.brian.mythicpet.pet;


import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import me.clip.placeholderapi.replacer.Replacer;
import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.event.PetSpawnEvent;
import net.brian.mythicpet.util.IridiumColorAPI;
import net.brian.mythicpet.util.MountPetUtil;
import net.brian.mythicpet.util.MythicPetLogger;
import net.brian.mythicpet.util.NonePlayerReplace;
import net.brian.playerdatasync.data.gson.PostProcessable;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;


public class Pet implements PostProcessable {

    public static final NamespacedKey ownerKey = new NamespacedKey(MythicPet.inst(),"owner");

    public String type;
    int saturation;
    String name;
    public double health;
    public double maxHealth;
    int maxSaturation;
    int exp;
    int level=1;
    public long deathTime;
    private HashMap<Integer,String> inventoryStringMap;


    transient Entity petEntity;
    transient PetModel petModel;
    transient PetTargetTable targetTable;

    public Pet(){
    }

    public Pet(String type){
        petModel = PetDirectory.getModel(type);
        this.type = type;
        name = petModel.display;
        if(!MythicUtil.mobExist(type)){
            MythicPetLogger.log(ChatColor.RED+"Correspond MythicMob "+petModel.mythicMob+" does not exist");
            maxHealth = 0;
        }else {
            maxHealth = MythicUtil.getDefaultHealth(type);
            health = maxHealth;
        }
    }

    public Pet(String type,int level,int exp,double maxHealth,double health,int saturation,long deathTime){
        this.type = type;
        this.deathTime = deathTime;
        this.level = level;
        this.maxHealth = maxHealth;
        this.health = health;
        this.saturation = saturation;
        this.exp = exp;
        petModel = PetDirectory.getModel(type);
        name = PetDirectory.getModel(type).display;
    }

    public ItemStack generateIcon(Player player){
        updateInfo();
        if(player == null){
            health = Math.min(health,maxHealth);
        }
        ItemStack icon = petModel.getIcon();
        ItemMeta meta = icon.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String s : meta.getLore()){
            s = replace(s, petModel,player);
            lore.add(s);
        }
        meta.setLore(lore);
        if(meta.hasDisplayName()){
            meta.setDisplayName(replace(meta.getDisplayName(), petModel,player));
        }
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"type"), PersistentDataType.STRING,type);
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"deathTime"), PersistentDataType.LONG,deathTime);
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"exp"), PersistentDataType.INTEGER,exp);
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"level"), PersistentDataType.INTEGER,level);
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"health"), PersistentDataType.DOUBLE,health);
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"maxHealth"), PersistentDataType.DOUBLE,maxHealth);
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"saturation"), PersistentDataType.INTEGER,saturation);
        meta.getPersistentDataContainer().set(new NamespacedKey(MythicPet.inst(),"UUID"), PersistentDataType.STRING, UUID.randomUUID().toString());
        icon.setItemMeta(meta);
        return icon;
    }

    public String replace(String s,PetModel model,Player player){
        s=replace(s,player);
        int nextlevel_exp = model.getRequire(level);
        if(nextlevel_exp == -1){
            s=s.replace("#current_exp# / #nextlevel_exp#", Message.MaxLevel);
        }
        else{
            s=s.replace("#current_exp#",String.valueOf(exp));
            s=s.replace("#nextlevel_exp#",String.valueOf(model.getRequire(level)));
        }
        s=s.replace("#level#",String.valueOf(level));
        s=s.replace("#health#",String.valueOf(Math.round(health)));
        s=s.replace("#max_health#",String.valueOf(Math.round(maxHealth)));
        s=s.replace("#health_bar#",healthBar(health,maxHealth));
        s=s.replace("#exp_bar#",String.valueOf(expBar(exp,model.getRequire(level))));
        int start = s.indexOf("#Math(")+6;
        int end = s.indexOf(")#");
        while(start<end && end>0 && start>0){
            String formula = s.substring(start,end);
            String[] math = formula.split(",");
            if(math.length==2){
                s=s.replace("#Math("+formula+")#",String.valueOf((Math.round(Double.parseDouble(math[0])+level*Double.parseDouble(math[1]))*10)/10.0));
            }
            else{
                s=s.replace("#Math("+formula+")#", String.valueOf(Math.round(new ExpressionBuilder(formula).build().evaluate()*10)/10));
            }
            start = s.indexOf("#Math(")+6;
            end = s.indexOf(")#");
        }
        return s;
    }

    public void despawn(){
        if(petEntity != null){
            petEntity.remove();
            petEntity = null;
        }
    }


    public String replace(String string,Player player){
        if(MythicPet.placeHolderAPI){
            if(player == null){
                NonePlayerReplace replacer = new NonePlayerReplace(Replacer.Closure.PERCENT);
                LocalExpansionManager var10003 = PlaceholderAPIPlugin.getInstance().getLocalExpansionManager();
                return replacer.apply(string,var10003::getExpansion);
            }
            return PlaceholderAPI.setPlaceholders(player,string);
        }
        else return string;
    }

    public String expBar(int current_exp,int nextlevel_exp){
        if(nextlevel_exp == -1) return ChatColor.YELLOW+"▋▋▋▋▋▋▋▋▋▋";
        StringBuilder bar = new StringBuilder();
        for(double i=0;i<nextlevel_exp;i+=(1.0*nextlevel_exp/10)){
            if(i<current_exp){
                bar.append(ChatColor.YELLOW + "▋");
            }
            else{
                bar.append(ChatColor.GRAY + "▋");
            }
        }
        return bar.toString();
    }

    public String healthBar(double health,double maxHealth){
        StringBuilder bar = new StringBuilder();
        for(double i=0;i<maxHealth;i+=(maxHealth /20)){
            if(i<health){
                bar.append(ChatColor.GREEN + "|");
            }
            else{
                bar.append(ChatColor.RED + "|");
            }
        }
        return bar.toString();
    }

    public boolean spawn(Player player){
        boolean success = spawn(player,player.getLocation());
        if(success){
            player.sendMessage(Message.Spawned.replace("#pet_name#", PetDirectory.getModel(type).display));
            return true;
        }
        else return false;
    }

    public boolean spawn(Player player,Location loc){
        return spawn(player,loc,petModel.mythicMob);
    }

    public boolean spawn(Player player, Location loc,String mythicMob){
        if(petEntity != null){
            if(!petEntity.isDead()){
                petEntity.remove();
            }
        }
        String alive = isAlive();
        if(!alive.equals("t")){
            String message = String.valueOf(Message.InRespawnCooldown);
            message = message.replace("#remain_time#",alive);
            player.sendMessage(message);
            return false;
        }
        if(!MythicUtil.mobExist(mythicMob)){
            player.sendMessage(ChatColor.RED+"Correspond MythicMob "+mythicMob+" does not exist");
            MythicPetLogger.log(ChatColor.RED+"Correspond MythicMob "+mythicMob+" does not exist");
            return false;
        }


        Entity entity = MythicUtil.spawn(mythicMob,player.getLocation(),level).get();
        petEntity = entity;
        MythicUtil.setOwner(entity,player.getUniqueId());
        if(entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.setMaxHealth(maxHealth);
            livingEntity.setHealth(Math.min(health,maxHealth));
        }
        petEntity.getPersistentDataContainer().set(ownerKey,PersistentDataType.STRING,player.getUniqueId().toString());

        if(petEntity.getCustomName() != null){
            petEntity.setCustomName(IridiumColorAPI.process(petEntity.getCustomName().replace("<mythicpet.owner>",player.getName())));
        }

        Bukkit.getPluginManager().callEvent(new PetSpawnEvent(player, this,petEntity));
        targetTable = new PetTargetTable(petEntity);
        Bukkit.getScheduler().runTaskLater(MythicPet.inst(),()->{
            if(petModel.isMountOnly()){
                MountPetUtil.mountPet(player,petEntity, petModel.mountType);
            }
        },20L);
        return true;
    }


    public PetModel getType(){
        return petModel;
    }


    public String isAlive(){
        if(health <= 0.0){
            long time = System.currentTimeMillis()-deathTime;
            if(time< petModel.respawnTime*1000){
                return String.valueOf(Math.ceil(petModel.respawnTime-time/1000.0));
            }
            else{
                health = maxHealth;
            }
        }
        return "t";
    }


    public void addExp(int amount){
        if(!petModel.levels.containsKey(level+1)){
            return;
        }
        exp += amount;
        while(exp > petModel.levels.get(level+1)){
            exp-= petModel.levels.get(level+1);
            level++;
            if(level == petModel.maxLevel){
                exp = petModel.levels.get(level);
                break;
            }
        }
    }

    public String getName(){
        return petModel.display;
    }

    public int getLevel() {
        return level;
    }

    private void updateInfo(){
        if(petEntity != null){
            if(!petEntity.isDead()){
                if(petEntity instanceof LivingEntity){
                    LivingEntity livingEntity = (LivingEntity) petEntity;
                    health = livingEntity.getHealth();
                    maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                }
            }
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Entity getPetEntity(){
        return petEntity;
    }

    public PetTargetTable getTargetTable() {
        return targetTable;
    }

    @Override
    public void gsonPostSerialize() {

    }

    @Override
    public void gsonPostDeserialize() {
        petModel = PetDirectory.getModel(type);
    }
}
