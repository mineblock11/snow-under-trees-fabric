package com.mineblock.snowundertrees.compat;

import com.mineblock.snowundertrees.config.SnowUnderTreesConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> SnowUnderTreesConfig.getInstance().generateScreen(parent);
    }
}
