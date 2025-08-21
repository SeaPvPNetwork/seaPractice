package dev.revere.alley.feature.kit.command.helper;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 14/07/2025
 */
public class KitHelperCommand extends BaseCommand {
    @CommandData(
            name = "kithelper",
            description = "Provides assistance for essentials.",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&c&lKit Helper Commands Help:",
                " &c● &f/enchant &8(&7enchantment&8) &8(&7level&8) &7| &fEnchant item in hand.",
                " &c● &f/glow &8(&7true|false&8) &7| &fSet item glow.",
                " &c● &f/potionduration &8(&7duration&8) &7| &fSet duration of a potion.",
                " &c● &f/removeenchants &7| &fRemoves enchants from item.",
                " &c● &f/rename &8(&7name&8) &7| &fRename item in hand.",
                " &c● &f/unbreakable &8(&7true|false&8) &7| &fSet item unbreakable.",
                ""
        ).forEach(message -> command.getSender().sendMessage(CC.translate(message)));
    }
}