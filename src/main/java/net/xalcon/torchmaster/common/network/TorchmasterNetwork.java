package net.xalcon.torchmaster.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.xalcon.torchmaster.Torchmaster;

public class TorchmasterNetwork
{
    private static SimpleChannel channel;

    public static void initNetwork()
    {
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(Torchmaster.MODID, "channel"), () -> "1.0", s -> true, s -> true);
        PacketSetFeralLanternLoS.Handler.Register(channel, 1);
    }

    public static SimpleChannel getChannel() { return channel; }
}
