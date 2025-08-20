package dev.revere.alley.feature.kit.command.impl.data.potion;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class KitClearPotionsCommand extends BaseCommand {

    @CommandData(name = "kit.clearpotions", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/kit removepotion &c<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);

        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        if (kit.getPotionEffects().isEmpty()) {
            player.sendMessage(CC.translate("&cThis kit has no potion effects to remove."));
            return;
        }

        kit.getPotionEffects().clear();
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_POTION_EFFECTS_CLEARED.getMessage().replace("{kit-name}", kit.getName())));
    }
}
