package net.brian.mythicpet;

import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.api.PetManager;
import net.brian.mythicpet.pets.PetImpl;
import net.brian.mythicpet.pets.PetManagerImpl;
import net.brian.mythicpet.command.CommandManager;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;
import net.brian.mythicpet.compatible.mythicmobs.latest.NewMythicUtil;
import net.brian.mythicpet.compatible.mythicmobs.latest.LatestMythicLoader;
import net.brian.mythicpet.compatible.mythicmobs.legacy.LegacyMythicLoader;
import net.brian.mythicpet.compatible.mythicmobs.legacy.LegacyMythicUtil;
import net.brian.mythicpet.config.InteractionGUIConfig;
import net.brian.mythicpet.petinteraction.RightClickPetListener;
import net.brian.mythicpet.petinteraction.SignalItem;
import net.brian.mythicpet.compatible.libdisguise.DisguiseListener;
import net.brian.mythicpet.compatible.mmoitems.MMOItemsLoader;
import net.brian.mythicpet.compatible.mmoitems.listener.MMOItemsHealPet;
import net.brian.mythicpet.compatible.pandeloot.PandeLootDamageRegister;
import net.brian.mythicpet.compatible.placeholder.PlaceholderManager;
import net.brian.mythicpet.compatible.protocollib.ProtocolLoader;
import net.brian.mythicpet.compatible.worldguard.WorldGuardSupport;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.config.Settings;
import net.brian.mythicpet.config.SystemIcon;
import net.brian.mythicpet.listener.*;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.scheduler.PetMovement;
import net.brian.mythicpet.storage.InteractionGUIService;
import net.brian.mythicpet.storage.StorageListener;
import net.brian.mythicpet.utils.MythicPetLogger;
import net.brian.playerdatasync.PlayerDataSync;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public final class MythicPets extends JavaPlugin {

    private static MythicPets plugin;
    private static InteractionGUIService interactionGUIService;
    public static boolean worldGuard = false;
    public WorldGuardSupport worldGuardFlag;
    public CommandManager commandManager;
    public MMOItemsLoader mmoitemsLoader;
    public static boolean mmoItems = false;
    public boolean pandeloot = false;
    public boolean protocolLib = false;
    public static boolean placeHolderAPI=false;
    public static boolean over17 = false;
    private static boolean modelEngine = false;
    private static final boolean premium = true;
    private static PetManager petManager;



    @Override
    public void onEnable() {
        over17 = false;
        plugin = this;
        loadSupports();
        PlayerDataSync.getInstance().register("mythicpet", PlayerPetProfile.class);

        loadManagers();
        registerEvents();

        petManager = new PetManagerImpl(this);

        commandManager = new CommandManager(this);

        reload();

        if(false){
            getServer().getPluginManager().registerEvents(new MMOItemsHealPet(this),this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onLoad(){
        Plugin wg = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if(wg != null) {
            worldGuard = true;
            worldGuardFlag =new WorldGuardSupport();
            worldGuardFlag.loadFlag();
            MythicPetLogger.log(ChatColor.GREEN+ "Enabled WorldGuard support");
        }
        Plugin mmoItemPlugin = Bukkit.getPluginManager().getPlugin("MMOItems");
        if(false){
            mmoitemsLoader = new MMOItemsLoader();
            mmoitemsLoader.load();
            MythicPetLogger.log(ChatColor.GREEN+ "Enabled MMOItems support");
        }
    }


    public static MythicPets inst(){
        return plugin;
    }


    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new StorageListener(),this);
        getServer().getPluginManager().registerEvents(new OwnerAction(),this);
        getServer().getPluginManager().registerEvents(new PetActions(),this);
        getServer().getPluginManager().registerEvents(new BlockDamage(),this);
        getServer().getPluginManager().registerEvents(new SignalItem(),this);
        getServer().getPluginManager().registerEvents(new RedeemPet(),this);
        getServer().getPluginManager().registerEvents(new IgnoreProjectile(),this);
        getServer().getPluginManager().registerEvents(new PetTargetListener(this),this);
        if(premium){
            getServer().getPluginManager().registerEvents(new RightClickPetListener(this),this);
            getServer().getPluginManager().registerEvents(interactionGUIService,this);
        }
    }


    public static void reload(){
        Settings.reload();
        Message.reload();
        SystemIcon.reload();
        InteractionGUIConfig.reload();
        petManager.reload();
        for (PlayerPetProfile profile : PlayerDataSync.getInstance().getPlayerDatas().getTable((PlayerPetProfile.class)).cacheData.values()) {
            profile.pets.forEach(Pet::refreshUpstream);
        }
    }

    public void loadManagers(){
        PetMovement movement = new PetMovement();
        movement.onTick();
        interactionGUIService = new InteractionGUIService();
    }





    public static Optional<PlayerPetProfile> getPlayer(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid, PlayerPetProfile.class);
    }


    public static boolean isLoaded(Player player){
        return PlayerDataSync.isLoaded(player);
    }


    private void loadSupports(){
        try {
            Class.forName("io.lumine.xikage.mythicmobs.MythicMobs");
            MythicPetLogger.log(ChatColor.YELLOW+"Detected MythicMobs version < 5.0.2");
            MythicPetLogger.log(ChatColor.YELLOW+"Enabling MythicMobs legacy support");
            MythicUtil.instance[0] = new LegacyMythicUtil();
            new LegacyMythicLoader(this);
        } catch (ClassNotFoundException e) {
            MythicPetLogger.log(ChatColor.YELLOW+"Detected MythicMobs version >= 5.0.2");
            MythicPetLogger.log(ChatColor.YELLOW+"Enabling MythicMobs latest support");
            MythicUtil.instance[0] = new NewMythicUtil();
            new LatestMythicLoader(this);
        }


        Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if(placeholderAPI != null){
            placeHolderAPI = true;
            new PlaceholderManager().register();
            MythicPetLogger.log(ChatColor.GREEN+ "Enabled PlaceholderAPI support");
        }
        Plugin pandeLoot = Bukkit.getPluginManager().getPlugin("PandeLoot");
        if(pandeLoot != null){
            pandeloot = true;
            getServer().getPluginManager().registerEvents(new PandeLootDamageRegister(),this);
            MythicPetLogger.log(ChatColor.GREEN+ "Enabled PandeLoot support");
        }
        Plugin protocolLibPlugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        if(protocolLibPlugin != null){
            protocolLib=true;
            ProtocolLoader.load();
        }
        Plugin libDis = Bukkit.getPluginManager().getPlugin("LibsDisguises");
        if(libDis != null) {
            Bukkit.getPluginManager().registerEvents(new DisguiseListener(), this);
            MythicPetLogger.log(ChatColor.GREEN + "Enabled LibsDisguises support");
        }
        if(Bukkit.getPluginManager().getPlugin("ModelEngine") != null){
            modelEngine = true;
        }

    }


    public WorldGuardSupport getWorldGuardSupport() {
        return worldGuardFlag;
    }


    public static boolean hasModelEngine() {
        return modelEngine;
    }

    public static InteractionGUIService getInteractionGUIService() {
        return interactionGUIService;
    }

    public static PetManager getPetManager(){
        return petManager;
    }

}
