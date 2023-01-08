package bl4ckscor3.mod.snowundertrees.data;

import bl4ckscor3.mod.snowundertrees.SnowUnderTrees;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class SnowUnderTreesDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(WorldgenProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, SnowUnderTreesBootstrap::configuredFeatures);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, SnowUnderTreesBootstrap::placedFeatures);
    }

    @Override
    public String getEffectiveModId() {
        return SnowUnderTrees.MODID;
    }
}