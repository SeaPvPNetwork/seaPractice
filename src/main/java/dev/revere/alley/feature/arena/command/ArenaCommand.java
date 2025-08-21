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
                    " &c● &f/arena list &7| List all arenas",
                    " &c● &f/arena create &8(&7arenaName&8) &7| Create an arena",
                    " &c● &f/arena delete &8(&7arenaName&8) &7| Delete an arena",
                    " &c● &f/arena toggle &8(&7arenaName&8) &7| Enable or Disable an Arena",
                    " &c● &f/arena view &8(&7arenaName&8) &7| View arena information",
                    " &c● &f/arena teleport &8(&7arenaName&8) &7| Teleport to an arena",
                    " &c● &f/arena tool &7| Get the Arena Selection tool"
            },
            {
                    " &c● &f/arena setdisplayname &8(&7arenaName&8) &8(&7displayname&8) &7| Set display-name of an arena",
                    " &c● &f/arena setcenter &8(&7arenaName&8) &7| Set center position",
                    " &c● &f/arena setcuboid &8(&7arenaName&8) &7| Set min and max position",
                    " &c● &f/arena setspawn &8(&7arenaName&8) &8<&7pos1/pos2&8> &7| Set spawn positions",
                    " &c● &f/arena setportal &8(&7arenaName&8) &8<&71/2&8> &7| Set portal positions",
                    " &c● &f/arena setheightlimit &8(&7arenaName&8) &7| Set height limit for standalone arenas",
                    " &c● &f/arena setvoidlevel &8(&7arenaName&8) &7| Set void level for standalone arenas"
            },
            {
                    " &c● &f/arena kitlist &8(&7arenaName&8) &7| List all kits for an arena",
                    " &c● &f/arena addkit &8(&7arenaName&8) &8(&7kitName&8) &7| Add a kit to an arena",
                    " &c● &f/arena removekit &8(&7arenaName&8) &8(&7kitName&8) &7| Remove arena kit"
            },
            {
                    " &c● &f/arena saveall &7| Save all arenas"
            },
    };
}