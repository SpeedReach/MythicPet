package net.brian.mythicpet.pets;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import me.clip.placeholderapi.replacer.Replacer;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.MythicPet;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.api.SpawnResult;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.config.SystemIcon;
import net.brian.mythicpet.utils.MountPetUtil;
import net.brian.mythicpet.utils.NonePlayerReplace;
import net.brian.mythicpet.utils.PetDirectory;
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

public class PetImpl implements Pet, PostProcessable {


    public static final NamespacedKey ownerKey = new NamespacedKey(MythicPets.inst(),"owner");


    private final String type;
    private int exp=0,level=1;
    private double health = 20;


    //Legacy Variables useless
    private String name;


    private long deathTime = 0,despawnTimeStamp=0;


    private transient Player owner;
    private transient MythicPet petType;
    private transient PetTargetTable targetTable;
    private transient Entity petEntity;
    private transient boolean disabled;

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
        refreshUpstream();
        deathTime = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"deathTime"), PersistentDataType.LONG,0L);
        exp = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"exp"), PersistentDataType.INTEGER,0);
        level = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"level"), PersistentDataType.INTEGER,0);
        health = container.getOrDefault(new NamespacedKey(MythicPets.inst(),"health"), PersistentDataType.DOUBLE,20d);
    }



    @Override
    public SpawnResult spawn(Location location, Player owner) {
        if(disabled) return SpawnResult.PET_NOT_EXIST;
        if(isActive()) return SpawnResult.ALREADY_SPAWNED;

        TimeInfo respawnTimeInfo = getRecoverTime();
        if(respawnTimeInfo.getMillSec() > 0){
            owner.sendMessage(Message.InRespawnCooldown.replace("#remain_time#",respawnTimeInfo.getTime(TimeUnit.SECOND).toString()));
            return SpawnResult.COOLDOWN;
        }

        refreshStats();



        return MythicUtil.spawn(petType.getMythicMob(),owner.getLocation(),level).map(entity -> {
            petEntity = entity;
            targetTable = new PetTargetTable(owner,petEntity);

            petEntity.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING,owner.getUniqueId().toString());

            if(petEntity instanceof LivingEntity livingEntity){
                AttributeInstance healthAtt = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if(healthAtt != null){
                    healthAtt.setBaseValue(petType.getMaxHealth(level));
                }
                livingEntity.setHealth(health);
            }

            Bukkit.getScheduler().runTaskLater(MythicPets.inst(),()->{
                if(petEntity != null &&petEntity.getCustomName() != null){
                    petEntity.setCustomName(petEntity.getCustomName().replace("<mythicpet.owner>",owner.getName()));
                }

                if(petType.isMountOnly()){
                    MountPetUtil.mountPet(owner,petEntity, petType.getMountType());
                }

            },20);




            this.owner = owner;
            return SpawnResult.SUCCESS;
        }).orElse(SpawnResult.MOB_NOT_EXISTS);

    }

    @Override
    public void despawn() {
        targetTable = null;
        if(petEntity != null){
            petEntity.remove();
            petEntity = null;
            despawnTimeStamp = System.currentTimeMillis();
        }
    }

    @Override
    public ItemStack getIcon(Player owner) {
        if(disabled){
            SystemIcon.get("Disabled");
        }

        refreshStats();
        MythicPetImpl.Icon icon = petType.getIcon();
        ItemStack itemStack = new ItemStack(icon.mat);
        ItemMeta meta = icon.getMeta();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(MythicPets.inst(),"type"),PersistentDataType.STRING,type);
        container.set(new NamespacedKey(MythicPets.inst(),"deathTime"),PersistentDataType.LONG,deathTime);
        container.set(new NamespacedKey(MythicPets.inst(),"exp"),PersistentDataType.INTEGER,exp);
        container.set(new NamespacedKey(MythicPets.inst(),"level"),PersistentDataType.INTEGER,level);
        container.set(new NamespacedKey(MythicPets.inst(),"health"), PersistentDataType.DOUBLE,health);

        List<String> lores = new ArrayList<>();
        for (String line : icon.lores) {
            lores.add(parse(line,owner));
        }
        if(getRecoverTime().getMillSec() > 0){
            lores.add(Message.Dead);
        }

        meta.setLore(lores);
        meta.setDisplayName(parse(meta.getDisplayName(),owner));

        itemStack.setItemMeta(meta);
        return itemStack;
    }


    /**
     * Only for the EntityDeathEvent to trigger this.
     */
    @Override
    public void setDead() {
        targetTable = null;
        deathTime = System.currentTimeMillis();
        despawnTimeStamp = deathTime;
        if(petEntity != null){
            petEntity.remove();
            petEntity = null;
        }
    }


    @Override
    public Optional<Entity> getPetEntity() {
        return Optional.ofNullable(petEntity);
    }

    @Override
    public void addExp(int amount) {
        if(disabled) return;

        if(petType.getMaxLevel() <= level){
            return;
        }
        exp += amount;
        int previousLevel = level;
        while(exp > petType.getRequire(level+1)){
            exp-= petType.getRequire(level+1);
            level++;
            if(level == petType.getMaxLevel()){
                exp = petType.getRequire(level);
                break;
            }
        }
        if(level > previousLevel){
            MythicUtil.setLevel(petEntity,level);
            Bukkit.getScheduler().runTaskLater(MythicPets.inst(),()->{
                if(petEntity != null &&petEntity.getCustomName() != null){
                    petEntity.setCustomName(petEntity.getCustomName().replace("<mythicpet.owner>",owner.getName()));
                }
            },20);
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
        if(disabled) return false;

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
        if(disabled) return false;

        if(isActive()){
            return false;
        }
        else{
            return System.currentTimeMillis() - deathTime > petType.getRespawnCooldown().getMillSec();
        }
    }

    private boolean shouldHeal(){
        if(disabled) return false;

        TimeInfo respawnCoolDown = petType.getRespawnCooldown();
        long currentTime = System.currentTimeMillis();
        return currentTime - despawnTimeStamp > respawnCoolDown.getMillSec();
    }

    @Override
    public TimeInfo getRecoverTime() {
        if(disabled) return new TimeInfo(0);

        TimeInfo respawnCoolDown = petType.getRespawnCooldown();
        long currentTime = System.currentTimeMillis();
        if(shouldHeal()){
            return new TimeInfo(0);
        }
        else{
            if(currentTime - deathTime > respawnCoolDown.getMillSec()){
                return new TimeInfo(0);
            }
            return new TimeInfo(respawnCoolDown.getMillSec()-(currentTime-deathTime));
        }
    }


    @Override
    public void refreshUpstream() {
        MythicPets.getPetManager().getMythicPet(type).ifPresentOrElse(parent->{
            disabled = false;
            petType = parent;
        },()-> disabled = true);
    }


    public String parse(String s,Player player) {
        if(disabled) return s;

        int nextlevel_exp = petType.getRequire(level+1);
        int maxHealth = petType.getMaxHealth(level);
        if(petEntity != null && petEntity instanceof LivingEntity){
            AttributeInstance attributeInstance = ((LivingEntity) petEntity).getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if(attributeInstance != null){
                maxHealth = (int) attributeInstance.getValue();
            }
        }

        if(level >= petType.getMaxLevel()){
            s=s.replace("#current_exp# / #nextlevel_exp#", Message.MaxLevel);
        }
        else{
            s=s.replace("#current_exp#",String.valueOf(exp));
            s=s.replace("#nextlevel_exp#", Integer.toString(nextlevel_exp));
        }
        s=s.replace("#level#",String.valueOf(level));
        s=s.replace("#health#",String.valueOf(Math.round(health)));
        s=s.replace("#max_health#",String.valueOf(maxHealth));
        s=s.replace("#health_bar#",new BarBuilder(health,maxHealth,ChatColor.GREEN,20,'|').build());
        s=s.replace("#exp_bar#",new BarBuilder(exp,nextlevel_exp, ChatColor.YELLOW,10,'???').build());

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
    public String getPetID() {
        return type;
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
            double maxHealth  = petType.getMaxHealth(level);
            if(health == 0 && getRecoverTime().getMillSec() <= 0){
                health = maxHealth;
            }
            else if(shouldHeal()){
                health = maxHealth;
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
