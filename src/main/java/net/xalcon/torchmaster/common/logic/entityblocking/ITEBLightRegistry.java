package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.xalcon.torchmaster.common.commands.TorchInfo;

public interface ITEBLightRegistry extends INBTSerializable<CompoundTag>
{
    boolean shouldBlockEntity(Entity entity, BlockPos pos);

    /**
     * Warning: The IEntityBlockingLight instance should not be directly attached to any chunk data!
     * This means, there should be no tileentities or entities implementing this interface!
     * The registry will call every instance on spawn checks, and since the registry doesnt care about chunks
     * it might cause chunk loads, memory leaks or other unexpected behaviors.
     * If an implementor of IEntityBlockingLight is only supposed to work while a chunk is loaded, registration
     * needs to be handled accordingly.
     *
     * Registrations are persisted across world loads, a registration is therefore only needed once.
     * @param lightKey a dimension-unique key for the given light
     * @param light the light instance
     */
    void registerLight(String lightKey, IEntityBlockingLight light);
    void unregisterLight(String lightKey);
    IEntityBlockingLight getLight(String lightKey);

    void onGlobalTick(Level level);

    TorchInfo[] getEntries();
}
