package net.xalcon.torchmaster.common.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceFluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumLightType;
import net.minecraftforge.common.util.Constants;
import net.xalcon.torchmaster.common.init.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.init.ModTileEntities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityFeralFlareLantern extends TileEntity implements ITickable
{
    private int ticks;
    private boolean useLineOfSight;
    private List<BlockPos> childLights = new ArrayList<>();

    public TileEntityFeralFlareLantern()
    {
        super(ModTileEntities.FERAL_FLARE_LANTERN);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, -1, this.write(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        super.onDataPacket(net, pkt);
        this.read(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return write(super.getUpdateTag());
    }

    @Override
    public NBTTagCompound write(NBTTagCompound nbt)
    {
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.pos, child));

        nbt.setTag("lights", new NBTTagIntArray(childLightsEncoded));
        nbt.setInt("ticks", this.ticks);
        nbt.setBoolean("useLoS", this.useLineOfSight);
        return super.write(nbt);
    }

    @Override
    public void read(NBTTagCompound nbt)
    {
        this.childLights.clear();
        if(nbt.getTagId("lights") == Constants.NBT.TAG_INT_ARRAY)
        {
            BlockPos origin = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
            int[] lightsEncoded = ((NBTTagIntArray) nbt.getTag("lights")).getIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.read(nbt);
    }

    @Override
    public void tick()
    {
        if(this.world.isRemote || ++this.ticks % TorchmasterConfig.feralFlareTickRate != 0) return;
        if(ticks > 1_000_000) ticks = 0;

        if(this.childLights.size() > TorchmasterConfig.FeralFlareLanternLightCountHardcap) return;

        int radius = TorchmasterConfig.feralFlareRadius;
        int diameter = radius * 2;

        int x = (radius - this.world.rand.nextInt(diameter)) + this.pos.getX();
        int y = (radius - this.world.rand.nextInt(diameter)) + this.pos.getY();
        int z = (radius - this.world.rand.nextInt(diameter)) + this.pos.getZ();

        // limit height - lower bounds
        if (y < 3) y = 3;

        // dont try to place blocks outside of the world height
        BlockPos targetPos = new BlockPos(x, y + 4, z);
        int worldHeightCap = world.getHeight();
        if(targetPos.getY() > worldHeightCap)
            targetPos = new BlockPos(targetPos.getX(), worldHeightCap - 1, targetPos.getZ());

        if(!this.world.isBlockLoaded(targetPos)) return;
        if (this.world.isAirBlock(targetPos) && this.world.getLightFor(EnumLightType.BLOCK, targetPos) < TorchmasterConfig.feralFlareMinLightLevel)
        {
            if(this.useLineOfSight)
            {
                Vec3d start = new Vec3d(targetPos).add(0.5, 0.5, 0.5);
                Vec3d end = new Vec3d(this.pos).add(0.5, 0.5, 0.5);
                RayTraceResult rtResult = world.rayTraceBlocks(start, end, RayTraceFluidMode.NEVER, true, false);

                if(rtResult != null)
                {
                    BlockPos hitPos = rtResult.getBlockPos();
                    if(rtResult.type == RayTraceResult.Type.BLOCK && !(hitPos.getX() == this.pos.getX() && hitPos.getY() == this.pos.getY() && hitPos.getZ() == this.pos.getZ()))
                        return;
                }
            }

            if(this.world.setBlockState(targetPos, ModBlocks.InvisibleLight.getDefaultState(), 3))
            {
                this.childLights.add(targetPos);
                this.markDirty();
            }
        }
    }

    public void removeChildLights()
    {
        if(this.world.isRemote) return;
        for(BlockPos pos : this.childLights)
        {
            if (this.world.getBlockState(pos).getBlock() == ModBlocks.InvisibleLight)
            {
                if(TorchmasterConfig.feralFlareLightDecayInstantly)
                    this.world.removeBlock(pos);
                else
                    this.world.setBlockState(pos, ModBlocks.InvisibleLight.getDecayState(), 2);
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

    public void setUseLineOfSight(boolean state)
    {
        this.useLineOfSight = state;
        this.markDirty();
        IBlockState blockState = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, blockState, blockState, 0);
    }

    public boolean shouldUseLineOfSight()
    {
        return this.useLineOfSight;
    }
}
