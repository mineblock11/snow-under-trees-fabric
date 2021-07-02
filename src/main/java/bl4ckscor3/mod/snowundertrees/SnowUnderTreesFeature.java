package bl4ckscor3.mod.snowundertrees;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class SnowUnderTreesFeature extends Feature<DefaultFeatureConfig>
{
	public SnowUnderTreesFeature(Codec<DefaultFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random rand, BlockPos pos, DefaultFeatureConfig config)
	{
		BlockPos.Mutable mPos = new BlockPos.Mutable();

		for(int xi = 0; xi < 16; xi++)
		{
			for(int zi = 0; zi < 16; zi++)
			{
				int x = pos.getX() + xi;
				int z = pos.getZ() + zi;

				mPos.set(x, world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z) - 1, z);

				if(world.getBlockState(mPos).getBlock() instanceof LeavesBlock)
				{
					mPos.set(x, world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z), z);

					if(world.getBiome(mPos).canSetSnow(world, mPos))
					{
						BlockState stateBelow;

						world.setBlockState(mPos, Blocks.SNOW.getDefaultState(), 2);
						mPos.move(Direction.DOWN);
						stateBelow = world.getBlockState(mPos);

						if(stateBelow.contains(SnowyBlock.SNOWY))
							world.setBlockState(mPos, stateBelow.with(SnowyBlock.SNOWY, true), 2);
					}
				}
			}
		}

		return true;
	}
}
