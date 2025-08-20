package dev.revere.alley.feature.level.command.impl.manage;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 26/05/2025
 */
public class LevelAdminViewCommand extends BaseCommand {
    @CommandData(name = "leveladmin.view", isAdminOnly = true, description = "View level information", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/leveladmin view &c<levelName>"));
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level == null) {
            sender.sendMessage(CC.translate("&cA level with that name does not exist!"));
            return;
        }

        Arrays.asList(
                "",
                "&c&lLevel Information:",
                " &f● &cName: &e" + level.getName(),
                " &f● &cDisplay Name: &e" + level.getDisplayName(),
                " &f● &cMinimum Elo: &e" + level.getMinElo(),
                " &f● &cMaximum Elo: &e" + level.getMaxElo(),
                " &f● &cMaterial: &e" + level.getMaterial().name(),
                " &f● &cDurability: &e" + level.getDurability(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));
    }
}