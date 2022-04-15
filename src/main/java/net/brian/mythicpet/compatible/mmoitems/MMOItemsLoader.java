package net.brian.mythicpet.compatible.mmoitems;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.brian.mythicpet.MythicPet;
import org.bukkit.Material;

public class MMOItemsLoader {
    public ItemStat mmo_pet_exp_multiplier;
    public void load(){
        if(MMOItems.plugin == null) return;
        MythicPet.mmoItems = true;
        mmo_pet_exp_multiplier = new DoubleStat("PET_EXP_MULTIPLIER", Material.BONE,
                "Pet Exp Multiplier",
                new String[]{"increase pet exp recieve","20 -> 120%"});
        MMOItems.plugin.getStats().register(mmo_pet_exp_multiplier);
        MMOItems.plugin.getStats().register(new DoubleStat("PET_HEALTH_RESTORE"
                ,Material.POTION
                ,"PET_HEALTH_RESTORE"
                ,new String[]{"restore pets health"}));
        MMOItems.plugin.getStats().register(new DoubleStat("PET_EXP_GAIN"
                ,Material.POTION
                ,"PET_EXP_GAIN"
                ,new String[]{"adds exp to the clicked pet"}));
    }

}
