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

package me.sonarbeserk.datanames.conversations.nameremoval;

import me.sonarbeserk.datanames.DataNames;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class ConfirmNameRemovalPrompt extends StringPrompt {
    private DataNames plugin = null;

    public ConfirmNameRemovalPrompt(DataNames plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptNameRemoval").replace("{termYes}", plugin.getLanguage().getMessage("termYes")).replace("{termNo}", plugin.getLanguage().getMessage("termNo")).replace("{timeout}", plugin.getConfig().getString("settings.timeout.changeDisplayName")));
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String s) {
        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termYes")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termYes").toCharArray()[0]))) {
            plugin.getDisplayNameMap().remove(String.valueOf(context.getSessionData("itemid")));
            context.setSessionData("nameRemoved", true);
            return Prompt.END_OF_CONVERSATION;
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termNo")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termNo").toCharArray()[0]))) {
            context.setSessionData("nameRemoved", false);
            return Prompt.END_OF_CONVERSATION;
        }

        return this;
    }
}
