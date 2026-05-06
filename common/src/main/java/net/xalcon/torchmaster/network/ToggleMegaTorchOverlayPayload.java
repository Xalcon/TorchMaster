package net.xalcon.torchmaster.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.xalcon.torchmaster.Constants;

public record ToggleMegaTorchOverlayPayload(BlockPos pos) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ToggleMegaTorchOverlayPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "toggle_overlay"));

    public static final StreamCodec<ByteBuf, ToggleMegaTorchOverlayPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    ToggleMegaTorchOverlayPayload::pos,
                    ToggleMegaTorchOverlayPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
