package com.mineblock.snowundertrees.world;

import com.mineblock.snowundertrees.SnowUnderTrees;
import com.mineblock.snowundertrees.config.SnowUnderTreesConfig;
import com.mineblock.snowundertrees.world.feature.SnowUnderTreesFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;

public class SnowUnderTreesWorldgen {
    public static final Feature<DefaultFeatureConfig> SNOW_UNDER_TREES_FEATURE = new SnowUnderTreesFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> SNOW_UNDER_TREES_CONFIGURED = new ConfiguredFeature<>(SNOW_UNDER_TREES_FEATURE, new DefaultFeatureConfig());
    public static RegistryKey<PlacedFeature> SNOW_UNDER_TREES_PLACED_KEY;
    public static RegistryKey<ConfiguredFeature<?, ?>> SNOW_UNDER_TREES_CONFIGURED_KEY;

    public static void initialize() {
        Registry.register(Registries.FEATURE, SnowUnderTrees.id("snow_under_trees"), SnowUnderTreesWorldgen.SNOW_UNDER_TREES_FEATURE);
        SnowUnderTreesWorldgen.SNOW_UNDER_TREES_CONFIGURED_KEY = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, SnowUnderTrees.id("snow_under_trees"));
        SnowUnderTreesWorldgen.SNOW_UNDER_TREES_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, SnowUnderTrees.id("snow_under_trees"));

        BiomeModifications.addFeature(b -> SnowUnderTreesWorldgen.shouldAddSnow(b.getBiomeKey()), GenerationStep.Feature.TOP_LAYER_MODIFICATION, SnowUnderTreesWorldgen.SNOW_UNDER_TREES_PLACED_KEY);
    }

    public static boolean shouldAddSnow(RegistryKey<Biome> key)
    {
        Identifier id = key.getValue();
        return SnowUnderTreesConfig.get().enableBiomeFeature && SnowUnderTreesConfig.get().supportedBiomes.contains(id.toString());
    }
}
