package com.mineblock.snowundertrees.data;

import com.mineblock.snowundertrees.SnowUnderTrees;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class SnowUnderTreesDatagen implements DataGeneratorEntrypoint {

    /**
     * This method is called by the Fabric Datagen module to generate data.
     * @param fabricDataGenerator The {@link FabricDataGenerator} instance
     */
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(WorldgenProvider::new);
    }

    /**
     * This method is called by the Fabric Datagen module to build registries.
     * @param registryBuilder a {@link RegistryBuilder} instance
     */
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        // Add all the registries we want to generate data for here.
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, SnowUnderTreesBootstrap::configuredFeatures);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, SnowUnderTreesBootstrap::placedFeatures);
    }

    @Override
    public String getEffectiveModId() {
        return SnowUnderTrees.MOD_ID;
    }
}