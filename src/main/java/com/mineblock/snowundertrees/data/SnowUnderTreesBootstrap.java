package com.mineblock.snowundertrees.data;


import com.mineblock.snowundertrees.SnowUnderTrees;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.*;

import java.util.List;

public class SnowUnderTreesBootstrap {
    public SnowUnderTreesBootstrap() {
    }

    /**
     * Main method for creating configured features.
     *
     * See also <a href="https://minecraft.fandom.com/wiki/Custom_feature#Configured_Feature">Configured Feature</a>
     * on the Minecraft Wiki.
     */
    public static void configuredFeatures(Registerable<ConfiguredFeature<?, ?>> registry) {
        var placedFeatureLookup = registry.getRegistryLookup(RegistryKeys.PLACED_FEATURE);

        registry.register(SnowUnderTrees.SNOW_UNDER_TREES_CONFIGURED_KEY, SnowUnderTrees.SNOW_UNDER_TREES_CONFIGURED);
    }

    /**
     * Main method for creating placed features.
     *
     * See also <a href="https://minecraft.fandom.com/wiki/Custom_feature#Placed_Feature">Placed Feature</a>
     * on the Minecraft Wiki.
     */
    public static void placedFeatures(Registerable<PlacedFeature> registry) {
        var configuredFeatureLookup = registry.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        registry.register(SnowUnderTrees.SNOW_UNDER_TREES_PLACED_KEY, new PlacedFeature(configuredFeatureLookup.getOrThrow(SnowUnderTrees.SNOW_UNDER_TREES_CONFIGURED_KEY), List.of()));
    }
}