package net.xalcon.torchmaster.common.tiles;

import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.Constants;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFeralFlareLantern extends TileEntity implements ITickable
{
    private int ticks;
    private List<BlockPos> childLights = new ArrayList<>();

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.pos, child));

        nbt.setTag("lights", new NBTTagIntArray(childLightsEncoded));
        nbt.setInteger("ticks", this.ticks);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.childLights.clear();
        if(nbt.getTagId("lights") == Constants.NBT.TAG_INT_ARRAY)
        {
            BlockPos origin = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"),nbt.getInteger("z"));
            int[] lightsEncoded = ((NBTTagIntArray) nbt.getTag("lights")).getIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInteger("ticks");
        super.readFromNBT(nbt);
    }

    @Override
    public void update()
    {
        if(this.world.isRemote || ++this.ticks % TorchmasterConfig.feralFlareTickRate != 0) return;
        if(ticks > 1_000_000) ticks = 0;

        int radius = TorchmasterConfig.feralFlareRadius;
        int diameter = radius * 2;

        int x = (radius - this.world.rand.nextInt(diameter)) + this.pos.getX();
        int y = (radius - this.world.rand.nextInt(diameter)) + this.pos.getY();
        int z = (radius - this.world.rand.nextInt(diameter)) + this.pos.getZ();

        // limit height - lower bounds
        if (y < 3) y = 3;

        // limit height - upper bounds
        BlockPos targetPos = new BlockPos(x, y, z);
        BlockPos precipitationHeight = this.world.getPrecipitationHeight(targetPos);
        if (targetPos.getY() > precipitationHeight.getY() + 4)
            targetPos = precipitationHeight.up(4);

        if (this.world.isBlockLoaded(targetPos) &&
                this.world.isAirBlock(targetPos) &&
                this.world.getBlockState(targetPos).getBlock() != ModBlocks.getInvisibleLight() &&
                this.world.getLightFor(EnumSkyBlock.BLOCK, targetPos) < TorchmasterConfig.feralFlareMinLightLevel)
        {
            this.world.setBlockState(targetPos, ModBlocks.getInvisibleLight().getDefaultState(), 3);
            this.childLights.add(targetPos);
            this.markDirty();
        }
    }

    public void removeChildLights()
    {
        if(this.world.isRemote) return;
        for(BlockPos pos : this.childLights)
        {
            if (this.world.getBlockState(pos).getBlock() == ModBlocks.getInvisibleLight())
            {
                this.world.setBlockState(pos, ModBlocks.getInvisibleLight().getDecayState(), 2);
            }
        }
        this.childLights.clear();
    }

    private static int encodePosition(BlockPos origin, BlockPos target)
    {
        int x = target.getX() - origin.getX();
        int y = target.getY() - origin.getY();
        int z = target.getZ() - origin.getZ();
        return ((x & 0xFF) << 16) + ((y & 0xFF) << 8) + (z & 0xFF);
    }

    private static BlockPos decodePosition(BlockPos origin, int pos)
    {
        int x = (byte)((pos >> 16) & 0xFF);
        int y = (byte)((pos >> 8) & 0xFF);
        int z = (byte)(pos & 0xFF);
        return origin.add(x, y, z);
    }
}
