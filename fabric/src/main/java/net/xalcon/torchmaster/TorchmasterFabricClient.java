package net.xalcon.torchmaster;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.xalcon.torchmaster.client.VolumeRendererOverlay;
import net.xalcon.torchmaster.client.gui.FeralFlareLanternScreen;
import net.xalcon.torchmaster.platform.Services;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
        MenuScreens.register(ModRegistry.menuFeralFlareLantern.get(), FeralFlareLanternScreen::new);

        WorldRenderEvents.AFTER_TRANSLUCENT.register(ctx ->
        {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            VolumeRendererOverlay.onRenderLevel(level.dimension(), ctx.camera());
        });

        ClientPlayConnectionEvents.DISCONNECT.register((phase, listener) -> {
            VolumeRendererOverlay.clearAll();
        });
    }
}