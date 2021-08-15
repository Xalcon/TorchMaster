package net.xalcon.torchmaster.common.tiles;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FeralFlareLanternTileEntity extends BlockEntity
{
    private FakePlayer fakePlayer;
    private int ticks;
    private boolean useLineOfSight;
    private List<BlockPos> childLights = new ArrayList<>();

    public FeralFlareLanternTileEntity(BlockPos pos, BlockState state)
    {
        super(ModBlocks.tileFeralFlareLantern, pos, state);
    }

    // @Override
    public void tick()
    {
        if(this.level.isClientSide || ++this.ticks % TorchmasterConfig.GENERAL.feralFlareTickRate.get() != 0) return;
        if(this.childLights.size() > TorchmasterConfig.GENERAL.feralFlareLanternLightCountHardcap.get()) return;
        ticks = 0;

        if(fakePlayer == null)
        {
            fakePlayer = FakePlayerFactory.get((ServerLevel) level, new GameProfile(UUID.fromString("2282ab80-e482-11e9-81b4-2a2ae2dbcce4"), "TorchMasterFeralFlareLantern"));
        }

        int radius = TorchmasterConfig.GENERAL.feralFlareRadius.get();
        int diameter = radius * 2;

        int x = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getX();
        int y = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getY();
        int z = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getZ();

        // limit height - lower bounds
        if (y < 3) y = 3;

        // limit height - upper bounds
        BlockPos targetPos = new BlockPos(x, y, z);
        int surfaceHeight = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, targetPos.getX(), targetPos.getZ());
        if (targetPos.getY() > surfaceHeight + 4)
            targetPos = targetPos.atY(surfaceHeight).above(4);

        // dont try to place blocks outside of the world height
        int worldHeightCap = level.getHeight();
        if(targetPos.getY() > worldHeightCap)
            targetPos = new BlockPos(targetPos.getX(), worldHeightCap - 1, targetPos.getZ());

        if(!this.level.isLoaded(targetPos)) return;

        if (this.level.isEmptyBlock(targetPos) && this.level.getBrightness(LightLayer.BLOCK, targetPos) < TorchmasterConfig.GENERAL.feralFlareMinLightLevel.get())
        {
            if(this.useLineOfSight)
            {
                Vec3 start = new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ()).add(0.5, 0.5, 0.5);
                Vec3 end = new Vec3(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ()).add(0.5, 0.5, 0.5);
                ClipContext rtxCtx = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, fakePlayer);
                BlockHitResult rtResult = level.clip(rtxCtx);

                if(rtResult.getType() == BlockHitResult.Type.BLOCK)
                {
                    BlockPos hitPos = rtResult.getBlockPos();
                    if(!(hitPos.getX() == this.worldPosition.getX() && hitPos.getY() == this.worldPosition.getY() && hitPos.getZ() == this.worldPosition.getZ()))
                        return;
                }
            }

            if(this.level.setBlock(targetPos, ModBlocks.blockInvisibleLight.defaultBlockState(), 3))
            {
                this.childLights.add(targetPos);
                this.setChanged();
            }
        }
    }

    public static <T extends BlockEntity> void dispatchTickBlockEntity(Level level, BlockPos pos, BlockState state, T blockEntity)
    {
        if(blockEntity instanceof FeralFlareLanternTileEntity)
            ((FeralFlareLanternTileEntity)blockEntity).tick();
    }

    @Override
    public void load(CompoundTag nbt)
    {
        this.childLights.clear();
        if(nbt.getTagType("lights") == Constants.NBT.TAG_INT_ARRAY)
        {
            BlockPos origin = new BlockPos(nbt.getInt("x"), nbt.getInt("y"),nbt.getInt("z"));
            int[] lightsEncoded = ((IntArrayTag) nbt.get("lights")).getAsIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.load(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.worldPosition, child));

        nbt.put("lights", new IntArrayTag(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
        return super.save(nbt);
    }

    public void setUseLineOfSight(boolean state)
    {
        this.useLineOfSight = state;
        this.setChanged();
        BlockState blockState = this.level.getBlockState(this.worldPosition);
        this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 0);
    }

    public boolean shouldUseLineOfSight()
    {
        return this.useLineOfSight;
    }

    public void removeChildLights()
    {
        if(this.level.isClientSide) return;
        for(var pos : this.childLights)
        {
            if (this.level.getBlockState(pos).getBlock() == ModBlocks.blockInvisibleLight)
            {
                this.level.removeBlock(pos, false);
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
        return origin.offset(x, y, z);
    }
}
