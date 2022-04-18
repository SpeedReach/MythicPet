package net.brian.mythicpet.utils;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.mount.controller.MountController;
import com.ticxo.modelengine.api.model.mount.handler.IMountHandler;

import net.brian.mythicpet.MythicPets;
import net.brian.mythicpet.compatible.mythicmobs.MythicUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MountPetUtil {


    public static void mountPet(Player passenger,Entity petEntity,String type){
        if(!petEntity.getPassengers().contains(passenger)){
            if(MythicPets.hasModelEngine()){
                ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().getModeledEntity(petEntity.getUniqueId());
                if(modeledEntity != null){
                    IMountHandler mountHandler = modeledEntity.getMountHandler();
                    MountController mountController = null;
                    mountController = ModelEngineAPI.api.getControllerManager().createController(type);
                    if(mountController == null){
                        ModelEngineAPI.api.getControllerManager().createController("walking");
                    }
                    mountHandler.setDriver(passenger,mountController);
                    mountHandler.setCanDamageMount(passenger,true);
                }
                else{
                    petEntity.addPassenger(passenger);
                    startControl(passenger, petEntity);
                }
            }
            else{
                petEntity.addPassenger(passenger);
                startControl(passenger, petEntity);
            }
        }
    }

    private static void startControl(Player passenger, Entity petEntity){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!petEntity.getPassengers().contains(passenger)) cancel();
                else{
                    MythicUtil.navigateTo(petEntity,passenger.getEyeLocation().add(passenger.getLocation().getDirection().multiply(10)));
                }
            }
        }.runTaskTimer(MythicPets.inst(),10,0);
    }


}
