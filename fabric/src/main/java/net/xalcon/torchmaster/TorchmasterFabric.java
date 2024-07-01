package net.xalcon.torchmaster;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class TorchmasterFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Torchmaster.LOG.info("Hello Fabric world!");
        Torchmaster.init();

        // BlockRenderLayerMap.INSTANCE.putBlock(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
    }


}
