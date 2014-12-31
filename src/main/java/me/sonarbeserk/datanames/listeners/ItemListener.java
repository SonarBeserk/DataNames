/**
 * *********************************************************************************************************************
 * DataNames - Sets an item's display name based on its data
 * =====================================================================================================================
 *  Copyright (C) 2014 by SonarBeserk
 * https://github.com/SonarBeserk/DataNames
 * *********************************************************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * *********************************************************************************************************************
 * Please refer to LICENSE for the full license. If it is not there, see <http://www.gnu.org/licenses/>.
 * *********************************************************************************************************************
 */

package me.sonarbeserk.datanames.listeners;

import me.sonarbeserk.datanames.DataNames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemListener implements Listener {
    private DataNames plugin = null;

    public ItemListener(DataNames plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent e) {
        String itemId = e.getItem().getItemStack().getType().name() + ":" + e.getItem().getItemStack().getData().getData();

        if (plugin.getDisplayNameMap().containsKey(itemId)) {
            if (e.getItem().getItemStack().getItemMeta() != null) {
                ItemMeta meta = e.getItem().getItemStack().getItemMeta();

                meta.setDisplayName(plugin.getDisplayNameMap().get(itemId));
                e.getItem().getItemStack().setItemMeta(meta);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraft(CraftItemEvent e) {
        String itemId = e.getCurrentItem().getType().name() + ":" + e.getCurrentItem().getData().getData();

        if (plugin.getDisplayNameMap().containsKey(itemId)) {
            if (e.getCurrentItem().getItemMeta() != null) {
                ItemMeta meta = e.getCurrentItem().getItemMeta();

                meta.setDisplayName(plugin.getDisplayNameMap().get(itemId));
                e.getCurrentItem().setItemMeta(meta);
            }
        }
    }
}
