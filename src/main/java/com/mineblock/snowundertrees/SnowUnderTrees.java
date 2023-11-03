package com.mineblock.snowundertrees;

import com.mineblock.snowundertrees.config.SnowUnderTreesConfig;
import com.mineblock.snowundertrees.world.SnowUnderTreesWorldgen;
import com.mineblock.snowundertrees.world.WorldTickHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;

public class SnowUnderTrees implements ModInitializer {
	public static final String MOD_ID = "snowundertrees";

	public static Identifier id(String path)
	{
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize()
	{
		SnowUnderTreesConfig.load();
		SnowUnderTreesWorldgen.initialize();

		WorldTickHandler.register();
	}
}
