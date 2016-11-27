package net.xalcon.torchmaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.xalcon.torchmaster.common.*;
import net.xalcon.torchmaster.server.EventHandlerServer;
import net.xalcon.torchmaster.server.TorchRegistry;

@Mod(modid = TorchMasterMod.MODID, version = TorchMasterMod.VERSION)
public class TorchMasterMod
{
    public static final String MODID = "torchmaster";
    public static final String VERSION = "1.0";

    private EventHandlerServer eventHandlerServer;

    @SidedProxy(clientSide = "net.xalcon.torchmaster.client.ClientProxy", serverSide = "net.xalcon.torchmaster.server.ServerProxy")
    public static Proxy Proxy;

    public static ConfigHandler Configuration;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration = new ConfigHandler();
        Configuration.init(event.getSuggestedConfigurationFile());
        TorchRegistry.INSTANCE.setTorchRange(Configuration.MegaTorchRange);

        MinecraftForge.EVENT_BUS.register((this.eventHandlerServer = new EventHandlerServer()));
        ModBlocks.init();
        ModTileEntities.init();
        ModRecipes.init();
    }
}
