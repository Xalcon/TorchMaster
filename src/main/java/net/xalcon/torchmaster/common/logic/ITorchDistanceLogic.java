package net.xalcon.torchmaster.common.logic;

import net.minecraft.util.math.BlockPos;

public interface ITorchDistanceLogic
{
    boolean isPositionInRange(double posX, double posY, double posZ, BlockPos torchPos, int range);
}
