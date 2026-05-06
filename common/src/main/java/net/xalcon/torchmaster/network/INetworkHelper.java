package net.xalcon.torchmaster.network;

import net.minecraft.core.BlockPos;

public interface INetworkHelper
{
    void registerPayloads();

    void sendToggleOverlayToServer(BlockPos pos);
}
