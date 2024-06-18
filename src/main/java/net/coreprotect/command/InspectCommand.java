package net.coreprotect.command;

import org.bukkit.command.CommandSender;

import net.coreprotect.config.ConfigHandler;
import net.coreprotect.language.Phrase;
import net.coreprotect.language.Selector;
import net.coreprotect.utility.Chat;
import net.coreprotect.utility.Color;
import org.bukkit.entity.Player;
import phonon.nodes.Nodes;
import phonon.nodes.objects.Resident;
import phonon.nodes.objects.Town;

public class InspectCommand {
    protected static void runCommand(CommandSender player, boolean permission, String[] args) {
        if (permission || isLeader((Player)player)) {
            int command = -1;
            ConfigHandler.inspecting.putIfAbsent(player.getName(), false);

            if (args.length > 1) {
                String action = args[1];
                if (action.equalsIgnoreCase("on")) {
                    command = 1;
                }
                else if (action.equalsIgnoreCase("off")) {
                    command = 0;
                }
            }

            if (!ConfigHandler.inspecting.get(player.getName())) {
                if (command == 0) {
                    Chat.sendMessage(player, Color.DARK_AQUA + "CoreProtect " + Color.WHITE + "- " + Phrase.build(Phrase.INSPECTOR_ERROR, Selector.SECOND)); // already disabled
                }
                else {
                    Chat.sendMessage(player, Color.DARK_AQUA + "CoreProtect " + Color.WHITE + "- " + Phrase.build(Phrase.INSPECTOR_TOGGLED, Selector.FIRST)); // now enabled
                    ConfigHandler.inspecting.put(player.getName(), true);
                }
            }
            else {
                if (command == 1) {
                    Chat.sendMessage(player, Color.DARK_AQUA + "CoreProtect " + Color.WHITE + "- " + Phrase.build(Phrase.INSPECTOR_ERROR, Selector.FIRST)); // already enabled
                }
                else {
                    Chat.sendMessage(player, Color.DARK_AQUA + "CoreProtect " + Color.WHITE + "- " + Phrase.build(Phrase.INSPECTOR_TOGGLED, Selector.SECOND)); // now disabled
                    ConfigHandler.inspecting.put(player.getName(), false);
                }
            }

        }
        else {
            Chat.sendMessage(player, Color.DARK_AQUA + "CoreProtect " + Color.WHITE + "- " + Phrase.build(Phrase.NO_PERMISSION));
        }
    }

    /**
     * @param player the player who might be leader
     * @return true if the player is a of a town leader
     * The way im doing this is basically the exact way the Nodes plugin does it internally except on java
     */
    public static boolean isLeader(Player player){
        Resident resident = Nodes.INSTANCE.getResident(player);
        if (resident == null){
            return false; //Gets if the player is a resident
        }

        Town playerTown = resident.getTown();
        if (playerTown == null){
            return false; //Gets if the player has a town
        }

        assert playerTown.getLeader() != null;
        return playerTown.getLeader().equals(resident); //Checks if the player is a leader of a town
    }
}
