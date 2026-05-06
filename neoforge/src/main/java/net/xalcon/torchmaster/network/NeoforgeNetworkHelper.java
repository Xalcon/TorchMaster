package net.xalcon.torchmaster.network;

import net.minecraft.core.BlockPos;
import net.neoforged.fml.loading.FMLEnvironment;

public class NeoforgeNetworkHelper implements INetworkHelper
{
    @Override
    public void registerPayloads()
    {
        // NeoForge requires registration via RegisterPayloadHandlersEvent — see NeoforgeNetworkRegistration.
    }

    @Override
    public void sendToggleOverlayToServer(BlockPos pos)
    {
        if (FMLEnvironment.dist.isDedicatedServer()) return;
        NeoforgeClientNetworkSender.send(pos);
    }
}
