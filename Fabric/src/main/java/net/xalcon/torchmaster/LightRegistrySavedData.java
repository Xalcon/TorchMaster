package net.xalcon.torchmaster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.xalcon.torchmaster.logic.entityblocking.BlockingLightRegistry;

public class LightRegistrySavedData extends SavedData
{
    public static final String ID = "torchmaster_lights";

    private final BlockingLightRegistry registry = new BlockingLightRegistry();

    @Override
    public CompoundTag save(CompoundTag compoundTag)
    {
        compoundTag.put("registry", registry.serializeNBT());
        return compoundTag;
    }

    public BlockingLightRegistry getRegistry()
    {
        return registry;
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }

    public static LightRegistrySavedData load(CompoundTag tag)
    {
        var registryTag = tag.getCompound("registry");
        var data = new LightRegistrySavedData();
        data.registry.deserializeNBT(registryTag);
        return data;
    }
}
