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

package com.serkprojects.datanames;

import com.serkprojects.serkcore.plugin.JavaPlugin;
import com.serkprojects.datanames.commands.DisplayNameCMD;
import com.serkprojects.datanames.listeners.ItemListener;

import java.util.HashMap;
import java.util.Map;

public class DataNames extends JavaPlugin {
    private Map<String, String> displayNameMap = null;

    @Override
    public void onEnable() {
        super.onEnable();

        displayNameMap = new HashMap<String, String>();

        if (getData().get("ids") != null) {
            for (String id : getData().getConfigurationSection("ids").getKeys(false)) {
                if (getData().get("ids." + id + ".displayName") != null) {
                    displayNameMap.put(id, String.valueOf(getData().get("ids." + id + ".displayName")));
                }
            }
        }

        getServer().getPluginManager().registerEvents(new ItemListener(this), this);

        getCommand("displayname").setExecutor(new DisplayNameCMD(this));
    }

    @Override
    public boolean shouldSaveData() {
        return true;
    }

    @Override
    public boolean registerPremadeMainCMD() {
        return true;
    }

    @Override
    public String getPermissionPrefix() {
        return getConfig().getString("settings.permissionPrefix");
    }

    @Override
    public void onReload() {
    }

    /**
     * Returns the display name map
     *
     * @return the display name map
     */
    public Map<String, String> getDisplayNameMap() {
        return displayNameMap;
    }

    @Override
    public void onDisable() {
        for (String id : displayNameMap.keySet()) {
            getData().set("ids." + id + ".displayName", displayNameMap.get(id));
        }

        super.onDisable();
    }
}
