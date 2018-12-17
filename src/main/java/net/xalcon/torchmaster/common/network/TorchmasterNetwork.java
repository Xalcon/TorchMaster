package net.xalcon.torchmaster.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.xalcon.torchmaster.TorchMasterMod;

public class TorchmasterNetwork
{
    private static SimpleNetworkWrapper network;

    public static void initNetwork()
    {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(TorchMasterMod.MODID);
        network.registerMessage(PacketSetFeralLanternLoS.Handler, PacketSetFeralLanternLoS.class, 0, Side.SERVER);
    }

    public static SimpleNetworkWrapper getNetwork() { return network; }
}
