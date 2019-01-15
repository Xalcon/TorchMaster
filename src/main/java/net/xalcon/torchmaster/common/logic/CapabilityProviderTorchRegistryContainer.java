package net.xalcon.torchmaster.common.logic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.OptionalCapabilityInstance;
import net.xalcon.torchmaster.common.init.ModCaps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderTorchRegistryContainer implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound>
{
    private ITorchRegistryContainer container = new TorchRegistryContainer();

    @Nonnull
    @Override
    public <T> OptionalCapabilityInstance<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side)
    {

        return cap == ModCaps.TORCH_REGISTRY_CONTAINER
                ? OptionalCapabilityInstance.of(() -> (T)container)
                : OptionalCapabilityInstance.empty();
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
