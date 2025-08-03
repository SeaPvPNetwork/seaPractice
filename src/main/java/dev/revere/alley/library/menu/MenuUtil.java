package dev.revere.alley.library.menu;

import dev.revere.alley.library.menu.pagination.PaginatedMenu;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 24/01/2025
 */
@UtilityClass
public class MenuUtil {
    /**
     * Checks if the model has a next page.
     *
     * @param player the model viewing the menu
     * @param offset the delta to modify the page number by
     * @param menu   the menu
     * @return true if the model has a next page
     */
    public boolean hasNext(Player player, int offset, PaginatedMenu menu) {
        int pg = menu.getPage() + offset;
        return menu.getPages(player) >= pg;
    }

    /**
     * Checks if the model has a previous page.
     *
     * @param offset the delta to modify the page number by
     * @param menu   the menu
     * @return true if the model has a previous page
     */
    public boolean hasPrevious(int offset, PaginatedMenu menu) {
        int pg = menu.getPage() + offset;
        return pg > 0;
    }

    /**
     * Calculates the number of rows to increment by one.
     *
     * @param buttons the buttons to display
     * @return the number of rows to increment by one
     */
    public int calculateRequiredRows(Map<Integer, Button> buttons) {
        return (int) Math.ceil(buttons.size() / 9.0) + 1;
    }
}