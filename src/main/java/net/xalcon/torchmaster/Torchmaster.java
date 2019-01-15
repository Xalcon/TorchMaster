package net.xalcon.torchmaster;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.xalcon.torchmaster.common.*;
import net.xalcon.torchmaster.common.init.ModCaps;
import net.xalcon.torchmaster.common.network.TorchmasterNetwork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*@Mod(
        modid = TorchMasterMod.MODID,
        version = TorchMasterMod.VERSION,
        dependencies = "required-after:forge@[14.21.1.2394,)",
        certificateFingerprint = "@CERT_FINGERPRINT@",
		acceptedMinecraftVersions = "[1.12, 1.13)"
)*/
@Mod(Torchmaster.MODID)
public class Torchmaster
{
    public static final Logger Log = LogManager.getLogger(Torchmaster.MODID);

    public static final String MODID = "torchmaster";
    public static final String VERSION = "@VERSION@";

	private EventHandlerServer eventHandlerServer;
	private ModGuiHandler guiHandler;

	public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
	public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    public static Torchmaster instance;

    public Torchmaster()
    {
        instance = this;
        FMLModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLModLoadingContext.get().getModEventBus().addListener(this::onIntermodEnqueue);
        FMLModLoadingContext.get().getModEventBus().addListener(this::onIntermodProcess);
        FMLModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
        FMLModLoadingContext.get().getModEventBus().addListener(this::onModIdMapping);

        ModCaps.registerModCaps();
        //TorchmasterNetwork.initNetwork();
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        Log.info("FMLCommonSetupEvent");
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        Log.info("FMLClientSetupEvent");
    }

    private void onIntermodEnqueue(InterModEnqueueEvent event)
    {
        Log.info("InterModEnqueueEvent");
    }

    private void onIntermodProcess(InterModProcessEvent event)
    {
        Log.info("InterModProcessEvent");
    }

    private void onLoadComplete(FMLLoadCompleteEvent event)
    {
        Log.info("FMLLoadCompleteEvent");
    }

    private void onModIdMapping(FMLModIdMappingEvent event)
    {
        Log.info("FMLModIdMappingEvent");
    }

    /*private void postInit(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.MegaTorch(MegaTorchFilterRegistry));
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.DreadLamp(DreadLampFilterRegistry));
    }*/
}
