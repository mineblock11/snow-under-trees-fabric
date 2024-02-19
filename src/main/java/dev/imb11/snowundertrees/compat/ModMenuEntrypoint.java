package dev.imb11.snowundertrees.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.imb11.snowundertrees.config.SnowUnderTreesConfig;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> SnowUnderTreesConfig.getInstance().generateScreen(parent);
    }
}
