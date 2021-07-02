package bl4ckscor3.mod.snowundertrees;

import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import com.google.common.collect.Lists;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = SnowUnderTrees.MODID)
public class Configuration implements ConfigData
{
	@Comment(value = "Set this to false to disable snow under trees when first generating chunks.")
	public boolean enableBiomeFeature = true;

	@Comment(value = "Set this to false to disable snow under trees when it's snowing.")
	public boolean enableWhenSnowing = true;

	@Comment(value = "Add biome IDs here to exempt biomes from being affected by the mod (surrounded by \"\"). You can find the biome ID of the biome you're currently in on the F3 screen.\n" +
			"For example, the biome ID of the plains biome looks like this: minecraft:plains")
	public List<? extends String> filteredBiomes = Lists.newArrayList();
}
