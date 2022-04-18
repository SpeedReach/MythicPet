package net.brian.mythicpet.pet;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import me.clip.placeholderapi.replacer.Replacer;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.Icon;
import net.brian.mythicpet.api.MythicPet;
import net.brian.mythicpet.api.SpawnResult;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.utils.MountPetUtil;
import net.brian.mythicpet.utils.NonePlayerReplace;
import net.brian.mythicpet.utils.bar.BarBuilder;
import net.brian.mythicpet.utils.time.TimeInfo;
import net.brian.mythicpet.utils.time.TimeUnit;
import net.brian.playerdatasync.data.gson.PostProcessable;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetImpl implements net.brian.mythicpet.api.Pet, PostProcessable {


    public static final NamespacedKey ownerKey = new NamespacedKey(MythicPets.inst(),"owner");

    private final String type;
    private int exp=0,level=1;
    private double health = 20;

    private long deathTimeStamp=0,despawnTimeStamp=0;


    private final transient MythicPet petType;
    private transient PetTargetTable targetTable;
    private transient Entity petEntity;

    public PetImpl(MythicPet type){
        this(type,1);
    }

    public PetImpl(MythicPet type, int level) {
        this.type = type.getID();
        this.petType = type;
        health = type.getMaxHealth(level);
        this.level = level;
        this.exp = 0;
    }

    public PetImpl(ItemStack itemStack){
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        type = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"type"), PersistentDataType.STRING,"");
        petType = PetDirectory.getModel(type);
        deathTimeStamp = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"deathTime"), PersistentDataType.LONG,0L);
        exp = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"exp"), PersistentDataType.INTEGER,0);
        level = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"level"), PersistentDataType.INTEGER,0);
        health = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"health"), PersistentDataType.DOUBLE,20d);
    }



    @Override
    public SpawnResult spawn(Location location, Player owner) {
        if(isActive()) return SpawnResult.ALREADY_SPAWNED;
        TimeInfo respawnTimeInfo = getRecoverTime();
        if(respawnTimeInfo.getMillSec() > 0){
            owner.sendMessage(Message.InRespawnCooldown.replace("#remain_time#",respawnTimeInfo.getTime(TimeUnit.SECOND).toString()));
            return SpawnResult.COOLDOWN;
        }

        refreshStats();



        return MythicUtil.spawn(type,owner.getLocation(),level).map(entity -> {
            petEntity = entity;
            targetTable = new PetTargetTable(petEntity);

            petEntity.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING,owner.getUniqueId().toString());

            if(petEntity instanceof LivingEntity livingEntity){
                livingEntity.setHealth(health);
                AttributeInstance healthAtt = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if(healthAtt != null){
                    healthAtt.setBaseValue(petType.getMaxHealth(level));
                }
            }

            Bukkit.getScheduler().runTaskLater(MythicPets.inst(),()->{
                if(petEntity.getCustomName() != null){
                    petEntity.setCustomName(petEntity.getCustomName().replace("<mythicpet.owner>",owner.getName()));
                }

                if(petType.isMountOnly()){
                    MountPetUtil.mountPet(owner,petEntity, petType.getMountType());
                }

            },20);




            return SpawnResult.SUCCESS;
        }).orElse(SpawnResult.NOT_EXIST);

    }

    @Override
    public void despawn() {
        targetTable = null;
        if(petEntity != null){
            petEntity.remove();
            despawnTimeStamp = System.currentTimeMillis();
        }
    }

    @Override
    public ItemStack getIcon(Player owner) {
        Icon icon = petType.getIcon();
        ItemStack itemStack = new ItemStack(icon.mat);
        ItemMeta meta = icon.getMeta();
        List<String> lores = new ArrayList<>();
        for (String line : icon.lores) {
            lores.add(parse(line,owner));
        }
        if(getRecoverTime().getMillSec() > 0){
            lores.add(Message.Dead);
        }

        meta.setLore(lores);
        itemStack.setItemMeta(meta);
        return itemStack;
    }


    /**
     * Only for the EntityDeathEvent to trigger this.
     */
    @Override
    public void setDead() {
        targetTable = null;
        if(petEntity != null){
            petEntity.remove();
            petEntity = null;
            deathTimeStamp = System.currentTimeMillis();
            despawnTimeStamp = deathTimeStamp;
        }
    }


    @Override
    public Optional<Entity> getPetEntity() {
        return Optional.ofNullable(petEntity);
    }

    @Override
    public void addExp(int amount) {
        if(petType.getMaxLevel() <= level){
            return;
        }
        exp += amount;
        while(exp > petType.getRequire(level+1)){
            exp-= petType.getRequire(level+1);
            level++;
            if(level == petType.getMaxLevel()){
                exp = petType.getRequire(level);
                break;
            }
        }
    }

    @Override
    public void setLevel(int level) {
        this.level = Math.min(petType.getMaxLevel(),level);
    }

    @Override
    public int getLevel(){
        return level;
    }

    /**
     * Check whether the pet is spawned and if the entity is alive
     */
    @Override
    public boolean isActive() {
        if(petEntity != null){
            if(petEntity.isValid()){
                return true;
            }
            else{
                setDead();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canSpawn() {
        if(isActive()){
            return false;
        }
        else{
            return System.currentTimeMillis() - deathTimeStamp > petType.getRespawnCooldown().getMillSec();
        }
    }

    @Override
    public TimeInfo getRecoverTime() {
        return new TimeInfo(Math.min(0,System.currentTimeMillis()-despawnTimeStamp));
    }

    @Override
    public void refreshUpstream() {
        //type = PetDirectory.getModel(petType);
    }


    public String parse(String s,Player player) {
        int nextlevel_exp = petType.getRequire(level);
        int maxHealth = petType.getMaxHealth(level);

        if(nextlevel_exp == -1){
            s=s.replace("#current_exp# / #nextlevel_exp#", Message.MaxLevel);
        }
        else{
            s=s.replace("#current_exp#",String.valueOf(exp));
            s=s.replace("#nextlevel_exp#", Integer.toString(nextlevel_exp));
        }
        s=s.replace("#level#",String.valueOf(level));
        s=s.replace("#health#",String.valueOf(Math.round(health)));
        s=s.replace("#max_health#",String.valueOf(petType.getMaxHealth(level)));
        s=s.replace("#health_bar#",new BarBuilder(health,maxHealth).build());
        s=s.replace("#exp_bar#",new BarBuilder(exp,nextlevel_exp, ChatColor.YELLOW,10,'▋').build());

        int start = s.indexOf("#Math(")+6;
        int end = s.indexOf(")#");
        while(start<end && end>0 && start>0){
            String formula = s.substring(start,end);
            String[] math = formula.split(",");
            if(math.length==2){
                s=s.replace("#Math("+formula+")#",Double.toString((Math.round(Double.parseDouble(math[0])+level*Double.parseDouble(math[1]))*10)/10.0));
            }
            else{
                s=s.replace("#Math("+formula+")#", Double.toString(Math.round(new ExpressionBuilder(formula).build().evaluate()*10)/10.0));
            }
            start = s.indexOf("#Math(")+6;
            end = s.indexOf(")#");
        }

        if(MythicPets.placeHolderAPI){
            if(player == null){
                NonePlayerReplace replacer = new NonePlayerReplace(Replacer.Closure.PERCENT);
                LocalExpansionManager var10003 = PlaceholderAPIPlugin.getInstance().getLocalExpansionManager();
                s = replacer.apply(s,var10003::getExpansion);
            }
            s =  PlaceholderAPI.setPlaceholders(player,s);
        }

        return s;
    }

    @Override
    public MythicPet getPetType() {
        return petType;
    }

    @Override
    public PetTargetTable getTargetTable() {
        return targetTable;
    }


    /**
     * Check whether the pet is alive, update its health to the pet instance
     */
    private void refreshStats(){
        if(isActive()){
            if(petEntity instanceof LivingEntity livingEntity && livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null){
                health = livingEntity.getHealth();
            }
        }
        else{
            if(getRecoverTime().getMillSec() <= 0){
                health = petType.getMaxHealth(level);
            }
        }
        health = Math.max(0,health);
    }

    @Override
    public void gsonPostSerialize() {

    }

    @Override
    public void gsonPostDeserialize() {
        refreshUpstream();
    }
}
