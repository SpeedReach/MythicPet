package net.brian.mythicpet.volatilecode;

import org.bukkit.Bukkit;

public abstract class VolatileCodeHandler {
    public static boolean esLegacy() {
        if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") ||
                Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")
                || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")) {
            return false;
        }else {
            return true;
        }
    }

    public static boolean esNew() {
        if(Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")
                || Bukkit.getVersion().contains("1.18")) {
            return true;
        }else {
            return true;
        }
    }
}
