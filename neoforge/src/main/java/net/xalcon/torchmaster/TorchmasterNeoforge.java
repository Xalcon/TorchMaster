package net.xalcon.torchmaster;


import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLanguageProvider;

@Mod(Constants.MOD_ID)
public class TorchmasterNeoforge
{

    public TorchmasterNeoforge(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        eventBus.addListener(this::doClientStuff);

        // Use NeoForge to bootstrap the Common mod.
        Torchmaster.LOG.info("Hello NeoForge world!");
        Torchmaster.init();

        // ItemBlockRenderTypes.setRenderLayer(ModRegistry.blockDreadLamp.get(), RenderType.CUTOUT);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
    }
}