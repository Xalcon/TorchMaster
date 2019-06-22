package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.logic.TorchDistanceLogics;

import java.util.HashMap;

public class ClientTorchRegistries
{
    private static HashMap<Integer, ClientTorchRegistry> megaTorchRegistries = new HashMap<>();

    public static ClientTorchRegistry getRegistryForDimension(int dimensionId)
    {
        return megaTorchRegistries.getOrDefault(dimensionId, new ClientTorchRegistry(TorchDistanceLogics.Cubic));
    }

    public static void clearAll()
    {
        Torchmaster.Log.info("Clearing cached mega torches");
        megaTorchRegistries.clear();
    }
}
