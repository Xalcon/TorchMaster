package net.xalcon.torchmaster;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
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
        public final ForgeConfigSpec.ConfigValue<Integer> megaTorchRadius;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> MegaTorchEntityBlockListOverrides;

        private General(ForgeConfigSpec.Builder builder)
        {
            builder.push("General");

            blockOnlyNaturalSpawns = builder
                .comment("By default, mega torches only block natural spawns (i.e. from low light levels). Setting this to false will also block spawns from spawners")
                .translation("torchmaster.config.blockOnlyNaturalSpawns.description")
                .define("blockOnlyNaturalSpawns", true);

            megaTorchRadius = builder
                .comment("The radius of the mega torch in each direction (cube) with the torch at its center")
                .translation("torchmaster.config.megaTorchRadius.description")
                .define("megaTorchRadius", 64);

            MegaTorchEntityBlockListOverrides = builder
                .comment("Bla")
                .translation("torchmaster.config.bla")
                .defineList("megaTorchEntityBlockListOverrides", Arrays.asList("+minecraft:zombie", "+minecraft:pig"),o -> o instanceof String);

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
