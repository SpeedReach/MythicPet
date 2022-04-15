package net.brian.mythicpet.compatible.mythicmobs.legacy;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicDropLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder;
import io.lumine.xikage.mythicmobs.skills.placeholders.PlaceholderManager;
import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.compatible.mythicmobs.legacy.condition.HasTarget;
import net.brian.mythicpet.compatible.mythicmobs.legacy.condition.IsPetCondition;
import net.brian.mythicpet.compatible.mythicmobs.legacy.condition.PetModeCondition;
import net.brian.mythicpet.compatible.mythicmobs.legacy.drops.PetDrop;
import net.brian.mythicpet.compatible.mythicmobs.legacy.drops.PetExperienceDrop;
import net.brian.mythicpet.compatible.mythicmobs.legacy.target.PetTarget;
import net.brian.mythicpet.compatible.mythicmobs.legacy.target.PetTargets;
import net.brian.mythicpet.util.IridiumColorAPI;
import net.brian.mythicpet.util.PetUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class LegacyMythicLoader implements Listener {

    public LegacyMythicLoader(MythicPet plugin){
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        loadVariable();
    }

    @EventHandler
    public void onTargetLoad(MythicTargeterLoadEvent event){
        String name = event.getTargeterName();
        SkillTargeter targeter = null;
        if(name.equalsIgnoreCase("PetTargets")){
            targeter = new PetTargets(event.getConfig());
            event.register(targeter);
            return;
        }
        if(name.equalsIgnoreCase("PetTarget")){
            targeter = new PetTarget(event.getConfig());
            event.register(targeter);
            return;
        }
    }

    @EventHandler
    public void onDropLoad(MythicDropLoadEvent event){
        if(event.getDropName().equalsIgnoreCase("pet-exp")){
            Drop drop = new PetExperienceDrop(event.getContainer().getConfigLine(),event.getConfig());
            event.register(drop);
            return;
        }
        if(event.getDropName().equalsIgnoreCase("pet")){
            Drop drop = new PetDrop(event.getContainer().getConfigLine(),event.getConfig());
            event.register(drop);
        }
    }

    @EventHandler
    public void onConditionLoad(MythicConditionLoadEvent event){
        if(event.getConditionName().equalsIgnoreCase("ispet")){
            SkillCondition skillCondition = new IsPetCondition(event.getConfig().getLine());
            event.register(skillCondition);
            return;
        }
        if(event.getConditionName().equalsIgnoreCase("PetMode")){
            SkillCondition skillCondition = new PetModeCondition(event.getConditionName(),event.getConfig().getLine(),event.getConfig());
            event.register(skillCondition);
            return;
        }
        if(event.getConditionName().equalsIgnoreCase("HasTarget")){
            SkillCondition skillCondition = new HasTarget(event.getConfig().getLine());
            event.register(skillCondition);
            return;
        }
    }

    @EventHandler
    public void onReloadEvent(MythicReloadedEvent event){
        Bukkit.getScheduler().runTaskLater(MythicPet.inst(),()->{
            Bukkit.getOnlinePlayers().forEach(player -> {
                MythicPet.getPlayer(player.getUniqueId()).ifPresent(profile -> {
                    if(profile.hasActive()){
                        profile.getCurrentPet().ifPresent(pet -> {
                            Entity entity = pet.getPetEntity();
                            String name = entity.getCustomName();
                            if(name != null){
                                entity.setCustomName(IridiumColorAPI.process(entity.getCustomName().replace("<mythicpet.owner>",player.getName())));
                            }
                        });
                    }
                });
            });
        },100L);
    }

    public void loadVariable(){
        PlaceholderManager manager = MythicMobs.inst().getPlaceholderManager();
        manager.register("mythicpet.owner", Placeholder.meta(((meta, arg) -> PetUtils.getOwner(meta.getCaster().getEntity().getBukkitEntity())
                .flatMap(uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid)))
                .map(HumanEntity::getName)
                .orElse("mythicpet.owner"))));
    }
}
