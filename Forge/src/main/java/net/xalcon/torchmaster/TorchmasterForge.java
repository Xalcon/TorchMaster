package net.xalcon.torchmaster;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xalcon.torchmaster.commands.CommandTorchmaster;
import net.xalcon.torchmaster.compat.VanillaCompat;

@Mod(Constants.MOD_ID)
public class TorchmasterForge
{
    public static MinecraftServer server;

    public TorchmasterForge() {
        Torchmaster.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, this::postInit);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchmasterConfig.spec, "torchmaster.toml");
    }

    private void postInit(final FMLLoadCompleteEvent event)
    {
        VanillaCompat.registerTorchEntities(Torchmaster.MegaTorchFilterRegistry);
        VanillaCompat.registerDreadLampEntities(Torchmaster.DreadLampFilterRegistry);

        Constants.LOG.info("Applying mega torch entity block list overrides...");
        Torchmaster.MegaTorchFilterRegistry.applyListOverrides(TorchmasterConfig.GENERAL.megaTorchEntityBlockListOverrides.get().toArray(new String[0]));
        Constants.LOG.info("Applying dread lamp entity block list overrides...");
        Torchmaster.DreadLampFilterRegistry.applyListOverrides(TorchmasterConfig.GENERAL.dreadLampEntityBlockListOverrides.get().toArray(new String[0]));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.blockDreadLamp.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event)
    {
        CommandTorchmaster.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event)
    {
        server = event.getServer();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppedEvent event)
    {
        server = null;
    }
}