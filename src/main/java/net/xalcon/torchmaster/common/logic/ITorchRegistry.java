package net.xalcon.torchmaster.common.logic;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITorchRegistry extends INBTSerializable<NBTTagList>
{
    void register(BlockPos pos);
    void unregister(BlockPos pos);
    boolean shouldEntityBeBlocked(Entity entity);
    void onGlobalTick(World world);
}
