package net.xalcon.torchmaster;


import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLanguageProvider;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.xalcon.torchmaster.commands.CommandTorchmaster;

@Mod(Constants.MOD_ID)
public class TorchmasterNeoforge
{

    public TorchmasterNeoforge(ModContainer container, IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.
        eventBus.addListener(this::doClientStuff);
        NeoForge.EVENT_BUS.addListener(TorchmasterNeoforge::loadComplete);
        NeoForge.EVENT_BUS.addListener(TorchmasterNeoforge::onRegisterCommands);

        container.registerConfig(ModConfig.Type.COMMON, TorchmasterNeoforgeConfig.spec, "torchmaster.toml");

        // Use NeoForge to bootstrap the Common mod.
        Torchmaster.init();
    }

    private static void loadComplete(LevelEvent.Load event)
    {
        Torchmaster.onWorldLoaded();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // In neoforge, rendertype is configured via the model.json
        // ItemBlockRenderTypes.setRenderLayer(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
    }

    private static void onRegisterCommands(RegisterCommandsEvent event)
    {
        CommandTorchmaster.register(event.getDispatcher());
    }
}