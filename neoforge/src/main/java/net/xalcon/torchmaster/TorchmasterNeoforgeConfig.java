package net.xalcon.torchmaster;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForgeConfig;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TorchmasterNeoforgeConfig
{
    public static final ModConfigSpec spec;
    public static final General GENERAL;

    static
    {
        final Pair<General, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(General::new);
        spec = specPair.getRight();
        GENERAL = specPair.getLeft();
    }

    public static class General
    {
        public final ModConfigSpec.ConfigValue<Boolean> beginnerTooltips;
        public final ModConfigSpec.ConfigValue<Boolean> blockOnlyNaturalSpawns;
        public final ModConfigSpec.ConfigValue<Boolean> lycanitesMobsBlockAll;
        public final ModConfigSpec.ConfigValue<Boolean> blockVillageSieges;
        public final ModConfigSpec.ConfigValue<Integer> megaTorchRadius;
        public final ModConfigSpec.ConfigValue<Integer> dreadLampRadius;
        public final ModConfigSpec.ConfigValue<List<? extends String>> megaTorchEntityBlockListOverrides;
        public final ModConfigSpec.ConfigValue<List<? extends String>> dreadLampEntityBlockListOverrides;

        public final ModConfigSpec.ConfigValue<Integer> feralFlareRadius;
        public final ModConfigSpec.ConfigValue<Integer> feralFlareTickRate;
        public final ModConfigSpec.ConfigValue<Integer> feralFlareMinLightLevel;
        public final ModConfigSpec.ConfigValue<Integer> feralFlareLanternLightCountHardcap;
        public final ModConfigSpec.ConfigValue<Integer> frozenPearlDurability;
        public final ModConfigSpec.ConfigValue<Boolean> aggressiveSpawnChecks;

        public final ModConfigSpec.ConfigValue<Boolean> logSpawnChecks;

        private General(ModConfigSpec.Builder builder)
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

            blockVillageSieges = builder
                    .comment("If this setting is enabled, the mega torch will block village sieges from zombies")
                    .translation("torchmaster.config.villagesiege.description")
                    .define("blockVillageSieges", true);

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
                            "      The whole also needs to be enclosed by [ and ] respectively",
                            "Block zombies: [\"+minecraft:zombie\"]",
                            "Allow creepers: [\"-minecraft:creeper\"],",
                            "Multiple Entries: [\"+minecraft:pig\", \"-gaia:dryad\"]")
                    .translation("torchmaster.config.megaTorch.blockListOverrides.description")
                    .defineListAllowEmpty("megaTorchEntityBlockListOverrides", new ArrayList<>(), String::new, EntityFilterList::IsValidFilterString);

            dreadLampEntityBlockListOverrides = builder
                    .comment(
                            "Same as the mega torch block list override, just for the dread lamp",
                            "Block squid: [\"+minecraft:squid\"]",
                            "Allow pigs: [\"-minecraft:pig\"]")
                    .translation("torchmaster.config.dreadLamp.blockListOverrides.description")
                    .defineListAllowEmpty("dreadLampEntityBlockListOverrides", new ArrayList<>(), String::new, EntityFilterList::IsValidFilterString);

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

            aggressiveSpawnChecks = builder
                    .comment("Configures the spawn check to be more aggressive, effectivly overriding the CheckSpawn results of other mods")
                    .translation("torchmaster.config.aggressiveSpawnChecks.description")
                    .define("aggressiveSpawnChecks", false);

            builder.pop();
        }
    }

    public final static ITorchmasterConfig WRAPPED_CONFIG = new ITorchmasterConfig()
    {
        @Override
        public int getFeralFlareTickRate()
        {
            return GENERAL.feralFlareTickRate.get();
        }

        @Override
        public int getFeralFlareLanternLightCountHardcap()
        {
            return GENERAL.feralFlareLanternLightCountHardcap.get();
        }

        @Override
        public int getFeralFlareRadius()
        {
            return GENERAL.feralFlareRadius.get();
        }

        @Override
        public int getFeralFlareMinLightLevel()
        {
            return GENERAL.feralFlareMinLightLevel.get();
        }

        @Override
        public int getDreadLampRadius()
        {
            return GENERAL.dreadLampRadius.get();
        }

        @Override
        public int getMegaTorchRadius()
        {
            return GENERAL.megaTorchRadius.get();
        }

        @Override
        public boolean getAggressiveSpawnChecks()
        {
            return GENERAL.aggressiveSpawnChecks.get();
        }

        @Override
        public boolean getBlockOnlyNaturalSpawns()
        {
            return GENERAL.blockOnlyNaturalSpawns.get();
        }

        @Override
        public boolean getBlockVillageSieges()
        {
            return GENERAL.blockVillageSieges.get();
        }

        @Override
        public List<String> getMegaTorchEntityBlockListOverrides()
        {
            return new ArrayList<>(GENERAL.megaTorchEntityBlockListOverrides.get());
        }

        @Override
        public List<String> getDreadLampEntityBlockListOverrides()
        {
            return new ArrayList<>(GENERAL.dreadLampEntityBlockListOverrides.get());
        }
    };
}
