package net.brian.mythicpet.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.replacer.Replacer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NonePlayerReplace implements Replacer {
    @NotNull
    private final Pattern pattern;

    public NonePlayerReplace(@NotNull Closure closure) {
        this.pattern = Pattern.compile(String.format("\\%s((?<identifier>[a-zA-Z0-9]+)_)(?<parameters>[^%s%s]+)\\%s", closure.head, closure.head, closure.tail, closure.tail));
    }

    @NotNull
    public String apply(@NotNull String text, @NotNull Function<String, PlaceholderExpansion> lookup) {
        Matcher matcher = this.pattern.matcher(text);
        if (!matcher.find()) {
            return text;
        } else {
            StringBuffer builder = new StringBuffer();

            do {
                String identifier = matcher.group("identifier");
                String parameters = matcher.group("parameters");
                PlaceholderExpansion expansion = lookup.apply(identifier);
                if (expansion != null) {
                    matcher.appendReplacement(builder,"0");
                }
            } while(matcher.find());

            return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(builder).toString());
        }
    }

    @Override
    public @NotNull String apply(@NotNull String s, @Nullable OfflinePlayer offlinePlayer, @NotNull Function<String,PlaceholderExpansion> function) {
        return null;
    }
}
