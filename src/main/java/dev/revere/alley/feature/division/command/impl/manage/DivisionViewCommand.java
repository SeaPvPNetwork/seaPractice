package dev.revere.alley.feature.division.command.impl.manage;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 26/01/2025
 */
public class DivisionViewCommand extends BaseCommand {
    @CommandData(name = "division.view", isAdminOnly = true, usage = "division view <name>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/division view &c<name>"));
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(CC.translate("&cA division with that name does not exist."));
            return;
        }

        Arrays.asList(
                "",
                "&c&lDivision &f(" + division.getDisplayName() + ")",
                " &c● &fName: &c" + division.getDisplayName(),
                " &c● &fTiers: &c" + division.getTiers().size(),
                " &c● &fDescription: &c" + division.getDescription(),
                " &c● &fRequired Wins: &c" + division.getTiers().get(0).getRequiredWins(),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}