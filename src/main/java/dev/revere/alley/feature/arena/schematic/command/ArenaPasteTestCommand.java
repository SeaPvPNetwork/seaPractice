package dev.revere.alley.feature.arena.schematic.command;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.schematic.ArenaSchematicService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class ArenaPasteTestCommand extends BaseCommand {
    @CommandData(name = "arena.paste", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/arena paste &c<schematicName>"));
            return;
        }

        String schematicName = args[0];
        ArenaSchematicService schematicService = AlleyPlugin.getInstance().getService(ArenaSchematicService.class);
        File schematicFile = schematicService.getSchematicFile(schematicName);

        if (!schematicFile.exists()) {
            player.sendMessage(CC.translate("&cSchematic file not found: &c" + schematicName + ".schematic"));
            return;
        }

        schematicService.paste(player.getLocation(), schematicFile);
        player.sendMessage(CC.translate("&aPasted schematic &e" + schematicName + "&a at your location."));
    }
}
