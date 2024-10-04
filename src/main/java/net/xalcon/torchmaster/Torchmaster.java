package net.xalcon.torchmaster;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
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
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.xalcon.torchmaster.common.EntityFilterRegistry;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.ModItems;
import net.xalcon.torchmaster.common.commands.CommandTorchmaster;
import net.xalcon.torchmaster.common.network.ModMessageHandler;
import net.xalcon.torchmaster.compat.VanillaCompat;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Torchmaster.MODID)
public class Torchmaster
{
    public static final String MODID = "torchmaster";
    public static final Logger Log = LogUtils.getLogger();

    public static MinecraftServer server;

    public static final EntityFilterRegistry MegaTorchFilterRegistry = new EntityFilterRegistry();
    public static final EntityFilterRegistry DreadLampFilterRegistry = new EntityFilterRegistry();

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> CreativeTab = CREATIVE_MODE_TABS.register("creative_tab", () -> CreativeModeTab.builder()
            .icon(() -> ModBlocks.itemMegaTorch.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(new ItemStack(ModBlocks.itemMegaTorch.get()));
                output.accept(new ItemStack(ModBlocks.itemDreadLamp.get()));
                output.accept(new ItemStack(ModBlocks.itemFeralFlareLantern.get()));
                output.accept(new ItemStack(ModItems.itemFrozenPearl.get()));
            })
            .title(Component.literal("Torchmaster"))
            .build()
    );

    public Torchmaster() {
        ModBlocks.init();
        ModItems.init();
        ModMessageHandler.initialize();

        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(EventPriority.LOWEST, this::postInit);

        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TorchmasterConfig.spec, "torchmaster.toml");
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
}
