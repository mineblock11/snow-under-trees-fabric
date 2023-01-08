package bl4ckscor3.mod.snowundertrees;

import java.util.Optional;

import bl4ckscor3.mod.snowundertrees.mixins.ThreadedAnvilChunkStorageInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;

public class WorldTickHandler
{
	public static void onWorldTick(ServerWorld world)
	{
		if (world.isRaining() && SnowUnderTrees.CONFIG.enableWhenSnowing)
		{
			((ThreadedAnvilChunkStorageInvoker) world.getChunkManager().threadedAnvilChunkStorage).invokeEntryIterator().forEach(chunkHolder -> {
				Optional<WorldChunk> optional = chunkHolder.getEntityTickingFuture().getNow(ChunkHolder.UNLOADED_WORLD_CHUNK).left();

				if (optional.isPresent() && world.random.nextInt(16) == 0)
				{
					WorldChunk chunk = optional.get();
					ChunkPos chunkPos = chunk.getPos();
					int chunkX = chunkPos.getStartX();
					int chunkY = chunkPos.getStartZ();
					BlockPos randomPos = world.getRandomPosInChunk(chunkX, 0, chunkY, 15);
					Biome biome = world.getBiome(randomPos).value();
					boolean biomeDisabled = SnowUnderTrees.CONFIG.filteredBiomes.contains(world.getRegistryManager().get(Registry.BIOME_KEY).getKey(biome).toString());

					if (!biomeDisabled && world.getBlockState(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, randomPos).down()).getBlock() instanceof LeavesBlock)
					{
						BlockPos pos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, randomPos);
						BlockState state = world.getBlockState(pos);

						if (biome.canSetSnow(world, pos) && state.isAir())
						{
							BlockPos downPos = pos.down();
							BlockState stateBelow = world.getBlockState(downPos);

							if (stateBelow.isSideSolidFullSquare(world, downPos, Direction.UP))
							{
								world.setBlockState(pos, Blocks.SNOW.getDefaultState());

								if (stateBelow.contains(SnowyBlock.SNOWY))
									world.setBlockState(downPos, stateBelow.with(SnowyBlock.SNOWY, true), 2);
							}
						}
					}
				}
			});
		}
	}
}
