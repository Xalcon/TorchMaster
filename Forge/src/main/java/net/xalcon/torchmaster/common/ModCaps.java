package net.xalcon.torchmaster.common;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xalcon.torchmaster.logic.entityblocking.IBlockingLightRegistry;

public class ModCaps
{
    // TODO!
    public static Capability<IBlockingLightRegistry> TEB_REGISTRY = CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent()
    public static void registerModCaps(RegisterCapabilitiesEvent event)
    {
        event.register(IBlockingLightRegistry.class);
    }
}
