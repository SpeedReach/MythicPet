package net.brian.mythicpet.motd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        if(!event.getPlayer().hasPermission("mythicpet.admin")){
            event.getPlayer().sendMessage("");
        }
    }

}
