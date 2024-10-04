package net.xalcon.torchmaster.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.network.volume.VolumeDisplayMessage;

public class ModMessageHandler
{
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Torchmaster.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void initialize()
    {
        INSTANCE.registerMessage(1, VolumeDisplayMessage.class, VolumeDisplayMessage::encode, VolumeDisplayMessage::decode, VolumeDisplayMessage::dispatch);
    }
}
