package net.xalcon.torchmaster.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.xalcon.torchmaster.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class NeoforgeNetworkRegistration
{
    private NeoforgeNetworkRegistration() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                ToggleMegaTorchOverlayPayload.TYPE,
                ToggleMegaTorchOverlayPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(
                        () -> ToggleOverlayHandler.handle(payload, (ServerPlayer) context.player())));
    }
}
