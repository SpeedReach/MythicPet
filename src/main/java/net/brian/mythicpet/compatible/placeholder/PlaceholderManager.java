package net.brian.mythicpet.compatible.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.compatible.placeholder.subplaceholder.PetHealth;
import net.brian.mythicpet.compatible.placeholder.subplaceholder.PetName;
import net.brian.mythicpet.compatible.placeholder.subplaceholder.SubPlaceholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderManager extends PlaceholderExpansion {

    List<SubPlaceholder> subPlaceholders = new ArrayList<>();

    public PlaceholderManager(){
        subPlaceholders.add(new PetHealth());
        subPlaceholders.add(new PetName());
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mythicpet";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SleepAllDay";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params){
        String[] args = params.split("_",2);
        if(args.length == 0 ) return null;
        SubPlaceholder subPlaceholder = getSubPlaceholder(args[0]);
        if(subPlaceholder != null){
            return MythicPets.getPlayer(player.getUniqueId()).map(profile->{
                if(args.length > 1){
                    return subPlaceholder.onPlaceholderRequest(profile,args[1].split("_"));
                }
                return subPlaceholder.onPlaceholderRequest(profile,new String[]{});
            }).orElse("Loading");
        }
        return "Not available Holder";
    }

    private SubPlaceholder getSubPlaceholder(String name){
        for (SubPlaceholder subPlaceholder : subPlaceholders) {
            if(subPlaceholder.getName().equalsIgnoreCase(name)) return subPlaceholder;
        }
        return null;
    }
}
