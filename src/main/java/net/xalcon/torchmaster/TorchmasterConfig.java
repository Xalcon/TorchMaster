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
        public final ForgeConfigSpec.ConfigValue<Boolean> blockOnlyNaturalSpawns;
        public final ForgeConfigSpec.ConfigValue<Boolean> lycanitesMobsBlockAll;
        public final ForgeConfigSpec.ConfigValue<Integer> megaTorchRadius;
        public final ForgeConfigSpec.ConfigValue<Integer> dreadLampRadius;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> megaTorchEntityBlockListOverrides;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> dreadLampEntityBlockListOverrides;

        private General(ForgeConfigSpec.Builder builder)
        {
            builder.push("General");

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
                .define("megaTorchRadius", 64);

            dreadLampRadius = builder
                .comment("The radius of the dread lamp in each direction (cube) with the torch at its center")
                .translation("torchmaster.config.dreadLamp.description")
                .define("dreadLampRadius", 64);

            megaTorchEntityBlockListOverrides = builder
                .comment(
                    "Use this setting to override the internal lists for entity blocking",
                    "You can use this to block more entities or even allow certain entities to still spawn",
                    "The + prefix will add the entity to the list, effectivly denying its spawns",
                    "The - prefix will remove the entity from the list (if necessary), effectivly allowing its spawns",
                    "Block zombies: +minecraft:zombie",
                    "Allow creepers: -minecraft:creeper")
                .translation("torchmaster.config.megaTorch.blockListOverrides.description")
                .defineList("megaTorchEntityBlockListOverrides", new ArrayList<>(), o -> o instanceof String);

            dreadLampEntityBlockListOverrides = builder
                .comment(
                    "Same as the mega torch block list override, just for the dread lamp",
                    "Block squid: +minecraft:squid",
                    "Allow pigs: -minecraft:pig")
                .translation("torchmaster.config.dreadLamp.blockListOverrides.description")
                .defineList("dreadLampEntityBlockListOverrides", new ArrayList<>(), o -> o instanceof String);

            builder.pop();
        }
    }


    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        Torchmaster.Log.debug(FORGEMOD, "Loaded torchmaster config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
        Torchmaster.Log.fatal(CORE, "torchmaster config just got changed on the file system!");
    }
}
