package net.xalcon.torchmaster;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.Logging.CORE;
import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TorchmasterConfig
{
    public static final ForgeConfigSpec spec;
    public static final General GENERAL;

    static
    {
        final Pair<General, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(General::new);
        spec = specPair.getRight();
        GENERAL = specPair.getLeft();
    }

    public static class General
    {
        public final ForgeConfigSpec.ConfigValue<Boolean> beginnerTooltips;
        public final ForgeConfigSpec.ConfigValue<Boolean> blockOnlyNaturalSpawns;
        public final ForgeConfigSpec.ConfigValue<Boolean> lycanitesMobsBlockAll;
        public final ForgeConfigSpec.ConfigValue<Integer> megaTorchRadius;
        public final ForgeConfigSpec.ConfigValue<Integer> dreadLampRadius;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> megaTorchEntityBlockListOverrides;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> dreadLampEntityBlockListOverrides;

        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareRadius;
        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareTickRate;
        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareMinLightLevel;
        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareLanternLightCountHardcap;
        public final ForgeConfigSpec.ConfigValue<Integer> frozenPearlDurability;

        public final ForgeConfigSpec.ConfigValue<Boolean> logSpawnChecks;

        private General(ForgeConfigSpec.Builder builder)
        {
            builder.push("General");

            beginnerTooltips = builder
                .comment("Show additional information in the tooltip of certain items and blocks")
                .translation("torchmaster.config.beginnerTooltips.description")
                .define("beginnerTooltips", true);

            blockOnlyNaturalSpawns = builder
                .comment("By default, mega torches only block natural spawns (i.e. from low light levels). Setting this to false will also block spawns from spawners")
                .translation("torchmaster.config.blockOnlyNaturalSpawns.description")
                .define("blockOnlyNaturalSpawns", true);

            lycanitesMobsBlockAll = builder
                .comment("If this setting is enabled, the mega torch will block all natural spawn attempts from Lycanites Mobs in its radius")
                .translation("torchmaster.config.lycanitesMobsBlockAll.description")
                .define("lycanitesMobsBlockAll", true);

            megaTorchRadius = builder
                .comment("The radius of the mega torch in each direction (cube) with the torch at its center")
                .translation("torchmaster.config.megaTorchRadius.description")
                .defineInRange("megaTorchRadius", 64, 0, Integer.MAX_VALUE);

            dreadLampRadius = builder
                .comment("The radius of the dread lamp in each direction (cube) with the torch at its center")
                .translation("torchmaster.config.dreadLamp.description")
                .defineInRange("dreadLampRadius", 64, 0, Integer.MAX_VALUE);

            megaTorchEntityBlockListOverrides = builder
                .comment(
                    "Use this setting to override the internal lists for entity blocking",
                    "You can use this to block more entities or even allow certain entities to still spawn",
                    "The + prefix will add the entity to the list, effectivly denying its spawns",
                    "The - prefix will remove the entity from the list (if necessary), effectivly allowing its spawns",
                    "Note: Each entry needs to be put in quotes! Multiple Entries should be separated by comma.",
                    "Block zombies: \"+minecraft:zombie\"",
                    "Allow creepers: \"-minecraft:creeper\"")
                .translation("torchmaster.config.megaTorch.blockListOverrides.description")
                .defineList("megaTorchEntityBlockListOverrides", new ArrayList<>(), o -> o instanceof String);

            dreadLampEntityBlockListOverrides = builder
                .comment(
                    "Same as the mega torch block list override, just for the dread lamp",
                    "Block squid: +minecraft:squid",
                    "Allow pigs: -minecraft:pig")
                .translation("torchmaster.config.dreadLamp.blockListOverrides.description")
                .defineList("dreadLampEntityBlockListOverrides", new ArrayList<>(), o -> o instanceof String);

            feralFlareRadius = builder
                .comment("The radius in which the feral flare should try to place lights")
                .translation("torchmaster.config.feralFlareRadius.description")
                .defineInRange("feralFlareRadius", 16, 1, 127);

            feralFlareTickRate = builder
                .comment("Controls how often the flare should try to place lights. 1 means every tick, 10 every 10th tick, etc")
                .translation("torchmaster.config.feralFlareTickRate.description")
                .defineInRange("feralFlareTickRate", 5, 1, Integer.MAX_VALUE);

            feralFlareMinLightLevel = builder
                .comment("The target minimum light level to place lights for")
                .translation("torchmaster.config.feralFlareMinLightLevel.description")
                .defineInRange("feralFlareMinLightLevel", 10, 0, 15);

            feralFlareLanternLightCountHardcap = builder
                .comment(
                    "The maximum amount of invisble lights a feral flare lantern can place. Set to 0 to disable light placement.",
                    "Warning: Setting this value too high in conjunction with the feralFlareMinLightLevel and Radius can lead to world corruption!",
                    "(Badly compressed packet error)")
                .translation("torchmaster.config.feralFlareLanternLightCountHardcap.description")
                .defineInRange("feralFlareLanternLightCountHardcap", 255, 0, Short.MAX_VALUE);

            frozenPearlDurability = builder
                .comment("Durability of the frozen pearl. Each removed light will remove one charge from the pearl. Set to 0 to disable durability")
                .translation("torchmaster.config.frozenPearlDurability.description")
                .defineInRange("frozenPearlDurability", 1024, 0, Short.MAX_VALUE);

            logSpawnChecks = builder
                .comment("Print entity spawn checks to the debug log")
                .translation("torchmaster.config.logSpawnChecks.description")
                .define("logSpawnChecks", false);

            builder.pop();
        }
    }


    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        Torchmaster.Log.debug(FORGEMOD, "Loaded torchmaster config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {
        Torchmaster.Log.fatal(CORE, "torchmaster config just got changed on the file system!");
    }
}
