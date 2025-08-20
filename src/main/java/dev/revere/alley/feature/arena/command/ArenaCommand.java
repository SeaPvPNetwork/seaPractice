package dev.revere.alley.feature.arena.command;

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
 * @project Alley
 * @date 02/05/2024 - 19:02
 */
public class ArenaCommand extends BaseCommand {

    @SuppressWarnings("unused")
    @CompleterData(name = "arena")
    public List<String> arenaCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            completion.addAll(Arrays.asList(
                    "create", "delete", "list", "kitlist", "setcuboid", "setcenter",
                    "setspawn", "removekit", "addkit", "teleport", "toggle", "tool",
                    "saveall", "setsafezone", "setdisplayname", "setheightlimit",
                    "setvoidlevel", "setportal", "view", "test", "paste-test"
            ));
        }

        return completion;
    }

    @CommandData(
            name = "arena",
            aliases = "arena.help",
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
            } catch (NumberFormatException e) {
                sender.sendMessage(CC.translate("&cInvalid page number."));
            }
        }

        if (page > pages.length || page < 1) {
            sender.sendMessage(CC.translate("&cNo more pages available."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&c&lArena Commands &8(&7Page &f" + page + "&7/&f" + pages.length + "&8)"));
        for (String string : pages[page - 1]) {
            sender.sendMessage(CC.translate(string));
        }
        sender.sendMessage("");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClickableUtil.sendPageNavigation(player, page, pages.length, "/arena", false, true);
        }
    }

    private final String[][] pages = {
            {
                    " &f● &c/arena list &7| List all arenas",
                    " &f● &c/arena create &8(&7arenaName&8) &7| Create an arena",
                    " &f● &c/arena delete &8(&7arenaName&8) &7| Delete an arena",
                    " &f● &c/arena toggle &8(&7arenaName&8) &7| Enable or Disable an Arena",
                    " &f● &c/arena view &8(&7arenaName&8) &7| View arena information",
                    " &f● &c/arena teleport &8(&7arenaName&8) &7| Teleport to an arena",
                    " &f● &c/arena tool &7| Get the Arena Selection tool"
            },
            {
                    " &f● &c/arena setdisplayname &8(&7arenaName&8) &8(&7displayname&8) &7| Set display-name of an arena",
                    " &f● &c/arena setcenter &8(&7arenaName&8) &7| Set center position",
                    " &f● &c/arena setcuboid &8(&7arenaName&8) &7| Set min and max position",
                    " &f● &c/arena setspawn &8(&7arenaName&8) &8<&7pos1/pos2&8> &7| Set spawn positions",
                    " &f● &c/arena setportal &8(&7arenaName&8) &8<&71/2&8> &7| Set portal positions",
                    " &f● &c/arena setheightlimit &8(&7arenaName&8) &7| Set height limit for standalone arenas",
                    " &f● &c/arena setvoidlevel &8(&7arenaName&8) &7| Set void level for standalone arenas"
            },
            {
                    " &f● &c/arena kitlist &8(&7arenaName&8) &7| List all kits for an arena",
                    " &f● &c/arena addkit &8(&7arenaName&8) &8(&7kitName&8) &7| Add a kit to an arena",
                    " &f● &c/arena removekit &8(&7arenaName&8) &8(&7kitName&8) &7| Remove arena kit"
            },
            {
                    " &f● &c/arena saveall &7| Save all arenas"
            },
    };
}