package dev.revere.alley.feature.item.command;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
public class CustomItemsCommand extends BaseCommand {
    @CommandData(
            name = "customitems",
            aliases = {"alleyitems", "specialitems"},
            usage = "/customitems",
            description = "List of commands for special items",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&c&lCustom Items Commands Help:",
                " &f● &c/customitems goldenhead &8(&7amount&8) &7| Gives you a golden head",
                ""
        ).forEach(line -> command.getPlayer().sendMessage(CC.translate(line)));
    }
}