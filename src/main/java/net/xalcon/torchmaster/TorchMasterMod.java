package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.xalcon.torchmaster.common.*;
import net.xalcon.torchmaster.server.EventHandlerServer;
import net.xalcon.torchmaster.server.TorchRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = TorchMasterMod.MODID, version = TorchMasterMod.VERSION, guiFactory = "net.xalcon.torchmaster.client.gui.config.TorchMasterGuiFactory")
public class TorchMasterMod
{
    public static final Logger Log = LogManager.getLogger(TorchMasterMod.MODID);

    public static final String MODID = "torchmaster";
    public static final String VERSION = "1.0";
    public static ConfigHandler ConfigHandler;

	private EventHandlerServer eventHandlerServer;
	private ModGuiHandler guiHandler;

    @Mod.Instance
    public static TorchMasterMod instance;

    @SidedProxy(clientSide = "net.xalcon.torchmaster.client.ClientProxy", serverSide = "net.xalcon.torchmaster.server.ServerProxy")
    public static Proxy Proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler = new ConfigHandler(event.getSuggestedConfigurationFile());

        MinecraftForge.EVENT_BUS.register((this.eventHandlerServer = new EventHandlerServer()));
        ModBlocks.init();
        ModTileEntities.init();
        ModRecipes.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.guiHandler = new ModGuiHandler();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this.guiHandler);
    }
}
