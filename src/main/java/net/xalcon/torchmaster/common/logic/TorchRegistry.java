package net.xalcon.torchmaster.common.logic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchMasterMod;

import java.util.ArrayList;
import java.util.List;

abstract class TorchRegistry implements ITorchRegistry
{
    private final List<BlockPos> torches = new ArrayList<>();

    protected abstract boolean shouldHandleEntityType(Class<? extends Entity> entityClass);
    protected abstract int getTorchRange();
    protected abstract boolean isBlockStateValid(IBlockState state);

    @Override
    public final void register(BlockPos pos)
    {
        if(!torches.contains(pos))
            torches.add(pos);
    }

    @Override
    public final void unregister(BlockPos pos)
    {
        torches.remove(pos);
    }

    @Override
    public final boolean shouldEntityBeBlocked(Entity entity)
    {
        return shouldHandleEntityType(entity.getClass())
                && isPositionInTorchRange(entity.posX, entity.posY, entity.posZ);
    }

    private boolean isPositionInTorchRange(double posX, double posY, double posZ)
    {
        int torchRange = getTorchRange();
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

    private int checkIndex;

    @Override
    public void onGlobalTick(World world)
    {
        if(this.torches.size() == 0) return;

        this.checkIndex = (this.checkIndex + 1) % this.torches.size();
        BlockPos loc = this.torches.get(this.checkIndex);
        if(world == null) return;
        if(world.isBlockLoaded(loc))
        {
            if(!this.isBlockStateValid(world.getBlockState(loc)))
            {
                TorchMasterMod.Log.info("Torch @ " + loc + " is no longer valid, removing from registry");
                unregister(loc);
            }
        }
    }

    @Override
    public NBTTagList serializeNBT()
    {
        NBTTagList tagList = new NBTTagList();
        for(BlockPos loc : this.torches)
            tagList.appendTag(NBTUtil.createPosTag(loc));
        return tagList;
    }

    @Override
    public void deserializeNBT(NBTTagList list)
    {
        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            BlockPos loc = NBTUtil.getPosFromTag(entry);
            this.register(loc);
        }
    }
}
