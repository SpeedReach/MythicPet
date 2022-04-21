package net.brian.mythicpet.storage;

import net.brian.mythicpet.api.Pet;
import net.brian.mythicpet.config.Message;
import net.brian.mythicpet.config.SystemIcon;
import net.brian.mythicpet.utils.PetDirectory;
import net.brian.mythicpet.pets.PetImpl;
import net.brian.mythicpet.player.PlayerPetProfile;
import net.brian.mythicpet.utils.AddPetStatus;
import net.brian.mythicpet.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;

public class PetStorage {
    int page = 1;
    public HashMap<Integer, Inventory> window = new HashMap<>();
    static final int[] available = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
    final int[] grayGlass = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,53};
    public boolean editMode = false;
    public PetStorage(PlayerPetProfile playerData){
        Player player = Bukkit.getPlayer(playerData.getUUID());
        StorageManager.storageMap.put(playerData.getUUID(),this);
        Inventory page = generateEmptyPage();
        window.put(1,page);
        int index = 0;
        int p = 1;
        Iterator<PetImpl> pets = playerData.pets.iterator();
        while(pets.hasNext()){
            Pet pet = pets.next();
            Pair<Integer,Integer> slot = getRawSlot(index);
            if(!window.containsKey(slot.fst())){
                page = generateEmptyPage();
                p++;
                window.put(p,page);
            }
            if(PetDirectory.getModels().contains(pet.getPetType().getID())){
                ItemStack itemStack = pet.getIcon(player);
                if(playerData.getCurrentPet().isPresent()){
                    if(pet.equals(playerData.getCurrentPet().get())){
                        AddPetStatus.inUse(itemStack);
                    }
                }
                page.setItem(slot.snd(), itemStack);
                index++;
            }
            else{
                pets.remove();
            }
        }

    }

    public Inventory generateEmptyPage(){
        Inventory inv = Bukkit.createInventory(null,54, Message.StorageTitle);
        inv.setItem(46, SystemIcon.get("PreviousPage"));
        inv.setItem(47, SystemIcon.get("Teleport"));
        inv.setItem(48, SystemIcon.get("ToggleOnEdit"));
        inv.setItem(49, SystemIcon.get("DefenseMode"));
        inv.setItem(50, SystemIcon.get("AttackMode"));
        inv.setItem(51, SystemIcon.get("FollowMode"));
        inv.setItem(52, SystemIcon.get("NextPage"));

        for(int slot:grayGlass){
            inv.setItem(slot, SystemIcon.get("BackGround"));
        }
        return inv;
    }

    public void nextPage(HumanEntity player){
        if(window.containsKey(page+1)){
            page++;
            player.openInventory(window.get(page));
        }
    }

    public void previousPage(HumanEntity player){
        if(window.containsKey(page-1)){
            page--;
            player.openInventory(window.get(page));
        }
    }

    public static int getSlot(int slot,int page){
        if(slot<17 && 9<slot){
            return slot-10+28*(page-1);
        }
        if(18<slot&&slot<26){
            return slot-12+28*(page-1);
        }
        if(27<slot&&slot<35){
            return slot-14+28*(page-1);
        }
        if(36<slot&&slot<44){
            return slot-16+28*(page-1);
        }
        else return -1;
    }

    public void toggleEdit(){
        editMode = !editMode;
        ItemStack icon;
        if(editMode){
            icon = SystemIcon.get("ToggleOffEdit");
        }
        else{
            icon = SystemIcon.get("ToggleOnEdit");
        }
        window.forEach((page,inv)->{
            inv.setItem(48,icon);
        });
    }

    public static Pair<Integer,Integer> getRawSlot(int slot,boolean tf){
        return new Pair<>(1 + (int) Math.floor(slot / 28.0), (int) (1 + 9 * (Math.ceil((0.1 + slot % 28) / 7.0)) + slot % 7));
    }
    public static Pair<Integer,Integer> getRawSlot(int slot){
        return new Pair<>(1 + (int) Math.floor(slot / 28.0), available[slot % available.length]);
    }
}
