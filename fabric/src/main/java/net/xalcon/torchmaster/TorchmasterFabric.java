package net.xalcon.torchmaster;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.renderer.RenderType;

public class TorchmasterFabric implements ModInitializer {
    public static final net.xalcon.torchmaster.TorchmasterConfig CONFIG = net.xalcon.torchmaster.TorchmasterConfig.createAndLoad();

    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Torchmaster.LOG.info("Hello Fabric world!");
        Torchmaster.init();

        ServerWorldEvents.LOAD.register((server, world) ->
        {
            Torchmaster.onWorldLoaded();
        });
        // BlockRenderLayerMap.INSTANCE.putBlock(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
    }


}
