package com.mineblock.snowundertrees.config;

import com.mineblock.snowundertrees.SnowUnderTrees;
import com.mineblock11.mru.config.YACLHelper;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.Serial;
import java.nio.file.Path;
import java.util.List;

public class SnowUnderTreesConfig {
    private static final YACLHelper.NamespacedHelper HELPER = new YACLHelper.NamespacedHelper("snowundertrees");
    public static ConfigClassHandler<SnowUnderTreesConfig> CONFIG_CLASS_HANDLER = HELPER.createHandler(SnowUnderTreesConfig.class);

    @SerialEntry
    public boolean enableBiomeFeature = true;

    @SerialEntry
    public boolean enableWhenSnowing = true;

    @SerialEntry
    public List<String> supportedBiomes = List.of(
            "minecraft:snowy_plains",
            "minecraft:ice_spikes",
            "minecraft:snowy_taiga",
            "minecraft:snowy_beach",
            "minecraft:snowy_slopes",
            "minecraft:jagged_peaks",
            "minecraft:frozen_peaks",
            "biomesoplenty:auroral_garden",
            "biomesoplenty:muskeg",
            "biomesoplenty:snowy_fir_clearing",
            "biomesoplenty:snowy_coniferous_forest",
            "biomesoplenty:fir_clearing",
            "biomesoplenty:coniferous_forest",
            "terralith:alpha_islands_winter",
            "terralith:alpine_grove",
            "terralith:emerald_peaks",
            "terralith:frozen_cliffs",
            "terralith:glacial_chasm",
            "terralith:rocky_shrubland",
            "terralith:scarlet_mountains",
            "terralith:skylands_winter",
            "terralith:snowy_badlands",
            "terralith:snowy_cherry_grove",
            "terralith:snowy_maple_forest",
            "terralith:snowy_shield",
            "terralith:wintry_forest",
            "terralith:wintry_lowlands",
            "byg:cardinal_tundra",
            "byg:shattered_glacier",
            "byg:frosted_taiga",
            "byg:frosted_coniferous_forest"
    );

    public static SnowUnderTreesConfig get() {
        return CONFIG_CLASS_HANDLER.instance();
    }

    public static void load() {
        // Check if "snowundertrees.json5" exists, and rename it to "snowundertrees.config.json"
        Path oldConfigPath = FabricLoader.getInstance().getConfigDir().resolve("snowundertrees.json5");
        Path newConfigPath = FabricLoader.getInstance().getConfigDir().resolve("snowundertrees.config.json");

        if (oldConfigPath.toFile().exists()) {
            oldConfigPath.toFile().renameTo(newConfigPath.toFile());
        }

        CONFIG_CLASS_HANDLER.load();
    }

    public static YetAnotherConfigLib getInstance() {
        return YetAnotherConfigLib.create(CONFIG_CLASS_HANDLER, (SnowUnderTreesConfig defaults, SnowUnderTreesConfig config, YetAnotherConfigLib.Builder builder) -> {
            var enableWhenSnowingOption = Option.<Boolean>createBuilder()
                    .name(HELPER.getName("enableWhenSnowing"))
                    .description(HELPER.description("enableWhenSnowing", true))
                    .binding(defaults.enableWhenSnowing, () -> config.enableWhenSnowing, (v) -> config.enableWhenSnowing = v)
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .build();

            var supportedBiomesOption = ListOption.<String>createBuilder()
                    .name(HELPER.getName("supportedBiomes"))
                    .description(HELPER.description("supportedBiomes", false))
                    .controller(StringControllerBuilder::create)
                    .initial("minecraft:plains")
                    .binding(defaults.supportedBiomes, () -> config.supportedBiomes, (v) -> config.supportedBiomes = v)
                    .build();

            var enableBiomeFeatureOption = Option.<Boolean>createBuilder()
                    .name(HELPER.getName("enableBiomeFeature"))
                    .description(HELPER.description("enableBiomeFeature", true))
                    .binding(defaults.enableBiomeFeature, () -> config.enableBiomeFeature, (v) -> config.enableBiomeFeature = v)
                    .listener((opt, val) -> {
                        enableWhenSnowingOption.setAvailable(val);
                        supportedBiomesOption.setAvailable(val);
                    })
                    .controller(opt -> BooleanControllerBuilder.create(opt).yesNoFormatter().coloured(true))
                    .build();

            return builder
                    .title(Text.translatable("snowundertrees.config.title"))
                    .category(ConfigCategory.createBuilder()
                            .name(Text.translatable("snowundertrees.config.title"))
                            .options(List.of(
                                    enableBiomeFeatureOption,
                                    enableWhenSnowingOption
                            ))
                            .group(supportedBiomesOption)
                            .build());
        });

    }
}
