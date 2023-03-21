package com.mineblock.snowundertrees;

import java.util.List;

import com.google.common.collect.Lists;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Sync;

@Config(name = SnowUnderTrees.MODID, wrapperName = "SnowUnderTreesConfig")
@Modmenu(modId = SnowUnderTrees.MODID)
@Sync(value = Option.SyncMode.OVERRIDE_CLIENT)
public class Configuration
{
	public boolean enableBiomeFeature = true;
	public boolean enableWhenSnowing = true;
	public List<String> filteredBiomes = List.of(
			"minecraft:snowy_plains",
			"minecraft:ice_spikes",
			"minecraft:snowy_taiga",
			"minecraft:snowy_beach",
			"minecraft:snowy_slopes",
			"minecraft:jagged_peaks",
			"minecraft:frozen_peaks"
	);
}
