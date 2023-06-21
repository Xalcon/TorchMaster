package net.xalcon.torchmaster;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.xalcon.torchmaster.common.EntityFilterRegistry;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.ModItems;
import net.xalcon.torchmaster.common.commands.CommandTorchmaster;
import net.xalcon.torchmaster.compat.VanillaCompat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Torchmaster.MODID)
public class Torchmaster
{
    public static final String MODID = "torchmaster";
    public static final Logger Log = LogManager.getLogger();

    public static MinecraftServer server;

    public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
    public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> CreativeTab = CREATIVE_MODE_TABS.register("creative_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModBlocks.itemMegaTorch.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(new ItemStack(ModBlocks.itemMegaTorch.get()));
                output.accept(new ItemStack(ModBlocks.itemDreadLamp.get()));
                output.accept(new ItemStack(ModBlocks.itemFeralFlareLantern.get()));
                output.accept(new ItemStack(ModItems.itemFrozenPearl.get()));
            })
            .build()
    );

    public Torchmaster() {
        ModBlocks.init();
        ModItems.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, this::postInit);

        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCreativeModeTabRegisterEvent);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchmasterConfig.spec, "torchmaster.toml");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // ModCaps.registerModCaps();
    }

    private void postInit(final FMLLoadCompleteEvent event)
    {
        VanillaCompat.registerTorchEntities(MegaTorchFilterRegistry);
        VanillaCompat.registerDreadLampEntities(DreadLampFilterRegistry);

        Log.info("Applying mega torch entity block list overrides...");
        MegaTorchFilterRegistry.applyListOverrides(TorchmasterConfig.GENERAL.megaTorchEntityBlockListOverrides.get().toArray(new String[0]));
        Log.info("Applying dread lamp entity block list overrides...");
        DreadLampFilterRegistry.applyListOverrides(TorchmasterConfig.GENERAL.dreadLampEntityBlockListOverrides.get().toArray(new String[0]));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.blockDreadLamp.get(), RenderType.cutout());
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

    /*public void onCreativeModeTabRegisterEvent(CreativeModeTabEvent.Register event)
    {
        CreativeModeTab = event.registerCreativeModeTab(new ResourceLocation(Torchmaster.MODID, "creativetab"), builder ->
        {
           builder.m_257737_(() -> new ItemStack(ModBlocks.itemMegaTorch.get()));

           builder.m_257501_((displayParameters, output) ->
           {
               output.m_246342_(new ItemStack(ModBlocks.itemMegaTorch.get()));
               output.m_246342_(new ItemStack(ModBlocks.itemDreadLamp.get()));
               output.m_246342_(new ItemStack(ModBlocks.itemFeralFlareLantern.get()));
               output.m_246342_(new ItemStack(ModItems.itemFrozenPearl.get()));
           });
        });
    }*/
}
