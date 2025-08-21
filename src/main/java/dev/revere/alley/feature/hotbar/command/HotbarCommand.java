package dev.revere.alley.feature.hotbar.command;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.common.text.CC;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
public class HotbarCommand extends BaseCommand {
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&c&l[NOTE] &fThese commands are not implemented yet.",
                "",
                "&c&lHotbar Commands Help",
                " &c● &f/hotbar create &8(&7name&8) &8(&7type&8) &7| Create a new hotbar item.",
                " &c● &f/hotbar delete &8(&7name&8) &7| Delete a hotbar item.",
                " &c● &f/hotbar list &7| List all hotbar items.",
                ""
        ).forEach(line -> command.getSender().sendMessage(CC.translate(line)));

        //TODO: implement the actual commands
    }
}
