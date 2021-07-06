package bl4ckscor3.mod.snowundertrees;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class SnowUnderTrees implements ModInitializer
{
	public static final String MODID = "snowundertrees";
	public static Configuration CONFIG;

	private static final Feature<DefaultFeatureConfig> SNOW_UNDER_TREES_FEATURE = new SnowUnderTreesFeature(DefaultFeatureConfig.CODEC);
	public static final ConfiguredFeature<?, ?> SNOW_UNDER_TREES_CONFIGURED = SNOW_UNDER_TREES_FEATURE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(DecoratorConfig.DEFAULT));
	private static List<Identifier> biomesToAddTo = new ArrayList<>();

	public SnowUnderTrees()
	{

	}

	@Override
	public void onInitialize()
	{
		AutoConfig.register(Configuration.class, JanksonConfigSerializer::new);
		SnowUnderTrees.CONFIG = AutoConfig.getConfigHolder(Configuration.class).getConfig();
		ServerLifecycleEvents.SERVER_STARTING.register(server -> AutoConfig.getConfigHolder(Configuration.class).load());

		Registry.register(Registry.FEATURE, new Identifier(MODID, "snow_under_trees"), SNOW_UNDER_TREES_FEATURE);
		RegistryKey<ConfiguredFeature<?, ?>> snowUnderTrees = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(MODID, "snow_under_trees"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, snowUnderTrees.getValue(), SNOW_UNDER_TREES_CONFIGURED);

		ServerTickEvents.START_WORLD_TICK.register(WorldTickHandler::onWorldTick);
		BiomeModifications.addFeature(b -> shouldAddSnow(b.getBiome(), b.getBiomeKey()), GenerationStep.Feature.TOP_LAYER_MODIFICATION, snowUnderTrees);
	}

	private boolean shouldAddSnow(Biome biome, RegistryKey<Biome> key)
	{
		Identifier id = key.getValue();
		return CONFIG.enableBiomeFeature && (biome.getPrecipitation() == Biome.Precipitation.SNOW || biomesToAddTo.contains(id)) && !CONFIG.filteredBiomes.contains(id.toString());
	}

	public static void addSnowUnderTrees(Biome biome)
	{
		Identifier id = BuiltinRegistries.BIOME.getId(biome);
		if (!biomesToAddTo.contains(id))
			biomesToAddTo.add(id);
	}
}
