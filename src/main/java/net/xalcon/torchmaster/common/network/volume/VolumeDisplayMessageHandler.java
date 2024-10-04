package net.xalcon.torchmaster.common.network.volume;

import net.minecraftforge.network.NetworkEvent;
import net.xalcon.torchmaster.client.EntityBlockingVolumeRenderer;

import java.util.function.Supplier;

public class VolumeDisplayMessageHandler
{
    public static void handle(VolumeDisplayMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        if(msg.showVolume())
        {
            EntityBlockingVolumeRenderer.showVolumeAt(msg.getPosition(), msg.getRange(), msg.getColor());
        }
        else
        {
            EntityBlockingVolumeRenderer.removeVolumeAt(msg.getPosition());
        }

        if(msg.showLocation())
        {
            EntityBlockingVolumeRenderer.showLocationAt(msg.getPosition(), msg.getColor());
        }
        else
        {
            EntityBlockingVolumeRenderer.removeLocationAt(msg.getPosition());
        }
    }
}
