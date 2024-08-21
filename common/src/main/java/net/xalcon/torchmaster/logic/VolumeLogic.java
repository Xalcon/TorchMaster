package net.xalcon.torchmaster.logic;

import net.minecraft.core.BlockPos;

public interface VolumeLogic
{
    boolean isPositionInRange(double posX, double posY, double posZ, BlockPos torchPos, int range);

    VolumeLogic Cubic = (posX, posY, posZ, torch, range) ->
    {
        // a range of 0 is effectively a 1x1x1 block volume, the torch itself.
        double minX = torch.getX() - range;
        double minY = torch.getY() - range;
        double minZ = torch.getZ() - range;
        double maxX = torch.getX() + range + 1;
        double maxY = torch.getY() + range + 1;
        double maxZ = torch.getZ() + range + 1;
        return minX <= posX && maxX >= posX &&
                minY <= posY && maxY >= posY &&
                minZ <= posZ && maxZ >= posZ;
    };
}
