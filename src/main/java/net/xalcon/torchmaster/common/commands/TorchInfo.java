package net.xalcon.torchmaster.common.commands;

import net.minecraft.util.math.BlockPos;

public class TorchInfo
{
    private String name;
    private BlockPos pos;

    public TorchInfo(String name, BlockPos pos)
    {
        this.name = name;
        this.pos = pos;
    }

    public String getName()
    {
        return name;
    }

    public BlockPos getPos()
    {
        return pos;
    }
}
