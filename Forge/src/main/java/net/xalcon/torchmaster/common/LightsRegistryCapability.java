package net.xalcon.torchmaster.common;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.xalcon.torchmaster.logic.entityblocking.BlockingLightRegistry;
import net.xalcon.torchmaster.logic.entityblocking.IBlockingLightRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LightsRegistryCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
    private final IBlockingLightRegistry container = new BlockingLightRegistry();
    /**
     * forge encourages modders to cache the following optional and use its listening feature.
     * We are never invalidating the instance, but its probably not a bad idea to only have one instance
     */
    private final LazyOptional optional = LazyOptional.of(() -> container);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == ModCaps.TEB_REGISTRY)
            return optional;

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return container.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        container.deserializeNBT(nbt);
    }

}
