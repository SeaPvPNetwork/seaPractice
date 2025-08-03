package dev.revere.alley.visual.tablist.task;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.visual.tablist.TablistAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:23
 */
public class TablistUpdateTask extends BukkitRunnable {
    protected final TablistAdapter tablistAdapterVisualizer;

    public TablistUpdateTask() {
        this.tablistAdapterVisualizer = new TablistImpl(AlleyPlugin.getInstance());
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.tablistAdapterVisualizer.update(player);
        }
    }
}