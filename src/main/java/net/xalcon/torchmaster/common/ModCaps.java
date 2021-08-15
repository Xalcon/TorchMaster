package net.xalcon.torchmaster.common;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.xalcon.torchmaster.common.logic.entityblocking.ITEBLightRegistry;

public class ModCaps
{
    @CapabilityInject(ITEBLightRegistry.class)
    public static Capability<ITEBLightRegistry> TEB_REGISTRY = null;

    public static void registerModCaps()
    {
        CapabilityManager.INSTANCE.register(ITEBLightRegistry.class);
    }
}
