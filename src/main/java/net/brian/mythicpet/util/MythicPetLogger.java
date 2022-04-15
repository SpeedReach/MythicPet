package net.brian.mythicpet.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MythicPetLogger {
    private static final Logger logger =Logger.getLogger("MythicPets");

    public static void log(String msg){
        logger.log(Level.INFO,msg);
    }
}
