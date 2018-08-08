package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.xalcon.torchmaster.common.*;
import net.xalcon.torchmaster.compat.EntityFilterRegisterEvent;
import net.xalcon.torchmaster.compat.RegistryBackwardsCompat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = TorchMasterMod.MODID,
        version = TorchMasterMod.VERSION,
        dependencies = "required-after:forge@[14.21.1.2394,)",
        certificateFingerprint = "@CERT_FINGERPRINT@",
		acceptedMinecraftVersions = "[1.12, 1.13)"
)
public class TorchMasterMod
{
    public static final Logger Log = LogManager.getLogger(TorchMasterMod.MODID);

    public static final String MODID = "torchmaster";
    public static final String VERSION = "@VERSION@";

	private EventHandlerServer eventHandlerServer;
	private ModGuiHandler guiHandler;

	public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
	public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    @Mod.Instance
    public static TorchMasterMod instance;

    @SidedProxy(clientSide = "net.xalcon.torchmaster.client.ClientProxy", serverSide = "net.xalcon.torchmaster.common.CommonProxy")
    public static CommonProxy Proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register((this.eventHandlerServer = new EventHandlerServer()));
        MinecraftForge.EVENT_BUS.register(new RegistryBackwardsCompat());

        ModCaps.registerModCaps();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.guiHandler = new ModGuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this.guiHandler);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.MegaTorch(MegaTorchFilterRegistry));
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.DreadLamp(DreadLampFilterRegistry));
    }
}
