package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.xalcon.torchmaster.client.ClientProxy;
import net.xalcon.torchmaster.common.*;
import net.xalcon.torchmaster.common.network.TorchmasterNetwork;
import net.xalcon.torchmaster.compat.EntityFilterRegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*@Mod(
        modid = TorchMasterMod.MODID,
        version = TorchMasterMod.VERSION,
        dependencies = "required-after:forge@[14.21.1.2394,)",
        certificateFingerprint = "@CERT_FINGERPRINT@",
		acceptedMinecraftVersions = "[1.12, 1.13)"
)*/
@Mod(TorchMasterMod.MODID)
public class TorchMasterMod
{
    public static final Logger Log = LogManager.getLogger(TorchMasterMod.MODID);

    public static final String MODID = "torchmaster";
    public static final String VERSION = "@VERSION@";

	private EventHandlerServer eventHandlerServer;
	private ModGuiHandler guiHandler;

	public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
	public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    public static TorchMasterMod instance;

    public static CommonProxy Proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public TorchMasterMod()
    {
        instance = this;
        FMLModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLModLoadingContext.get().getModEventBus().addListener(this::postInit);
    }

    private void preInit(final FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register((this.eventHandlerServer = new EventHandlerServer()));
        ModCaps.registerModCaps();
        TorchmasterNetwork.initNetwork();
    }

    private void init(FMLInitializationEvent event)
    {
        // TODO: GUI Registration
        //NetworkRegistry.INSTANCE.registerGuiHandler(this, this.guiHandler = new ModGuiHandler());
    }

    private void postInit(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.MegaTorch(MegaTorchFilterRegistry));
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.DreadLamp(DreadLampFilterRegistry));
    }
}
