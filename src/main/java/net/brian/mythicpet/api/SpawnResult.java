package net.brian.mythicpet.api;

public enum SpawnResult {

    /**If the pet is already spawn , it'll fail and return this*/
    ALREADY_SPAWNED,
    /**Fails if the pet is dead and is in cooldown*/
    COOLDOWN,
    /**Fails if the corresponding MythicMob doesn't exist*/
    MOB_NOT_EXISTS,
    //Fails if the pet with the id does not exists
    PET_NOT_EXIST,
    SUCCESS

}
