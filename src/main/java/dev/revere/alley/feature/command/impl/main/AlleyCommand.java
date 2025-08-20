package dev.revere.alley.feature.command.impl.main;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
public class AlleyCommand extends BaseCommand {
    @SuppressWarnings("unused")
    @CompleterData(name = "practicecore")
    public List<String> alleyCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            completion.addAll(Arrays.asList(
                    "reload", "debug", "server"
            ));
        }

        return completion;
    }

    @CommandData(name = "practicecore", aliases = {"apractice", "aprac", "practice", "prac", "emmy", "remi", "revere"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Arrays.asList(
                "",
                "     &c&lPractice Core Practice",
                "      &f│ Authors: &c" + this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                "      &f│ Version: &c" + this.plugin.getDescription().getVersion(),
                "",
                "     &c&lDescription:",
                "      &f│ " + this.plugin.getDescription().getDescription(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (sender.hasPermission(this.plugin.getService(PluginConstant.class).getAdminPermissionPrefix())) {
            Arrays.asList(
                    "     &c&lAdmin Help",
                    "      &f│ /practicecore reload &7- &cReloads the bootstrap.",
                    "      &f│ /practicecore debug &7- &cDatabase Debugging.",
                    "      &f│ /practicecore server &7- &cCore Hook Info.",
                    ""
            ).forEach(line -> sender.sendMessage(CC.translate(line)));
        }
    }
}