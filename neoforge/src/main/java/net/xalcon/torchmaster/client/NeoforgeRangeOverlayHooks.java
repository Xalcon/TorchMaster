package net.xalcon.torchmaster.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.platform.Services;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class NeoforgeRangeOverlayHooks
{
    private NeoforgeRangeOverlayHooks() {}

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        int radius = Services.PLATFORM.getConfig().getMegaTorchRadius();
        MegaTorchOverlayRenderer.render(
                event.getPoseStack(),
                event.getCamera(),
                event.getFrustum(),
                level,
                radius);
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event)
    {
        if (!event.getLevel().isClientSide()) return;
        if (!(event.getChunk() instanceof LevelChunk lc)) return;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        OverlayChunkScanner.scan(lc, level.dimension());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event)
    {
        if (!event.getLevel().isClientSide()) return;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        OverlayCache.clearChunk(level.dimension(), event.getChunk().getPos());
    }
}
