package net.xalcon.torchmaster.network;

import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
final class NeoforgeClientNetworkSender
{
    private NeoforgeClientNetworkSender() {}

    static void send(BlockPos pos)
    {
        PacketDistributor.sendToServer(new ToggleMegaTorchOverlayPayload(pos));
    }
}
