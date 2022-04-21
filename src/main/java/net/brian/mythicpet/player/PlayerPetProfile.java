package net.brian.mythicpet.player;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.api.SpawnResult;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.config.Settings;
import net.brian.mythicpet.event.PetLevelUpEvent;
import net.brian.mythicpet.pets.PetImpl;
import net.brian.mythicpet.utils.ItemStackBase64;
import net.brian.playerdatasync.PlayerDataSync;
import net.brian.playerdatasync.data.PlayerData;
import net.brian.playerdatasync.data.gson.PostProcessable;
import net.brian.playerdatasync.data.gson.QuitProcessable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


import java.util.*;

public class PlayerPetProfile extends PlayerData implements QuitProcessable, PostProcessable {

    public List<PetImpl> pets = new ArrayList<>();;
    int storageSize = Settings.DefaultPages;
    public String mode = Mode.FOLLOW;

    private HashMap<Integer,String> petStringInventory = new HashMap<>();

    private int currentIndex = -1;

    transient Pet currentPet;
    transient Inventory petInventory;
    transient Player player;

    public PlayerPetProfile(UUID uuid) {
        super(uuid);
        petInventory = MythicPets.inst().getSettings().createPetInventory(uuid);
        player = Bukkit.getPlayer(uuid);
    }

    public int maxPage(){
        return storageSize;
    }

    public Pet getPet(int index){
        return pets.get(index);
    }

    public void despawnPet(){
        if(currentPet != null){
            String message = Message.Despawn;
            getPlayer().sendMessage(message);
            currentPet.despawn();
        }
        currentPet = null;
    }

    public UUID getUUID(){
        return uuid;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public void spawnPet(Integer index){
        despawnPet();
        Pet pet = pets.get(index);
        Player player = Bukkit.getPlayer(uuid);

        SpawnResult spawnResult = pet.spawn(player);
        if(spawnResult.equals(SpawnResult.SUCCESS)){
            player.sendMessage(Message.Spawned.replace("#pet_name#",pet.getPetType().getDisplayName()));
            currentPet = pet;
        }
        else {
            player.sendMessage("Failed spawn because of "+spawnResult.name());
        }
    }

    public void addExp(int amount){
        if(currentPet == null) return;
        Pet pet = currentPet;
        if(MythicPets.mmoItems){
            double multiplier = net.Indyuce.mmoitems.api.player.PlayerData.get(uuid).getStats().getStat(MythicPets.inst().mmoitemsLoader.mmo_pet_exp_multiplier);
            amount = (int)(amount*(1+0.01*(multiplier)));
        }
        int previousLevel = pet.getLevel();
        pet.addExp(amount);
        if(pet.getLevel()>previousLevel){
            PetLevelUpEvent levelUpEvent = new PetLevelUpEvent(this,pet);
            Bukkit.getServer().getPluginManager().callEvent(levelUpEvent);
        }
    }
    public static Optional<PlayerPetProfile> get(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid, PlayerPetProfile.class);
    }

    public boolean hasActive(){
        return getCurrentPet().isPresent();
    }

    public Optional<Pet> getCurrentPet(){
        if(currentPet != null) {
            if(!currentPet.isActive()){
                currentPet = null;
            }
        }
        return Optional.ofNullable(currentPet);
    }


    public void setMode(String string){
        mode = string;
        getCurrentPet().ifPresent(pet -> pet.getTargetTable().clear());
    }

    @Override
    public void onQuit() {
        if(currentPet != null){
            currentPet.despawn();
        }
    }


    @Override
    public void gsonPostSerialize() {

        getCurrentPet().ifPresentOrElse(pet -> {
            currentIndex = pets.indexOf(pet);
        },()->currentIndex = -1);

        if(!MythicPets.over17){
            int size = petInventory.getSize();
            for(int i=0;i<size;i++){
                petStringInventory.put(i, ItemStackBase64.getString(petInventory.getItem(i)));
            }
        }

    }

    @Override
    public void gsonPostDeserialize() {
        player = Bukkit.getPlayer(uuid);
        if(player == null){
            return;
        }

        if(currentIndex != -1){
            spawnPet(currentIndex);
        }
        if(!MythicPets.over17){
            if(petStringInventory == null) petStringInventory = new HashMap<>();
            petInventory = MythicPets.inst().getSettings().createPetInventory(uuid);
            for(int i=0;i<petInventory.getSize();i++){
                petInventory.setItem(i,ItemStackBase64.getItem(petStringInventory.get(i)));
            }
        }
    }

    public Inventory getPetInventory(){
        return petInventory;
    }

    public void removePet(Pet pet){
        if(hasActive()){
            if(currentPet.equals(pet)){
                despawnPet();
            }
        }
        pets.remove(pet);
    }

    public void removePet(int index){
        removePet(pets.get(index));
    }
}
