package net.xalcon.torchmaster;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

@Environment(EnvType.CLIENT)
public class TorchmasterFabricClient implements ClientModInitializer {
    public void onInitializeClient()
    {
        BlockRenderLayerMap.INSTANCE.putBlock(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
    }
}