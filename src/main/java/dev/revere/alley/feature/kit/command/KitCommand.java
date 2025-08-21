package dev.revere.alley.feature.kit.command;

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
 * @date 28/04/2024 - 21:58
 */
public class KitCommand extends BaseCommand {

    @SuppressWarnings("unused")
    @CompleterData(name = "kit")
    public List<String> kitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            completion.addAll(Arrays.asList(
                    "list", "create", "delete", "toggle", "view", "settings", "viewsettings",
                    "setsetting", "setcategory", "setdescription", "setdisclaimer", "setdisplayname",
                    "seteditable", "seticon", "setinv", "getinv", "addpotion", "clearpotions", "removepotion",
                    "saveall", "save", "setraidingrolekit",
                    "removeraidingrolekit", "setmenutitle", "setprofile", "resetlayouts"
            ));
        }

        return completion;
    }

    @CommandData(
            name = "kit",
            aliases = "kit.help",
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

        if (page > this.pages.length || page < 1) {
            sender.sendMessage(CC.translate("&cNo more pages available."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&c&lKit Commands &8(&7Page &f" + page + "&7/&f" + this.pages.length + "&8)"));
        for (String string : this.pages[page - 1]) {
            sender.sendMessage(CC.translate(string));
        }

        sender.sendMessage("");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ClickableUtil.sendPageNavigation(player, page, this.pages.length, "/kit", false, true);
        }
    }

    private final String[][] pages = {
            {
                    " &c● &f/kit list &7| View all kits",
                    " &c● &f/kit create &8(&7kitName&8) &7| Create a kit",
                    " &c● &f/kit delete &8(&7kitName&8) &7| Delete a kit",
                    " &c● &f/kit toggle &8(&7kitName&8) &7| Toggle a kit",
                    " &c● &f/kit view &8(&7kitName&8) &7| View a kit",
                    "",
                    "&fUse &c/kithelper &ffor other useful commands."
            },
            {
                    " &c● &f/kit settings &7| View all existing kit settings",
                    " &c● &f/kit viewsettings &8(&7kitName&8) &7| View settings of a kit",
                    " &c● &f/kit setsetting &8(&7kitName&8) &8(&7setting&8) &8(&7enabled&8) &7| Set kit setting"
            },
            {
                    " &c● &f/kit setcategory &8(&7kitName&8) &8(&7category&8) &7| Set category of a kit",
                    " &c● &f/kit setdescription &8(&7kitName&8) &8(&7description&8) &7| Set description of a kit",
                    " &c● &f/kit setdisclaimer &8(&7kitName&8) &8(&7disclaimer&8) &7| Set disclaimer",
                    " &c● &f/kit setdisplayname &8(&7kitName&8) &8(&7displayname&8) &7| Set display-name of a kit",
                    " &c● &f/kit setmenutitle &8(&7kitName&8) &8(&7title&8) &7| Set menu title of a kit",
                    " &c● &f/kit seteditable &8(&7kitName&8) &8(&7true/false&8) &7| Set if a kit is editable",
                    " &c● &f/kit setprofile &8(&7kitName&8) &8(&7profileName&8) &7| Set kb profile of a kit",
                    " &c● &f/kit seticon &8(&7kitName&8) &7| Set icon of a kit"
            },
            {
                    " &c● &f/kit setinv &8(&7kitName&8) &7| Set inventory of a kit",
                    " &c● &f/kit getinv &8(&7kitName&8) &7| Get inventory of a kit"
            },
            {
                    " &c● &f/kit addpotion &8(&7kitName&8) &7| Set potion effects of a kit",
                    " &c● &f/kit removepotion &8(&7kitName&8) &7| Remove potion effects of a kit",
                    " &c● &f/kit clearpotions &8(&7kitName&8) &7| Clear potion effects of a kit"
            },
            {
                    " &c● &f/kit setraidingrolekit &8(&7kitName&8) &8(&7role&8) &8(&7roleKitName&8) &7| Set raiding role kit",
                    " &c● &f/kit removeraidingrolekit &8(&7kitName&8) &8(&7role&8) &8(&7roleKitName&8) &7| Remove raiding role kit"
            },
            {
                    " &c● &f/kit resetlayouts &8(&7kitName&8) &7| Reset all profile layouts",
                    " &c● &f/kit saveall &7| Save all kits",
                    " &c● &f/kit save &8(&7kitName&8) &7| Save a kit"
            }
    };
}