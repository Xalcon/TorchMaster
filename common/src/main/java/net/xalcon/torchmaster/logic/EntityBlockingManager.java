package net.xalcon.torchmaster.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.xalcon.torchmaster.Torchmaster;

import java.util.HashMap;
import java.util.Map;

public class EntityBlockingManager extends SavedData
{
    private final Map<String, EntityBlocker> entityBlockers = new HashMap<>();

    public void registerBlocker(EntityBlocker blocker)
    {
        entityBlockers.putIfAbsent(blocker.getIdentifier(), blocker);
        setDirty();
    }

    public void unregisterBlocker(EntityBlocker blocker)
    {
        entityBlockers.remove(blocker.getIdentifier());
        setDirty();
    }

    public boolean shouldBlockEntitySpawn(Entity entity, Level level, MobSpawnType spawnType)
    {
        for(var blocker: entityBlockers.values())
        {
            if(blocker.shouldBlockEntitySpawn(entity, level, spawnType))
            {
                return true;
            }
        }
        return false;
    }

    public boolean shouldBlockVillageSiege(Level level, BlockPos pos)
    {
        for(var blocker: entityBlockers.values())
        {
            if(blocker.shouldBlockVillageSiege(level, pos))
            {
                return true;
            }
        }
        return false;
    }

    public boolean shouldBlockVillageRaid(Level level, BlockPos pos)
    {
        for(var blocker: entityBlockers.values())
        {
            if(blocker.shouldBlockVillageRaid(level, pos))
            {
                return true;
            }
        }
        return false;
    }

    public CompoundTag save()
    {
        var list = new ListTag();
        for (EntityBlocker blocker : entityBlockers.values())
        {
            var tag = EntityBlockerSerializerRegistry.Serialize(blocker);
            if(tag == null)
            {
                Torchmaster.LOG.error("Unable to save entity blocker {}, data is lost", blocker);
                continue;
            }
            list.add(tag);
        }
        var tag = new CompoundTag();
        tag.put("blockers", list);
        return tag;
    }

    public void loadFrom(CompoundTag tag)
    {
        entityBlockers.clear();

        var list = tag.getList("blockers", Tag.TAG_COMPOUND);
        for(var i = 0; i < list.size(); i++)
        {
            var blocker = EntityBlockerSerializerRegistry.Deserialize(list.getCompound(i));
            if(blocker == null)
            {
                Torchmaster.LOG.error("Unable to load entity blocker from nbt, data is lost");
                continue;
            }

            entityBlockers.put(blocker.getIdentifier(), blocker);
        }

        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider)
    {
        compoundTag.put("registry", save());
        return compoundTag;
    }

    public static EntityBlockingManager load(CompoundTag tag, HolderLookup.Provider provider)
    {
        var registryTag = tag.getCompound("registry");
        var mgr = new EntityBlockingManager();
        mgr.loadFrom(registryTag);
        return mgr;
    }

    public static final SavedData.Factory<EntityBlockingManager> Factory = new Factory<>(EntityBlockingManager::new, EntityBlockingManager::load, null);
}
