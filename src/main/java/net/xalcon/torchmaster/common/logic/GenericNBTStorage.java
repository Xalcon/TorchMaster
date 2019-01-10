package net.xalcon.torchmaster.common.logic;

import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class GenericNBTStorage<T extends INBTSerializable<NBTTagCompound>> implements Capability.IStorage<T>
{
    @Nullable
    @Override
    public INBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)
    {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, EnumFacing side, INBTBase nbt)
    {
        if(nbt instanceof NBTTagCompound)
            instance.deserializeNBT((NBTTagCompound) nbt);
    }
}
