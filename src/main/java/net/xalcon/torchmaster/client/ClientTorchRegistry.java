package net.xalcon.torchmaster.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.init.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.logic.ITorchDistanceLogic;

import java.util.ArrayList;

public class ClientTorchRegistry
{
    private ArrayList<BlockPos> torches = new ArrayList<>();

    private ITorchDistanceLogic distanceLogic;

    public ClientTorchRegistry(ITorchDistanceLogic distanceLogic)
    {
        this.distanceLogic = distanceLogic;
    }

    public void addTorch(BlockPos pos)
    {
        if(!torches.contains(pos))
        {
            Torchmaster.Log.info("Cached torch at position {}", pos);
            torches.add(pos);
        }
    }

    public void removeTorch(BlockPos pos)
    {
        Torchmaster.Log.info("Removed torch at position {}", pos);
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
            if(state.getBlock() != ModBlocks.MegaTorch)
            {
                this.removeTorch(pos);
            }
        }
        checkIndex = (checkIndex + 1) % torches.size();
    }

    public boolean isPositionInRange(double posX, double posY, double posZ)
    {
        int torchRange = TorchmasterConfig.MegaTorchRange;

        for(BlockPos torch : torches)
        {
            if(distanceLogic.isPositionInRange(posX, posY, posZ, torch, torchRange))
                return true;
        }
        return false;
    }
}
