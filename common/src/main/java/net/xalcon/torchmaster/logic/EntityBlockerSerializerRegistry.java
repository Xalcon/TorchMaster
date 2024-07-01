package net.xalcon.torchmaster.logic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;
import net.xalcon.torchmaster.Torchmaster;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EntityBlockerSerializerRegistry
{
    private static final Map<ResourceLocation, EntityBlockerSerializer<?>> serializers = new HashMap<>();

    public static void RegisterSerializer(ResourceLocation type, EntityBlockerSerializer<?> serializer)
    {
        if(serializers.containsKey(type))
        {
            Torchmaster.LOG.error("Serializer with type '{}' was already registered", type);
            return;
        }

        serializers.put(type, serializer);
    }

    @Nullable
    public static EntityBlocker Deserialize(CompoundTag tag)
    {
        var typeStr = tag.getString("_type");
        if(typeStr.isEmpty())
        {
            Torchmaster.LOG.error("Unable to deserialize EntityBlocker, empty type");
            return null;
        }

        var type = ResourceLocation.tryParse(typeStr);
        if(type == null)
        {
            Torchmaster.LOG.error("Unable to deserialize EntityBlocker, can't parse type '{}'", typeStr);
            return null;
        }

        var serializer = serializers.get(type);
        if(serializer == null)
        {
            Torchmaster.LOG.error("Unable to deserialize EntityBlocker, type '{}' not found in registry", typeStr);
            return null;
        }

        return serializer.DeserializeFrom(tag);
    }

    @Nullable
    public static CompoundTag Serialize(EntityBlocker blocker)
    {
        var tag = new CompoundTag();
        var type = blocker.getType();
        var serializer = serializers.get(type);
        if(serializer == null)
        {
            Torchmaster.LOG.error("Serializer for type '{}' not found, unable to save data", type);
            return null;
        }

        serializer.SerializeInto(tag, blocker);
        tag.putString("_type", type.toString());
        return tag;
    }
}
