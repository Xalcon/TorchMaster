package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.xalcon.torchasm.TorchCorePlugin;
import net.xalcon.torchmaster.common.*;
import net.xalcon.torchmaster.compat.EntityFilterRegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = TorchMasterMod.MODID,
        version = TorchMasterMod.VERSION,
        guiFactory = "net.xalcon.torchmaster.client.gui.config.TorchMasterGuiFactory",
        dependencies = "required-after:forge@[14.21.0.2338,)",
        canBeDeactivated = true
)
public class TorchMasterMod
{
    /**
     * This field is set by a patch in the WorldServer.tick() method
     * The logic of this field is inverted so it can be used even if the patch wasnt applied successfully
     */
    public static boolean isNotInWorldTick = true;
    public static final Logger Log = LogManager.getLogger(TorchMasterMod.MODID);

    public static final String MODID = "torchmaster";
    public static final String VERSION = "@VERSION@";
    public static ConfigHandler ConfigHandler;

	private EventHandlerServer eventHandlerServer;
	private ModGuiHandler guiHandler;

	public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
	public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    @Mod.Instance
    public static TorchMasterMod instance;

    @SidedProxy(clientSide = "net.xalcon.torchmaster.client.ClientProxy", serverSide = "net.xalcon.torchmaster.common.CommonProxy")
    public static CommonProxy Proxy;

    @EventHandler
    public void onDisable(FMLModDisabledEvent event)
    {
        System.out.println(event.description());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler = new ConfigHandler(event.getSuggestedConfigurationFile());

        MinecraftForge.EVENT_BUS.register((this.eventHandlerServer = new EventHandlerServer()));
        MinecraftForge.EVENT_BUS.register(ConfigHandler);

        if(!TorchCorePlugin.isTickHookInstalled)
        {
            Log.error("8888888888 8888888b.  8888888b.   .d88888b.  8888888b.  ");
            Log.error("888        888   Y88b 888   Y88b d88P\" \"Y88b 888   Y88b ");
            Log.error("888        888    888 888    888 888     888 888    888 ");
            Log.error("8888888    888   d88P 888   d88P 888     888 888   d88P ");
            Log.error("888        8888888P\"  8888888P\"  888     888 8888888P\"  ");
            Log.error("888        888 T88b   888 T88b   888     888 888 T88b   ");
            Log.error("888        888  T88b  888  T88b  Y88b. .d88P 888  T88b  ");
            Log.error("8888888888 888   T88b 888   T88b  \"Y88888P\"  888   T88b ");
            Log.error("Torchmaster was not able to install the worldserver tick hook! The MegaTorch will not be able to allow spawns from mobspawners!");
        }
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
