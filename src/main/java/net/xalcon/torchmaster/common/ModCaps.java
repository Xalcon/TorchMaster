package net.xalcon.torchmaster.common;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xalcon.torchmaster.common.logic.entityblocking.ITEBLightRegistry;

public class ModCaps
{
    public static Capability<ITEBLightRegistry> TEB_REGISTRY = CapabilityManager.get(new CapabilityToken<>(){});
}
