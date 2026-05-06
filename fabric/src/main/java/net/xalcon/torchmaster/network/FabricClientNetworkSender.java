package net.xalcon.torchmaster.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;

@Environment(EnvType.CLIENT)
final class FabricClientNetworkSender
{
    private FabricClientNetworkSender() {}

    static void send(BlockPos pos)
    {
        ClientPlayNetworking.send(new ToggleMegaTorchOverlayPayload(pos));
    }
}
