package dev.imb11.snowundertrees.world.feature;

import com.mojang.serialization.Codec;
import dev.imb11.snowundertrees.compat.SereneSeasonsEntrypoint;
import dev.imb11.snowundertrees.config.SnowUnderTreesConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SnowUnderTreesFeature extends Feature<DefaultFeatureConfig> {

    public SnowUnderTreesFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        if (!SnowUnderTreesConfig.get().enableBiomeFeature) {
            return false;
        }

        if(SnowUnderTreesConfig.get().respectSeasonMods) {
            if(!SereneSeasonsEntrypoint.shouldPlaceSnow(context.getWorld().toServerWorld(), context.getOrigin())) {
                return false;
            }
        }

        BlockPos origin = context.getOrigin();
        StructureWorldAccess world = context.getWorld();

        // Iterate within a 16x16 area around the feature origin
        for (int xOffset = 0; xOffset < 16; xOffset++) {
            for (int zOffset = 0; zOffset < 16; zOffset++) {
                int x = origin.getX() + xOffset;
                int z = origin.getZ() + zOffset;

                // Find top surfaces
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z) - 1;
                BlockPos currentPos = new BlockPos(x, y, z);

                // Early exit if not leaves
                if (!(world.getBlockState(currentPos).getBlock() instanceof LeavesBlock)) {
                    continue; // Skip to the next iteration if not leaves
                }

                // Find ground below leaves
                y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
                currentPos = currentPos.withY(y);

                // Biome check for snow suitability
                Biome biome = world.getBiome(currentPos).value();
                if (!biome.canSetSnow(world, currentPos)) {
                    continue; // Skip if this biome doesn't allow snow
                }

                // Snow placement
                world.setBlockState(currentPos, Blocks.SNOW.getDefaultState(), 2);

                BlockPos belowPos = currentPos.down();
                BlockState belowState = world.getBlockState(belowPos);
                if (belowState.contains(SnowyBlock.SNOWY)) {
                    world.setBlockState(belowPos, belowState.with(SnowyBlock.SNOWY, true), 2);
                }
            }
        }

        return true;
    }
}