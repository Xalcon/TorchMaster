package net.xalcon.torchmaster.common.logic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.xalcon.torchmaster.common.ModCaps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderTorchRegistryContainer implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound>
{
    private ITorchRegistryContainer container = new TorchRegistryContainer();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == ModCaps.TORCH_REGISTRY_CONTAINER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        return hasCapability(capability, facing)
                ? ModCaps.TORCH_REGISTRY_CONTAINER.cast(container)
                : null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return container.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        container.deserializeNBT(nbt);
    }
}
