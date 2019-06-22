package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.nbt.CompoundNBT;

public interface ILightSerializer
{
    CompoundNBT serializeLight(String lightKey, IEntityBlockingLight light);
    IEntityBlockingLight deserializeLight(String lightKey, CompoundNBT nbt);
    String getSerializerKey();
}
