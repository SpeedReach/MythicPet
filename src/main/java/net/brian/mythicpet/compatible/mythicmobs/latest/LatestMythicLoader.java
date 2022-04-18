package net.brian.mythicpet.compatible.mythicmobs.latest;

import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.compatible.mythicmobs.latest.conditions.HasTarget;
import net.brian.mythicpet.compatible.mythicmobs.latest.conditions.IsPetCondition;
import net.brian.mythicpet.compatible.mythicmobs.latest.conditions.PetModeCondition;
import net.brian.mythicpet.compatible.mythicmobs.latest.drops.PetDrop;
import net.brian.mythicpet.compatible.mythicmobs.latest.drops.PetExpDrop;
import net.brian.mythicpet.compatible.mythicmobs.latest.target.PetTarget;
import net.brian.mythicpet.utils.PetUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LatestMythicLoader implements Listener {


    public LatestMythicLoader(MythicPets plugin){
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        loadVariable();
    }

    private void loadVariable(){
        MythicProvider.get().getPlaceholderManager()
                .register("mythicpet.owner", Placeholder.meta((meta, arg) -> {
                    return PetUtils.getOwner(meta.getCaster().getEntity().getBukkitEntity())
                            .map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
                            .orElse("<mythicpet.owner>");
                }));
    }

    @EventHandler
    public void onTargetLoad(MythicTargeterLoadEvent event){
        String name = event.getTargeterName();

        if(name.equalsIgnoreCase("PetTarget")){
            event.register(new PetTarget(event.getConfig()));
        }
    }

    @EventHandler
    public void onConditionLoad(MythicConditionLoadEvent event){
        String name = event.getConditionName();

        if(name.equalsIgnoreCase("hasTarget")){
            event.register(new HasTarget(event.getConfig().getLine()));
        }
        else if(name.equalsIgnoreCase("ispet")){
            event.register(new IsPetCondition(event.getConfig().getLine()));
        }
        else if(name.equalsIgnoreCase("PetMode")){
            event.register(new PetModeCondition(event.getConfig()));
        }
    }

    @EventHandler
    public void onDropLoad(MythicDropLoadEvent event){
        String name = event.getDropName();
        if(name.equalsIgnoreCase("pet")){
            event.register(new PetDrop(event.getConfig()));
        }
        else if(name.equalsIgnoreCase("pet-exp")){
            event.register(new PetExpDrop());
        }
    }
}
