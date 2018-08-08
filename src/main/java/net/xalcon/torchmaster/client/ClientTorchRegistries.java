package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.TorchMasterMod;

import java.util.HashMap;

public class ClientTorchRegistries
{
    private static HashMap<Integer, ClientTorchRegistry> megaTorchRegistries = new HashMap<>();

    public static ClientTorchRegistry getRegistryForDimension(int dimensionId)
    {
        return megaTorchRegistries.getOrDefault(dimensionId, new ClientTorchRegistry());
    }

    public static void clearAll()
    {
        TorchMasterMod.Log.info("Clearing cached mega torches");
        megaTorchRegistries.clear();
    }
}
