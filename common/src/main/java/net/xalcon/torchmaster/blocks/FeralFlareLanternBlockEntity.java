package net.xalcon.torchmaster.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.platform.Services;

import java.util.ArrayList;
import java.util.List;

public class FeralFlareLanternBlockEntity extends BlockEntity
{
    private int ticks;
    private boolean useLineOfSight;
    private List<BlockPos> childLights = new ArrayList<>();

    public FeralFlareLanternBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModRegistry.tileFeralFlareLantern.get(), pos, state);
    }

    // @Override
    public void tick()
    {
        var config = Services.PLATFORM.getConfig();
        if(this.level.isClientSide || ++this.ticks % config.getFeralFlareTickRate() != 0) return;
        if(this.childLights.size() > config.getFeralFlareLanternLightCountHardcap()) return;
        ticks = 0;

        int radius = config.getFeralFlareRadius();
        int diameter = radius * 2;

        int x = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getX();
        int y = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getY();
        int z = (radius - this.level.random.nextInt(diameter)) + this.worldPosition.getZ();

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

        if (this.level.isEmptyBlock(targetPos) && this.level.getBrightness(LightLayer.BLOCK, targetPos) < config.getFeralFlareMinLightLevel())
        {
            if(this.useLineOfSight)
            {
                Vec3 start = new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ()).add(0.5, 0.5, 0.5);
                Vec3 end = new Vec3(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ()).add(0.5, 0.5, 0.5);
                ClipContext rtxCtx = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty());
                BlockHitResult rtResult = level.clip(rtxCtx);

                if(rtResult.getType() == BlockHitResult.Type.BLOCK)
                {
                    BlockPos hitPos = rtResult.getBlockPos();
                    if(!(hitPos.getX() == this.worldPosition.getX() && hitPos.getY() == this.worldPosition.getY() && hitPos.getZ() == this.worldPosition.getZ()))
                        return;
                }
            }

            if(this.level.setBlock(targetPos, ModRegistry.blockInvisibleLight.get().defaultBlockState(), Block.UPDATE_ALL))
            {
                this.childLights.add(targetPos);
                this.setChanged();
            }
        }
    }

    public static <T extends BlockEntity> void dispatchTickBlockEntity(Level level, BlockPos pos, BlockState state, T blockEntity)
    {
        if(blockEntity instanceof FeralFlareLanternBlockEntity)
            ((FeralFlareLanternBlockEntity)blockEntity).tick();
    }

    //@Nullable
    //@Override
    //public Packet<ClientGamePacketListener> getUpdatePacket() {
    //    return ClientboundBlockEntityDataPacket.create(this);
    //}

    //@Override
    //public CompoundTag getUpdateTag() {
    //    var tag = super.getUpdateTag();
    //    tag.putBoolean("useLoS", useLineOfSight);
    //    return tag;
    //}

    //@Override
    //public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    //    super.onDataPacket(net, pkt);
    //}

    //@Override
    //public void handleUpdateTag(CompoundTag tag) {
    //    super.handleUpdateTag(tag);
    //}

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        super.saveAdditional(nbt, pRegistries);
        List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
        for(BlockPos child : this.childLights)
            childLightsEncoded.add(encodePosition(this.worldPosition, child));

        nbt.put("lights", new IntArrayTag(childLightsEncoded));
        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("useLoS", this.useLineOfSight);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
    {
        this.childLights.clear();
        if(nbt.getTagType("lights") == Tag.TAG_INT_ARRAY)
        {
            BlockPos origin = new BlockPos(nbt.getInt("x"), nbt.getInt("y"),nbt.getInt("z"));
            int[] lightsEncoded = ((IntArrayTag) nbt.get("lights")).getAsIntArray();
            for(int encodedLight : lightsEncoded)
                this.childLights.add(decodePosition(origin, encodedLight));
        }
        this.ticks = nbt.getInt("ticks");
        this.useLineOfSight = nbt.getBoolean("useLoS");
        super.loadAdditional(nbt, pRegistries);
    }

    // @Override
    // public CompoundTag save(CompoundTag nbt)
    // {
    //     List<Integer> childLightsEncoded = new ArrayList<>(this.childLights.size());
    //     for(BlockPos child : this.childLights)
    //         childLightsEncoded.add(encodePosition(this.worldPosition, child));
    //     nbt.put("lights", new IntArrayTag(childLightsEncoded));
    //     nbt.putInt("ticks", this.ticks);
    //     nbt.putBoolean("useLoS", this.useLineOfSight);
    //     return super.save(nbt);
    // }

    public void setUseLineOfSight(boolean state)
    {
        Torchmaster.LOG.info("Current: {}, New: {}", useLineOfSight, state);
        this.useLineOfSight = state;
        this.setChanged();
        BlockState blockState = this.level.getBlockState(this.worldPosition);
        this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 3);
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
            if (this.level.getBlockState(pos).getBlock() == ModRegistry.blockInvisibleLight.get())
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
