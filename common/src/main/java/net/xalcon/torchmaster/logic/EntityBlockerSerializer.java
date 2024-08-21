package net.xalcon.torchmaster.logic;

import net.minecraft.nbt.CompoundTag;

public interface EntityBlockerSerializer<T extends EntityBlocker >
{
    void SerializeInto(CompoundTag tag, EntityBlocker blocker);
    EntityBlocker DeserializeFrom(CompoundTag tag);
}
