package dev.imb11.snowundertrees.world;

import dev.imb11.snowundertrees.SnowUnderTrees;
import dev.imb11.snowundertrees.config.SnowUnderTreesConfig;
import dev.imb11.snowundertrees.world.feature.SnowUnderTreesFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;

public class SnowUnderTreesWorldgen {
    private static final Feature<DefaultFeatureConfig> SNOW_UNDER_TREES_FEATURE = new SnowUnderTreesFeature(DefaultFeatureConfig.CODEC);
    private static final ConfiguredFeature<?, ?> SNOW_UNDER_TREES_CONFIGURED = new ConfiguredFeature<>(SNOW_UNDER_TREES_FEATURE, new DefaultFeatureConfig());
    private static final RegistryKey<ConfiguredFeature<?, ?>> SNOW_UNDER_TREES_CONFIGURED_KEY = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, SnowUnderTrees.id("snow_under_trees"));
    private static final RegistryKey<PlacedFeature> SNOW_UNDER_TREES_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, SnowUnderTrees.id("snow_under_trees"));

    public static void initialize() {
        // Register the core feature
        Registry.register(Registries.FEATURE, SnowUnderTrees.id("snow_under_trees"), SNOW_UNDER_TREES_FEATURE);

        // Add biome modification
        BiomeModifications.addFeature(biome -> shouldAddSnow(biome.getBiomeKey()), GenerationStep.Feature.TOP_LAYER_MODIFICATION, SNOW_UNDER_TREES_PLACED_KEY);
    }

    private static boolean shouldAddSnow(RegistryKey<Biome> biomeKey) {
        return SnowUnderTreesConfig.get().enableBiomeFeature &&
                SnowUnderTreesConfig.get().supportedBiomes.contains(biomeKey.getValue().toString());
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> configuredKey() {
        return SNOW_UNDER_TREES_CONFIGURED_KEY;
    }

    public static ConfiguredFeature<?, ?> configuredFeature() {
        return SNOW_UNDER_TREES_CONFIGURED;
    }

    public static RegistryKey<PlacedFeature> placedKey() {
        return SNOW_UNDER_TREES_PLACED_KEY;
    }

    public static Feature<?> feature() {
        return SNOW_UNDER_TREES_FEATURE;
    }
}
