package net.xalcon.torchmaster;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

        for (var entry : GENERAL.megatorchBurnoutFuelTypes.get())
        {
            var tokens = entry.split("@");
            var item = new ResourceLocation(tokens[0]);
            var fuel = Integer.parseInt(tokens[1]);
            GENERAL.fuelMap.put(item, fuel);
        }
    }

    public static class General
    {
        public final ForgeConfigSpec.ConfigValue<Boolean> beginnerTooltips;
        public final ForgeConfigSpec.ConfigValue<Boolean> blockOnlyNaturalSpawns;
        public final ForgeConfigSpec.ConfigValue<Boolean> lycanitesMobsBlockAll;
        public final ForgeConfigSpec.ConfigValue<Boolean> blockVillageSieges;
        public final ForgeConfigSpec.ConfigValue<Integer> megaTorchRadius;
        public final ForgeConfigSpec.ConfigValue<Integer> dreadLampRadius;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> megaTorchEntityBlockListOverrides;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> dreadLampEntityBlockListOverrides;

        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareRadius;
        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareTickRate;
        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareMinLightLevel;
        public final ForgeConfigSpec.ConfigValue<Integer> feralFlareLanternLightCountHardcap;
        public final ForgeConfigSpec.ConfigValue<Integer> frozenPearlDurability;
        public final ForgeConfigSpec.ConfigValue<Boolean> aggressiveSpawnChecks;
        public final ForgeConfigSpec.ConfigValue<Boolean> logSpawnChecks;

        public final ForgeConfigSpec.ConfigValue<Boolean> enableMegatorchBurnout;
        public final ForgeConfigSpec.ConfigValue<Integer> megatorchBurnoutDataRefreshRate;
        public final ForgeConfigSpec.ConfigValue<Integer> megatorchBurnoutWarningThreshold;
        // TODO
        public final ForgeConfigSpec.ConfigValue<Integer> megatorchBurnoutMaxFuel;
        // TODO
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> megatorchBurnoutFuelTypes;

        public final Map<ResourceLocation, Integer> fuelMap = new HashMap<>();

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

            aggressiveSpawnChecks = builder
                .comment("Configures the spawn check to be more aggressive, effectivly overriding the CheckSpawn results of other mods")
                .translation("torchmaster.config.aggressiveSpawnChecks.description")
                .define("aggressiveSpawnChecks", false);

            builder.pop();

            builder.push("Megatorch Burnout");

            enableMegatorchBurnout = builder
                    .comment("Enable Megatorch Burnout. If enabled, a fuel item needs to be used on the torch to light it.")
                    .translation("torchmaster.config.enableMegatorchBurnout")
                    .define("enableMegatorchBurnout", false);

            megatorchBurnoutDataRefreshRate = builder
                    .comment("Defines the time in between updates that are sent to all clients in range in world ticks. 20 ticks = 1 second. Don't change this if you don't know what this is for. Low value may cause lag")
                    .translation("torchmaster.config.megatorchBurnoutDataRefreshRate")
                    .defineInRange("megatorchDataRefreshRate", 5 * 20, 1, 20 * 60);

            megatorchBurnoutWarningThreshold = builder
                    .comment("Amount of fuel left before the torch should indicate low fuel level warnings. Set to 0 to disable.")
                    .translation("torchmaster.config.megatorchBurnoutWarningThreshold")
                    .defineInRange("megatorchBurnoutWarningThreshold", 5 * 60 * 20, 0, Integer.MAX_VALUE);

            megatorchBurnoutMaxFuel = builder
                    .comment("Amount of fuel that can be stored in the torch. Fuel is used at a rate of 1 per tick.")
                    .translation("torchmaster.config.megatorchBurnoutMaxFuel")
                    .defineInRange("megatorchBurnoutMaxFuel", 8 * 60 * 60 * 20, 0, Integer.MAX_VALUE);

            megatorchBurnoutFuelTypes = builder
                    .comment("Map of fuel types. Use the format modname:itemname@fuel. I.e. minecraft:coal@72000 would give 1h worth of fuel when using a piece of coal")
                    .translation("torchmaster.config.megatorchBurnoutFuelTypes")
                    .defineList("megatorchBurnoutFuelTypes", List.of("minecraft:coal@72000", "minecraft:diamond@1728000"), o ->
                    {
                        var tokens = o.toString().split("@");

                        if(tokens.length != 2)
                        {
                            Torchmaster.Log.error("Invalid fuel type definition: '{}'", o);
                            return false;
                        }
                        if(tokens[0].trim().equals(""))
                        {
                            Torchmaster.Log.error("Invalid fuel type definition. Unable to parse item: '{}'", o);
                            return false;
                        }

                        if(tokens[1].trim().equals(""))
                        {
                            Torchmaster.Log.error("Invalid fuel type definition. Unable to parse fuel: '{}'", o);
                            return false;
                        }

                        var itemString = tokens[0];
                        // Can't validate item on startup, items may not be registered yet
                        // if(!ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemString)))
                        // {
                        //     Torchmaster.Log.error("Item not found in item registry: {}", itemString);
                        //     return false;
                        // }

                        var fuelValue = Integer.parseInt(tokens[1]);
                        if(fuelValue < 1)
                        {
                            Torchmaster.Log.error("Invalid fuel type definition. Fuel must be >0: '{}'", o);
                            return false;
                        }

                        return true;
                    });

            builder.pop();
        }
    }
}
