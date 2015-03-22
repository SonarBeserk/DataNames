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

package com.serkprojects.datanames.conversations.nameremoval;

import com.serkprojects.datanames.DataNames;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;

public class NameRemovalAbandonListener implements ConversationAbandonedListener {
    private DataNames plugin = null;

    public NameRemovalAbandonListener(DataNames plugin) {
        this.plugin = plugin;
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent e) {
        if (e.getContext().getForWhom() == null) {
            return;
        }

        if (e.gracefulExit()) {
            if (e.getContext().getSessionData("nameRemoved") != null) {
                boolean nameChanged = (boolean) e.getContext().getSessionData("nameRemoved");

                if (nameChanged) {
                    e.getContext().getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("nameRemoved")));
                } else {
                    e.getContext().getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("nameNotRemoved")));
                }
            }
        } else {

        }
    }
}
