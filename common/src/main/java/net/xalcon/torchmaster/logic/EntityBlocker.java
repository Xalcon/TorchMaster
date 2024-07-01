package net.xalcon.torchmaster.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;

public interface EntityBlocker
{
    String getIdentifier();
    ResourceLocation getType();
    boolean shouldBlockEntitySpawn(Entity entity, Level level, MobSpawnType spawnType);
    boolean shouldBlockVillageSiege(Level level, BlockPos pos);
    boolean shouldBlockVillageRaid(Level level, BlockPos pos);
}
