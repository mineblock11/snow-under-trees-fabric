package bl4ckscor3.mod.snowundertrees;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;

public class SnowUnderTrees implements ModInitializer
{
	public static final String MODID = "snowundertrees";
	public static RegistryKey<PlacedFeature> SNOW_UNDER_TREES_PLACED_KEY;
	public static Configuration CONFIG;

	private static final Feature<DefaultFeatureConfig> SNOW_UNDER_TREES_FEATURE = new SnowUnderTreesFeature(DefaultFeatureConfig.CODEC);
	public static final ConfiguredFeature<?, ?> SNOW_UNDER_TREES_CONFIGURED = new ConfiguredFeature<>(SNOW_UNDER_TREES_FEATURE, new DefaultFeatureConfig());
	private static final List<Identifier> biomesToAddTo = new ArrayList<>();
	public static RegistryKey<ConfiguredFeature<?, ?>> SNOW_UNDER_TREES_CONFIGURED_KEY;

	@Override
	public void onInitialize()
	{
		AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
		SnowUnderTrees.CONFIG = AutoConfig.getConfigHolder(Configuration.class).getConfig();
		ServerLifecycleEvents.SERVER_STARTING.register(server -> AutoConfig.getConfigHolder(Configuration.class).load());

		Registry.register(Registries.FEATURE, new Identifier(MODID, "snow_under_trees"), SNOW_UNDER_TREES_FEATURE);
		SNOW_UNDER_TREES_CONFIGURED_KEY = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(MODID, "snow_under_trees"));
		SNOW_UNDER_TREES_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(MODID, "snow_under_trees"));

		ServerTickEvents.START_WORLD_TICK.register(WorldTickHandler::onWorldTick);
		BiomeModifications.addFeature(b -> shouldAddSnow(b.getBiome(), b.getBiomeKey()), GenerationStep.Feature.TOP_LAYER_MODIFICATION, SNOW_UNDER_TREES_PLACED_KEY);
	}

	private boolean shouldAddSnow(Biome biome, RegistryKey<Biome> key)
	{
		Identifier id = key.getValue();
		return CONFIG.enableBiomeFeature && (biome.getPrecipitation() == Biome.Precipitation.SNOW || biomesToAddTo.contains(id)) && !CONFIG.filteredBiomes.contains(id.toString());
	}
}
