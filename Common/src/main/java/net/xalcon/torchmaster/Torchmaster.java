package net.xalcon.torchmaster;

import net.xalcon.torchmaster.events.EventHandler;
import net.xalcon.torchmaster.platform.Services;

public class Torchmaster
{
    public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
    public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    public static final EventHandler EventHandler = new EventHandler();

    // This method serves as an initialization hook for the mod. The vanilla
    // game has no mechanism to load tooltip listeners so this must be
    // invoked from a mod loader specific project like Forge or Fabric.
    public static void init() {

        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.isDevelopmentEnvironment() ? "development" : "production");

        ModRegistry.init();
    }
}