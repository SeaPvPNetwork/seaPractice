package dev.revere.alley.feature.ffa.command;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.ClickableUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 25/07/2025
 */
public class FFACommand extends BaseCommand {

    @SuppressWarnings("unused")
    @CompleterData(name = "ffa")
    public List<String> ffaCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (!command.getPlayer().hasPermission(this.getAdminPermission())) {
            return completion;
        }

        if (command.getArgs().length == 1) {
            completion.addAll(Arrays.asList(
                    "maxplayers", "setsafezone", "setarena", "setslot", "setspawn",
                    "list", "setup", "toggle", "add", "kick", "listplayers"
            ));
        }

        return completion;
    }

    @CommandData(
            name = "ffa",
            aliases = "ffa.help",
            isAdminOnly = true,
            inGameOnly = false
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                sender.sendMessage(CC.translate("&cInvalid page number."));
            }
        }

        if (page > this.pages.length || page < 1) {
            sender.sendMessage(CC.translate("&cNo more pages available."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&c&lFFA Commands &8(&7Page &f" + page + "&7/&f" + this.pages.length + "&8)"));
        for (String string : this.pages[page - 1]) {
            sender.sendMessage(CC.translate(string));
        }
        sender.sendMessage("");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClickableUtil.sendPageNavigation(player, page, this.pages.length, "/ffa", false, true);
        }
    }

    private final String[][] pages = {
            {
                    " &f● &c/ffa setup &8(&7ffaName&8) &8(&7arenaName&8) &8(&7maxPlayers&8) &8(&7menuSlot&8) &7| Set up a new FFA match",
                    " &f● &c/ffa toggle &8(&7ffaName&8) &7| Enable or disable an FFA arena",
                    " &f● &c/ffa list &7| List current FFA matches",
                    " &f● &c/ffa listplayers &8(&7ffaName&8) &7| List all players playing ffa",
            },
            {
                    " &f● &c/ffa maxplayers &8(&7ffaName&8) &8(&7amount&8) &7| Set the max player count.",
                    " &f● &c/ffa safezone &8(&7kitName&8) &8(&7pos1/pos2&8) &7| Set the spawn safezone bounds",
                    " &f● &c/ffa setspawn &8(&7ffaName&8) &7| Set the spawn location for an FFA arena",
                    " &f● &c/ffa setarena &8(&7ffaName&8) &7| Set arena of a ffa match",
                    " &f● &c/ffa setslot &8(&7ffaName&8) &8(&7slotNumber&8) &7| Set menu slot"
            },
            {
                    " &f● &c/ffa add &8(&7playerName&8) &8(&7ffaName&8) &7| Add a player",
                    " &f● &c/ffa kick &8(&7playerName&8) &7| Kick a player"
            }
    };
}