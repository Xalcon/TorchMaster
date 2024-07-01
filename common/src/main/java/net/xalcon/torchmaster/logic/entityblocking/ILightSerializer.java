package net.xalcon.torchmaster.logic.entityblocking;

import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public interface ILightSerializer
{
    CompoundTag serializeLight(IEntityBlockingLight light);
    Optional<IEntityBlockingLight> deserializeLight(CompoundTag nbt);
    String getSerializerKey();
}
