package net.xalcon.torchmaster.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public final class MegaTorchScreenOpener
{
    private MegaTorchScreenOpener() {}

    public static void open(BlockPos pos)
    {
        Minecraft.getInstance().setScreen(new MegaTorchScreen(pos));
    }
}
