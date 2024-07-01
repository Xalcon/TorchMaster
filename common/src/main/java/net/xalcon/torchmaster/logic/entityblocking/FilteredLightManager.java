package net.xalcon.torchmaster.logic.entityblocking;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Torchmaster;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FilteredLightManager extends SavedData implements IBlockingLightManager
{
    private final Map<String, IEntityBlockingLight> lights = new HashMap<>();

    @Override
    public boolean shouldBlockEntity(Entity entity, Level level, MobSpawnType spawnType)
    {
        for(var light: lights.values())
        {
            if(light.shouldBlockEntity(entity, level, spawnType))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldBlockVillagePillagerSiege(Vec3 pos)
    {
        for(var light: lights.values())
        {
            if(light.shouldBlockVillagePillagerSiege(pos))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldBlockVillageZombieRaid(Vec3 pos)
    {
        for(var light: lights.values())
        {
            if(light.shouldBlockVillageZombieRaid(pos))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerLight(String lightKey, IEntityBlockingLight light)
    {
        lights.put(lightKey, light);
        setDirty();
    }

    @Override
    public void unregisterLight(String lightKey)
    {
        lights.remove(lightKey);
        setDirty();
    }

    @Override
    public Optional<IEntityBlockingLight> getLight(String lightKey)
    {
        var light = lights.get(lightKey);
        if(light == null) return Optional.empty();
        return Optional.of(light);
    }

    @Override
    public void onGlobalTick(Level level)
    {
        // TODO: Rate limit this
        for(var light: lights.values())
        {
            light.cleanupCheck(level);
        }
    }

    @Override
    public TorchInfo[] getEntries()
    {
        // TODO: implement for command?
        return new TorchInfo[0];
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider)
    {
        var list = new ListTag();
        for (var pair : lights.entrySet())
        {
            var lightKey = pair.getKey();
            var light = pair.getValue();
            var serializerType = light.getLightSerializerType();
            LightSerializerRegistry.getLightSerializer(serializerType).ifPresentOrElse(serializer -> {
                var tag = serializer.serializeLight(light);
                tag.putString("_type", serializerType);
                tag.putString("_key", lightKey);
                list.add(tag);
            }, () -> Torchmaster.LOG.error("Unable to save light {}, data is lost", light.getPos()));
        }
        var tag = new CompoundTag();
        tag.put("lights", list);
        return tag;
    }

    private static FilteredLightManager load(CompoundTag tag, HolderLookup.Provider provider)
    {
        var lightsList = tag.getList("lights", Tag.TAG_COMPOUND);
        var mgr = new FilteredLightManager();
        mgr.lights.clear();
        for(var i = 0; i < lightsList.size(); i++)
        {
            var lightNbt = lightsList.getCompound(i);
            var lightKey = lightNbt.getString("_key");
            var serializerType = lightNbt.getString("_type");
            LightSerializerRegistry.getLightSerializer(serializerType)
                .ifPresentOrElse(serializer ->
                        serializer.deserializeLight(lightNbt).ifPresentOrElse(
                                l -> mgr.lights.put(lightKey, l),
                                () -> Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}, deserialization failed, data is lost", lightKey, serializerType)
                        ), () -> Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}. Serializer not found, data is lost", lightKey, serializerType));
        }
        return mgr;
    }

    public static final SavedData.Factory<FilteredLightManager> Factory = new Factory<>(FilteredLightManager::new, FilteredLightManager::load, null);
}
