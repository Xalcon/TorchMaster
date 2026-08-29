package net.xalcon.torchmaster;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.xalcon.torchmaster.client.gui.FeralFlareLanternScreen;

@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class TorchmasterClientModEventHandler
{
    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event)
    {
        event.register(ModRegistry.menuFeralFlareLantern.get(), FeralFlareLanternScreen::new);
    }
}
