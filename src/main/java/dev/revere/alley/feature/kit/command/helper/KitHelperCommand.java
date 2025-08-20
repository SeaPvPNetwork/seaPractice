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
                " &f● &c/enchant &8(&7enchantment&8) &8(&7level&8) &7| &fEnchant item in hand.",
                " &f● &c/glow &8(&7true|false&8) &7| &fSet item glow.",
                " &f● &c/potionduration &8(&7duration&8) &7| &fSet duration of a potion.",
                " &f● &c/removeenchants &7| &fRemoves enchants from item.",
                " &f● &c/rename &8(&7name&8) &7| &fRename item in hand.",
                " &f● &c/unbreakable &8(&7true|false&8) &7| &fSet item unbreakable.",
                ""
        ).forEach(message -> command.getSender().sendMessage(CC.translate(message)));
    }
}