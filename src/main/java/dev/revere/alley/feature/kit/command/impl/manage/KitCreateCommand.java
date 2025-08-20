package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.ActionBarReflectionServiceImpl;
import dev.revere.alley.common.InventoryUtil;
import dev.revere.alley.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 13:06
 */
public class KitCreateCommand extends BaseCommand {
    @CommandData(name = "kit.create", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/kit create &c<kitName>"));
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        if (kitService.getKit(kitName) != null) {
            player.sendMessage(CC.translate(KitLocale.KIT_ALREADY_EXISTS.getMessage()));
            return;
        }

        ItemStack[] inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        ItemStack[] armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());

        Material icon = Material.DIAMOND_SWORD;
        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
            icon = player.getItemInHand().getType();
        }

        kitService.createKit(kitName, inventory, armor, icon);
        this.plugin.getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).sendMessage(player, KitLocale.KIT_CREATED.getMessage().replace("{kit-name}", kitName), 5);

        player.sendMessage(CC.translate(KitLocale.KIT_CREATED.getMessage().replace("{kit-name}", kitName)));
        player.sendMessage("");
        player.sendMessage(CC.translate("&7Do not forget to reload the queues &c&lAFTER ENABLING &7 the kit &8(/kit toggle) &7by using &c&l/queue reload&7."));
        player.sendMessage("");
    }
}