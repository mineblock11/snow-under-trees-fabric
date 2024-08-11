package dev.imb11.snowundertrees.compat;

import com.mineblock11.mru.entry.CompatabilityEntrypoint;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sereneseasons.api.season.Season;
import sereneseasons.init.ModAPI;
import sereneseasons.init.ModConfig;
import sereneseasons.season.SeasonHandler;

public class SereneSeasonsEntrypoint implements CompatabilityEntrypoint {
    private static final Logger LOGGER = LoggerFactory.getLogger("SnowUnderTrees/SereneSeasons");
    private @Nullable static SeasonHandler seasonHandler;
    public static boolean isSereneSeasonsLoaded = false;
    @Override
    public void initialize() {
        LOGGER.info("Serene Seasons detected!");
        isSereneSeasonsLoaded = true;
        seasonHandler = new SeasonHandler();
    }

    public static boolean shouldPlaceSnow(World world) {
        if(isSereneSeasonsLoaded) {
            return ModConfig.seasons.generateSnowAndIce && seasonHandler.getServerSeasonState(world).getSeason() == Season.WINTER;
        } else {
            return false;
        }
    }
}
