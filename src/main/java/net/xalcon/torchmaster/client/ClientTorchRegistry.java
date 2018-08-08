package net.xalcon.torchmaster.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;

import java.util.ArrayList;

public class ClientTorchRegistry
{
    private ArrayList<BlockPos> torches = new ArrayList<>();

    public void addTorch(BlockPos pos)
    {
        if(!torches.contains(pos))
        {
            TorchMasterMod.Log.info("Cached torch at position {}", pos);
            torches.add(pos);
        }
    }

    public void removeTorch(BlockPos pos)
    {
        TorchMasterMod.Log.info("Removed torch at position {}", pos);
        torches.remove(pos);
    }

    private int checkIndex = 0;

    public void onClientTick(World world)
    {
        if(torches.size() == 0) return;

        BlockPos pos = torches.get(checkIndex);
        if(world.isBlockLoaded(pos))
        {
            IBlockState state = world.getBlockState(pos);
            if(state.getBlock() != ModBlocks.getMegaTorch())
            {
                this.removeTorch(pos);
            }
        }
        checkIndex = (checkIndex + 1) % torches.size();
    }

    public boolean isPositionInRange(double posX, double posY, double posZ)
    {
        int torchRange = TorchmasterConfig.MegaTorchRange;
        int torchRangeSq = torchRange * torchRange;

        for(BlockPos torch : torches)
        {
            double dx = torch.getX() + 0.5 - posX;
            double dy = Math.abs(torch.getY() + 0.5 - posY);
            double dz = torch.getZ() + 0.5 - posZ;
            if((dx * dx + dz * dz) <= (torchRangeSq) && dy <= torchRange)
                return true;
        }
        return false;
    }
}
