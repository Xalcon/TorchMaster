package net.xalcon.torchmaster.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;

public final class ToggleOverlayHandler
{
    private static final double MAX_REACH_SQR = 64.0D;

    private ToggleOverlayHandler() {}

    public static void handle(ToggleMegaTorchOverlayPayload payload, ServerPlayer player)
    {
        if (player == null) return;
        BlockPos pos = payload.pos();
        if (pos == null) return;

        ServerLevel level = player.serverLevel();
        if (!level.isLoaded(pos)) return;

        if (player.position().distanceToSqr(Vec3.atCenterOf(pos)) > MAX_REACH_SQR)
        {
            Torchmaster.LOG.debug("Rejected overlay toggle from {}: out of reach", player.getGameProfile().getName());
            return;
        }

        BlockState state = level.getBlockState(pos);
        if (!state.is(ModRegistry.blockMegaTorch.get()))
        {
            Torchmaster.LOG.debug("Rejected overlay toggle from {}: block at {} is not a megatorch", player.getGameProfile().getName(), pos);
            return;
        }

        level.setBlock(pos, state.cycle(EntityBlockingLightBlock.OVERLAY_VISIBLE), Block.UPDATE_ALL);
    }
}
