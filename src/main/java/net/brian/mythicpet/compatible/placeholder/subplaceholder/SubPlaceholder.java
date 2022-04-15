package net.brian.mythicpet.compatible.placeholder.subplaceholder;

import net.brian.mythicpet.player.PlayerPetProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class SubPlaceholder {
    private final String name;

    public SubPlaceholder(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String onPlaceholderRequest(@NotNull PlayerPetProfile playerProfile, @NotNull String[] params);
}
