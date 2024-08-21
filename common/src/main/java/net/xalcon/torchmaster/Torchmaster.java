package net.xalcon.torchmaster;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.xalcon.torchmaster.compat.VanillaCompat;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.logic.entityblocking.FilteredLightManager;
import net.xalcon.torchmaster.logic.entityblocking.IBlockingLightManager;
import net.xalcon.torchmaster.platform.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class Torchmaster
{
    public static final EntityFilterList MegaTorchFilterRegistry = new EntityFilterList(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity_filter/mega_torch"));
    public static final EntityFilterList DreadLampFilterRegistry = new EntityFilterList(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity_filter/dread_lamp"));

    public static final Logger LOG = LoggerFactory.getLogger(Constants.MOD_NAME);

    public static ITorchmasterConfig getConfig()
    {
        return Services.PLATFORM.getConfig();
    }

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        ModRegistry.initialize();

        if(Torchmaster.getConfig().shouldLogSpawnChecks())
        {
            LogHax.enableDebugLogging(Torchmaster.LOG);
        }
    }

    public static Optional<IBlockingLightManager> getRegistryForLevel(Level level)
    {
        if(level instanceof ServerLevel serverLevel)
        {
            var dimensionIdentifier = level.dimension().location().toDebugFileName();
            return Optional.of(serverLevel.getDataStorage().computeIfAbsent(FilteredLightManager.Factory, "torchmaster_lights_" + dimensionIdentifier));
        }
        return Optional.empty();
    }

    public static void onWorldLoaded()
    {
        DreadLampFilterRegistry.clear();
        MegaTorchFilterRegistry.clear();

        VanillaCompat.registerDreadLampEntities(DreadLampFilterRegistry);
        VanillaCompat.registerTorchEntities(MegaTorchFilterRegistry);

        DreadLampFilterRegistry.applyListOverrides(Torchmaster.getConfig().getDreadLampEntityBlockListOverrides());
        MegaTorchFilterRegistry.applyListOverrides(Torchmaster.getConfig().getMegaTorchEntityBlockListOverrides());
    }
}