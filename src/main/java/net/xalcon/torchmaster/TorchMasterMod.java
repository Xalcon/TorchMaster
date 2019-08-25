package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.xalcon.torchmaster.common.*;
import net.xalcon.torchmaster.common.commands.CommandTorchmasterEntityDump;
import net.xalcon.torchmaster.common.commands.CommandTorchmasterTorchList;
import net.xalcon.torchmaster.common.network.TorchmasterNetwork;
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

        TorchmasterNetwork.initNetwork();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this.guiHandler = new ModGuiHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.MegaTorch(MegaTorchFilterRegistry));
        MinecraftForge.EVENT_BUS.post(new EntityFilterRegisterEvent.DreadLamp(DreadLampFilterRegistry));

        TorchMasterMod.Log.info("Applying mega torch entity block list overrides...");
        MegaTorchFilterRegistry.applyListOverrides(TorchmasterConfig.MegaTorchEntityBlockListOverrides);
        TorchMasterMod.Log.info("Applying dread lamp entity block list overrides...");
        DreadLampFilterRegistry.applyListOverrides(TorchmasterConfig.DreadLampEntityBlockListOverrides);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandTorchmasterEntityDump());
        event.registerServerCommand(new CommandTorchmasterTorchList());
    }
}
