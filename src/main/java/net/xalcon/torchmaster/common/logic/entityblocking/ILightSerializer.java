package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.nbt.CompoundTag;

public interface ILightSerializer
{
    CompoundTag serializeLight(String lightKey, IEntityBlockingLight light);
    IEntityBlockingLight deserializeLight(String lightKey, CompoundTag nbt);
    String getSerializerKey();
}
