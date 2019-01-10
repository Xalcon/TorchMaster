package net.xalcon.torchmaster.common.logic;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITorchRegistryContainer extends INBTSerializable<NBTTagCompound>
{
    ITorchRegistry getMegaTorchRegistry();
    ITorchRegistry getDreadLampRegistry();
    boolean shouldEntityBeBlocked(Entity entity);
    void onGlobalTick(World world);
}
