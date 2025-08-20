package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 24/09/2024 - 18:29
 */
public class ArenaViewCommand extends BaseCommand {
    @CommandData(name = "arena.view", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/arena view &c<arenaName>"));
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);

        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            sender.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", args[0]));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&c&lArena " + arena.getName() + " &f(" + (arena.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        sender.sendMessage(CC.translate(" &f● &cDisplay Name: &f" + arena.getDisplayName()));
        sender.sendMessage(CC.translate(" &f● &cName: &f" + arena.getName()));
        sender.sendMessage(CC.translate(" &f● &cType: &f" + arena.getType()));
        sender.sendMessage(CC.translate("    &c&lData: &f"));
        sender.sendMessage(CC.translate("    &f• &cCenter: &f" + (arena.getCenter() != null ? arena.getCenter().getX() + ", " + arena.getCenter().getY() + ", " + arena.getCenter().getZ() + ", &7" + arena.getCenter().getPitch() + ", " + arena.getCenter().getYaw() + " &7[" + arena.getCenter().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("    &f• &cPos1: &f" + (arena.getPos1() != null ? arena.getPos1().getX() + ", " + arena.getPos1().getY() + ", " + arena.getPos1().getZ() + ", &7" + arena.getPos1().getPitch() + ", " + arena.getPos1().getYaw() + " &7[" + arena.getPos1().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("    &f• &cPos2: &f" + (arena.getPos2() != null ? arena.getPos2().getX() + ", " + arena.getPos2().getY() + ", " + arena.getPos2().getZ() + ", &7" + arena.getPos2().getPitch() + ", " + arena.getPos2().getYaw() + " &7[" + arena.getPos2().getWorld().getName() + "]" : "&cNull")));

        if (arena instanceof StandAloneArena) {
            StandAloneArena standAloneArena = (StandAloneArena) arena;
            sender.sendMessage(CC.translate("    &f• &cPortal 1: &f" + (standAloneArena.getTeam1Portal() != null ? standAloneArena.getTeam1Portal().getX() + ", " + standAloneArena.getTeam1Portal().getY() + ", " + standAloneArena.getTeam1Portal().getZ() + " &7[" + standAloneArena.getTeam1Portal().getWorld().getName() + "]" : "&cNull")));
            sender.sendMessage(CC.translate("    &f• &cPortal 2: &f" + (standAloneArena.getTeam2Portal() != null ? standAloneArena.getTeam2Portal().getX() + ", " + standAloneArena.getTeam2Portal().getY() + ", " + standAloneArena.getTeam2Portal().getZ() + " &7[" + standAloneArena.getTeam2Portal().getWorld().getName() + "]" : "&cNull")));
            sender.sendMessage(CC.translate("    &f• &cHeight Limit: &f" + standAloneArena.getHeightLimit()));
        }

        sender.sendMessage(CC.translate("    &f• &cCuboid:"));
        sender.sendMessage(CC.translate("     &f- &cMinimum: &f" + (arena.getMinimum() != null ? arena.getMinimum().getX() + ", " + arena.getMinimum().getY() + ", " + arena.getMinimum().getZ() + " &7[" + arena.getMinimum().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("     &f- &cMaximum: &f" + (arena.getMaximum() != null ? arena.getMaximum().getX() + ", " + arena.getMaximum().getY() + ", " + arena.getMaximum().getZ() + " &7[" + arena.getMaximum().getWorld().getName() + "]" : "&cNull")));
        sender.sendMessage(CC.translate("   &f• &cKits: &f(" + arena.getKits().size() + ")"));
        if (arena.getKits().isEmpty()) {
            sender.sendMessage(CC.translate("    &f- &cNo Kits added yet."));
        } else {
            arena.getKits().forEach(kit -> sender.sendMessage(CC.translate("    &f- &c" + kit)));
        }
    }
}
