package net.xalcon.torchmaster.common.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.xalcon.torchmaster.common.logic.GenericNBTStorage;
import net.xalcon.torchmaster.common.logic.ITorchRegistryContainer;
import net.xalcon.torchmaster.common.logic.TorchRegistryContainer;

public class ModCaps
{
    @CapabilityInject(ITorchRegistryContainer.class)
    public static Capability<ITorchRegistryContainer> TORCH_REGISTRY_CONTAINER = null;

    public static void registerModCaps()
    {
        CapabilityManager.INSTANCE.register(ITorchRegistryContainer.class, new GenericNBTStorage<>(), TorchRegistryContainer::new);
    }
}
