package net.xalcon.torchmaster.common.logic;

import net.minecraft.util.math.BlockPos;

public interface IDistanceLogic
{
    boolean isPositionInRange(double posX, double posY, double posZ, BlockPos torchPos, int range);
}
