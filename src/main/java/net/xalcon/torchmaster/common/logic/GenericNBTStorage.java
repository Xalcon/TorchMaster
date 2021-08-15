package net.xalcon.torchmaster.common.logic;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
    public void readNBT(Capability<T> capability, T instance, Direction side, Tag nbt)
    {
        if(nbt instanceof CompoundTag)
            instance.deserializeNBT((CompoundTag) nbt);
    }
}
