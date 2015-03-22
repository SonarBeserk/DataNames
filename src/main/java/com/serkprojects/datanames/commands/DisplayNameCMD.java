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

package com.serkprojects.datanames.commands;

import com.serkprojects.datanames.DataNames;
import com.serkprojects.datanames.conversations.namechange.ConfirmNameChangePrompt;
import com.serkprojects.datanames.conversations.namechange.NameChangeAbandonListener;
import com.serkprojects.datanames.conversations.nameremoval.ConfirmNameRemovalPrompt;
import com.serkprojects.datanames.conversations.nameremoval.NameRemovalAbandonListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public class DisplayNameCMD implements CommandExecutor {
    private DataNames plugin = null;

    public DisplayNameCMD(DataNames plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("usageDisplay").replace("{name}", plugin.getDescription().getName()));
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                helpSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                setSubCommand(sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("remove")) {
                removeSubCommand(sender);
                return true;
            }

            helpSubCommand(sender);
        }

        return true;
    }

    private boolean permissionCheck(CommandSender sender, String permission, boolean autoMessage) {
        if (!sender.hasPermission(permission)) {
            if (autoMessage) {
                plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("noPermission"));
                return false;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void helpSubCommand(CommandSender sender) {
        plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("usageDisplay"));
        return;
    }

    private void setSubCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("commandPlayerRequired"));
            return;
        }

        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.set", true)) {
            return;
        }

        Player senderPlayer = (Player) sender;

        if (senderPlayer.getItemInHand() == null) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("notHoldingItem"));
            return;
        } else if (senderPlayer.getItemInHand().getItemMeta() == null || senderPlayer.getItemInHand().getItemMeta().getDisplayName() == null) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("notHoldingItemWithDisplayName"));
            return;
        }

        String itemId = senderPlayer.getItemInHand().getType().name() + ":" + senderPlayer.getItemInHand().getData().getData();

        if (plugin.getDisplayNameMap().containsKey(itemId)) {
            ConversationFactory conversationFactory = new ConversationFactory(plugin);

            Conversation conversation = conversationFactory.withModality(true).withLocalEcho(false).withFirstPrompt(new ConfirmNameChangePrompt(plugin)).withTimeout(plugin.getConfig().getInt("settings.timeout.changeDisplayName")).addConversationAbandonedListener(new NameChangeAbandonListener(plugin)).buildConversation(senderPlayer);
            conversation.getContext().setSessionData("itemid", itemId);
            conversation.getContext().setSessionData("name", senderPlayer.getItemInHand().getItemMeta().getDisplayName());
            conversation.begin();
        } else {
            plugin.getDisplayNameMap().put(itemId, senderPlayer.getItemInHand().getItemMeta().getDisplayName());
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("nameChanged"));
        }
    }

    private void removeSubCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("commandPlayerRequired"));
            return;
        }

        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.remove", true)) {
            return;
        }

        Player senderPlayer = (Player) sender;

        if (senderPlayer.getItemInHand() == null) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("notHoldingItem"));
            return;
        } else if (senderPlayer.getItemInHand().getItemMeta() == null || senderPlayer.getItemInHand().getItemMeta().getDisplayName() == null) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("notHoldingItemWithDisplayName"));
            return;
        }

        String itemId = senderPlayer.getItemInHand().getType().name() + ":" + senderPlayer.getItemInHand().getData().getData();

        if (!plugin.getDisplayNameMap().containsKey(itemId)) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("notSetDisplayName"));
        } else {
            ConversationFactory conversationFactory = new ConversationFactory(plugin);

            Conversation conversation = conversationFactory.withModality(true).withLocalEcho(false).withFirstPrompt(new ConfirmNameRemovalPrompt(plugin)).withTimeout(plugin.getConfig().getInt("settings.timeout.removeDisplayName")).addConversationAbandonedListener(new NameRemovalAbandonListener(plugin)).buildConversation(senderPlayer);
            conversation.getContext().setSessionData("itemid", itemId);
            conversation.getContext().setSessionData("name", senderPlayer.getItemInHand().getItemMeta().getDisplayName());
            conversation.begin();
        }
    }
}
