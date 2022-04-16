package net.brian.mythicpet.command;

import net.brian.mythicpet.MythicPet;
import net.brian.mythicpet.command.commands.*;
import net.brian.mythicpet.config.Message;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    private final ArrayList<SubCommand> commands = new ArrayList<>();
    private final List<String> available = new ArrayList<>();

    //Sub Commands
    public static String main = "mythicpet";

    public CommandManager(MythicPet plugin){
        plugin.getCommand(main).setExecutor(this);
        setUp();
    }

    public void setUp(){
        register(new HelpCommand());
        register(new GivePet());
        register(new ModeCommand());
        register(new Reload());
        register(new StorageCommand());
        register(new SetLevel());
        register(new Exp());
        register(new ListCommand());
        register(new BindSignalItem());
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase(main)){
            if(args.length==1) {
                return available;
            }
            SubCommand subCommand = getSubCommand(args[0]);
            if(subCommand != null){
                return subCommand.onTabComplete(sender,args);
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length==0) {
            sender.sendMessage(Message.Help);
            return true;
        }
        if(command.getName().equalsIgnoreCase(main)){
            SubCommand subCommand = getSubCommand(args[0]);
            if(subCommand != null){
                subCommand.onCommand(sender,args);
            }
            else{
                sender.sendMessage(Message.Help);
            }
        }
        return true;
    }

    private SubCommand getSubCommand(String name){
        for (SubCommand subCommand : commands) {
            if (subCommand.name().equalsIgnoreCase(name)) {
                return subCommand;
            }
            String[] aliases = subCommand.aliases();
            for (String alias : aliases) {
                if (name.equalsIgnoreCase(alias)) {
                    return subCommand;
                }
            }
        }
        return null;
    }

    public void register(SubCommand subCommand){
        commands.add(subCommand);
        available.add(subCommand.name());
    }
}
