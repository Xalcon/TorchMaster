package net.xalcon.torchmaster.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.Torchmaster;

@Mod.EventBusSubscriber(modid = Torchmaster.MODID, value = Dist.CLIENT)
public class PlayerEventsHandler
{
    @SubscribeEvent
    public static void onPlayerJoinWorldEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityBlockingVolumeRenderer.clearAll();
    }

    @SubscribeEvent
    public static void onPlayerJoinWorldEvent(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        EntityBlockingVolumeRenderer.clearAll();
    }
}
