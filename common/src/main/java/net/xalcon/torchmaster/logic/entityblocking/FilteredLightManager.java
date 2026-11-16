package net.xalcon.torchmaster.logic.entityblocking;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.Torchmaster;

import java.util.*;

public class FilteredLightManager extends SavedData implements IBlockingLightManager
{
    static record LightData(String key, String type, CompoundTag data) {
    }

    private final Map<String, IEntityBlockingLight> lights = new HashMap<>();

    private static final Codec<LightData> LIGHT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("key").forGetter(LightData::key),
            Codec.STRING.fieldOf("type").forGetter(LightData::type),
            CompoundTag.CODEC.fieldOf("data").forGetter(LightData::data)
    ).apply(instance, LightData::new));

    private static final Codec<FilteredLightManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LIGHT_CODEC.listOf().fieldOf("lights").forGetter(FilteredLightManager::save)
    ).apply(instance, FilteredLightManager::load));

    private static FilteredLightManager load(List<LightData> lights)
    {
        var mgr = new FilteredLightManager();
        for(var light : lights)
        {
            LightSerializerRegistry.getLightSerializer(light.type)
                    .ifPresentOrElse(serializer ->
                            serializer.deserializeLight(light.data).ifPresentOrElse(
                                    l -> mgr.lights.put(light.key, l),
                                    () -> Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}, deserialization failed, data is lost", light.key, light.type)
                            ), () -> Torchmaster.LOG.error("Unable to load light data from nbt for {} - {}. Serializer not found, data is lost", light.key, light.type));
        }
        return mgr;
    }

    private List<LightData> save()
    {
        var lightDataList = new ArrayList<LightData>();
        for (var pair : lights.entrySet())
        {
            var lightKey = pair.getKey();
            var light = pair.getValue();
            var serializerType = light.getLightSerializerType();
            LightSerializerRegistry.getLightSerializer(serializerType).ifPresentOrElse(serializer -> {
                var tag = serializer.serializeLight(light);
                var lightData = new LightData(lightKey, serializerType, tag);
                lightDataList.add(lightData);
            }, () -> Torchmaster.LOG.error("Unable to save light {}, data is lost", light.getPos()));
        }
        return lightDataList;
    }

    @Override
    public boolean shouldBlockEntityType(EntityType<?> entityType, Level level, Vec3 pos, EntitySpawnReason spawnType)
    {
        for(var light: lights.values())
        {
            if(light.shouldBlockEntityType(entityType, level, pos, spawnType))
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

    public static SavedDataType<FilteredLightManager> typeFor(String id)
    {
        return new SavedDataType<>(Torchmaster.modLoc(id), FilteredLightManager::new, CODEC, null);
    }
}
