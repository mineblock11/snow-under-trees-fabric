package com.mineblock.snowundertrees.world;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mineblock.snowundertrees.config.SnowUnderTreesConfig;
import com.mineblock.snowundertrees.mixins.ThreadedAnvilChunkStorageInvoker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;

public class WorldTickHandler
{
	public static void register()
	{
		ServerTickEvents.START_WORLD_TICK.register(WorldTickHandler::onWorldTick);
	}

	public static void onWorldTick(ServerWorld world)
	{
		if (world.isRaining() && SnowUnderTreesConfig.get().enableWhenSnowing)
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
					var optionalKey = world.getRegistryManager().get(RegistryKeys.BIOME).getKey(biome);
					AtomicBoolean biomeDisabled = new AtomicBoolean(true);

					optionalKey.ifPresent((key) -> biomeDisabled.set(SnowUnderTreesConfig.get().supportedBiomes.contains(key.getValue().toString())));

					if (!biomeDisabled.get() && world.getBlockState(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, randomPos).down()).getBlock() instanceof LeavesBlock)
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
