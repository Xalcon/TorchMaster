package net.xalcon.torchmaster.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.xalcon.torchmaster.platform.Services;

@Environment(EnvType.CLIENT)
public final class FabricRangeOverlayHooks
{
    private FabricRangeOverlayHooks() {}

    public static void register()
    {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(ctx ->
        {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            int radius = Services.PLATFORM.getConfig().getMegaTorchRadius();
            MegaTorchOverlayRenderer.render(
                    ctx.matrixStack(),
                    ctx.camera(),
                    ctx.frustum(),
                    level,
                    radius);
        });

        ClientChunkEvents.CHUNK_LOAD.register((level, chunk) ->
                OverlayChunkScanner.scan(chunk, level.dimension()));

        ClientChunkEvents.CHUNK_UNLOAD.register((level, chunk) ->
                OverlayCache.clearChunk(level.dimension(), chunk.getPos()));
    }
}
