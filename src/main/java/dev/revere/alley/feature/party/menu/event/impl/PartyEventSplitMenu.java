package dev.revere.alley.feature.party.menu.event.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.party.menu.event.impl.button.PartyEventSplitButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:38
 */
public class PartyEventSplitMenu extends Menu {
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();

    @Override
    public String getTitle(Player player) {
        return "&c&lSelect a kit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && !queue.isDuos() && queue.getKit().isEnabled()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new PartyEventSplitButton(queue.getKit()));
            }
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }
}