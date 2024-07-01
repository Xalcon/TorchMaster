package net.xalcon.torchmaster;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.xalcon.torchmaster.logic.EntityBlockingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class Torchmaster
{
    public static final Logger LOG = LoggerFactory.getLogger(Constants.MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        ModRegistry.initialize();

    }

    public static EntityBlockingManager getEntityBlockingManagerForLevel(ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(EntityBlockingManager.Factory, "tmebm_" + level.dimension());
    }
}