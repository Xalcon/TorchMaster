package net.xalcon.torchmaster.common.logic;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class GenericNBTStorage<T extends INBTSerializable<CompoundTag>> implements Capability.IStorage<T>
{
    @Nullable
    @Override
    public Tag writeNBT(Capability<T> capability, T instance, Direction side)
    {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt)
    {
        if(nbt instanceof CompoundNBT)
            instance.deserializeNBT((CompoundNBT) nbt);
    }
}
