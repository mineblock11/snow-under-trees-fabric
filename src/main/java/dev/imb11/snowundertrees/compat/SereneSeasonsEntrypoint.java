
package dev.imb11.snowundertrees.compat;

import com.mineblock11.mru.entry.CompatabilityEntrypoint;
import dev.imb11.snowundertrees.config.SnowUnderTreesConfig;
import dev.imb11.snowundertrees.mixins.ThreadedAnvilChunkStorageInvoker;
import dev.imb11.snowundertrees.world.WorldTickHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? >=1.20.4 {
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.init.ModConfig;
import sereneseasons.season.SeasonHooks;
//?}

import java.util.HashMap;

public class SereneSeasonsEntrypoint implements CompatabilityEntrypoint {
    private static final Logger LOGGER = LoggerFactory.getLogger("SnowUnderTrees/SereneSeasons");
    public static boolean isSereneSeasonsLoaded = false;

    public static boolean isBiomeSuitable(ServerWorld world, WorldChunk chunk, BlockPos biomeCheckPos, Biome biome) {
        return SeasonHooks.shouldSnowHook(biome, world, biomeCheckPos);
    }

    @Override
    public void initialize() {
        if (!FabricLoader.getInstance().isModLoaded("sereneseasons")) return;

        LOGGER.info("Serene Seasons detected!");

        //? >=1.20.4 {
        isSereneSeasonsLoaded = true;
        ServerTickEvents.END_WORLD_TICK.register(SereneSeasonsEntrypoint::attemptMeltSnow);
        //?}
    }

    //? >=1.20.4 {
    private static void attemptMeltSnow(ServerWorld serverWorld) {
        if (!isWinter(serverWorld) || !SnowUnderTreesConfig.get().meltSnowSeasonally) return;
        if (!shouldMeltSnow(serverWorld, SeasonHelper.getSeasonState(serverWorld).getSubSeason())) return;

        /*? if <1.21 {*/
        /*ThreadedAnvilChunkStorageInvoker chunkStorage = (ThreadedAnvilChunkStorageInvoker) serverWorld.getChunkManager().threadedAnvilChunkStorage;
        *//*?} else {*/
        ThreadedAnvilChunkStorageInvoker chunkStorage = (ThreadedAnvilChunkStorageInvoker) serverWorld.getChunkManager().chunkLoadingManager;
         /*?}*/

        var chunks = chunkStorage.invokeEntryIterator();

        for (var chunk : chunks) {
            BlockPos randomPosition = serverWorld.getRandomPosInChunk(chunk.getPos().getStartX(), 0, chunk.getPos().getStartZ(), 15);
            if (!isBiomeSuitable(serverWorld, chunk.getWorldChunk(), randomPosition, serverWorld.getBiome(randomPosition).value())) {
                continue;
            }

            BlockPos heightmapPosition = serverWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING, randomPosition).down();
            BlockState blockState = serverWorld.getBlockState(heightmapPosition);
            if (!blockState.isIn(BlockTags.LEAVES)) {
                continue;
            }

            BlockPos pos = serverWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, randomPosition);
            if (!SeasonHooks.warmEnoughToRainSeasonal(serverWorld, pos)) {
                continue;
            }

            BlockState before = serverWorld.getBlockState(pos);
            BlockState after = Blocks.AIR.getDefaultState();

            if (before == after) {
                continue;
            }

            BlockPos downPos = pos.down();
            BlockState below = serverWorld.getBlockState(downPos);

            serverWorld.setBlockState(pos, after);
            LOGGER.info("Melted snow at: {}", pos.toShortString());

            if (below.contains(SnowyBlock.SNOWY)) {
                serverWorld.setBlockState(downPos, below.with(SnowyBlock.SNOWY, false), 2);
                LOGGER.info("Melted snow at: {}", downPos.toShortString());
            }
        }
    }

    private static final HashMap<Object, Integer> MELT_CHANCES = new HashMap<>();

    static {
        if (FabricLoader.getInstance().isModLoaded("sereneseasons")) {
            MELT_CHANCES.put(Season.SubSeason.EARLY_SPRING, 16);
            MELT_CHANCES.put(Season.SubSeason.MID_SPRING, 12);
            MELT_CHANCES.put(Season.SubSeason.LATE_SPRING, 8);
            MELT_CHANCES.put(Season.SubSeason.EARLY_SUMMER, 4);
            MELT_CHANCES.put(Season.SubSeason.MID_SUMMER, 2);
            MELT_CHANCES.put(Season.SubSeason.LATE_SUMMER, 1);
            MELT_CHANCES.put(Season.SubSeason.EARLY_AUTUMN, 8);
            MELT_CHANCES.put(Season.SubSeason.MID_AUTUMN, 12);
            MELT_CHANCES.put(Season.SubSeason.LATE_AUTUMN, 16);
        }
    }

    private static boolean shouldMeltSnow(ServerWorld world, Season.SubSeason subSeason) {
        int chance = MELT_CHANCES.getOrDefault(subSeason, -1);
        if (chance == -1) return false;
        LOGGER.info("Chance: {}", chance);
        var rnd = world.random.nextBetween(0, chance);
        LOGGER.info("Random: {}", rnd);
        return rnd == 0;
    }

    public static boolean isWinter(World world) {
        return SeasonHelper.getSeasonState(world).getSeason() == Season.WINTER;
    }
    //?}

    //? >=1.20.4 {
    public static boolean shouldPlaceSnow(World world, BlockPos pos) {
        if (isSereneSeasonsLoaded) {
            return ModConfig.seasons.generateSnowAndIce && SeasonHooks.coldEnoughToSnowSeasonal(world, pos);
        } else {
            return false;
        }
    }
    //?} else {
    /*public static boolean shouldPlaceSnow(World world, BlockPos pos) {
        return true;
    }
    *///?}
}