package net.xalcon.torchmaster.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;

public class FabricNetworkHelper implements INetworkHelper
{
    @Override
    public void registerPayloads()
    {
        PayloadTypeRegistry.playC2S().register(
                ToggleMegaTorchOverlayPayload.TYPE,
                ToggleMegaTorchOverlayPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
                ToggleMegaTorchOverlayPayload.TYPE,
                (payload, context) -> context.player().server.execute(
                        () -> ToggleOverlayHandler.handle(payload, context.player())));
    }

    @Override
    public void sendToggleOverlayToServer(BlockPos pos)
    {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        FabricClientNetworkSender.send(pos);
    }
}
