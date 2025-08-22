package dev.revere.alley.feature.command.impl.other;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/06/2025
 */
public class CreditsCommand extends BaseCommand {
    @CommandData(name = "credits", aliases = {"emmy", "remi", "revere"} )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate("&f"));
        player.sendMessage(CC.translate("&7&m                                                   "));
        player.sendMessage(CC.translate("     &c&lPractice Core &7- &fImprove, win and be the best."));
        player.sendMessage(CC.translate("&f"));
        player.sendMessage(CC.translate("      &7│ &fThis core is a fork from &6Alley"));
        player.sendMessage(CC.translate("      &7│ &fModified for &bSeaPvP&f by &bdiamondclass"));
        player.sendMessage(CC.translate("&f"));
        player.sendMessage(CC.translate("      &7│ &f• &7Authors: &cEmmy&7, &cRemi&7 - &cRevere Inc."));
        player.sendMessage(CC.translate("&f"));
        player.sendMessage(CC.translate("      &7│ &cAlley &fis available on GitHub"));
        player.sendMessage(CC.translate("      &7│ &fgithub.com/RevereInc/alley-practice"));
        player.sendMessage(CC.translate("&7&m                                                   "));
        player.sendMessage(CC.translate("&f"));
    }
}
