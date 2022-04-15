package net.brian.mythicpet.compatible.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;

public class WorldGuardSupport {
    public StateFlag pet_damaged_by_player;
    public StateFlag pet_hurt;
    public StateFlag pet_damage_pet;
    public StateFlag pet_damage_player;
    public WorldGuardSupport(){
    }
    public void loadFlag(){
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("pet-damaged-by-player", true);
            StateFlag flag2 = new StateFlag("pet-damaged",true);
            StateFlag flag3 = new StateFlag("pet-damage-pet",false);
            StateFlag flag4 = new StateFlag("pet-damage-player",false);
            registry.register(flag);
            registry.register(flag3);
            registry.register(flag4);
            registry.register(flag2);
            pet_hurt = flag2;
            pet_damaged_by_player = flag;
            pet_damage_pet = flag3;
            pet_damage_player = flag4;
            // only set our field if there was no error
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to check type
            Flag<?> existing = registry.get("pet-damaged-by-player");
            Flag<?> existing2 = registry.get("pet-damaged");
            Flag<?> existing3 = registry.get("pet-damage-pet");
            Flag<?> existing4 = registry.get("pet-damage-player");
            if (existing instanceof StateFlag) {
                pet_damaged_by_player = (StateFlag) existing;
            } else {
                // types don't match - this is bad news! some other plugin conflicts with you
                // hopefully this never actually happens
            }
        }
    }
    public boolean petCanHitPet(Location location){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(location));
        return set.testState(null,pet_damage_pet);
    }
    public boolean petCanHitPlayer(Location location){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ApplicableRegionSet set = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(location));
        return set.testState(null,pet_damage_player);
    }
}
