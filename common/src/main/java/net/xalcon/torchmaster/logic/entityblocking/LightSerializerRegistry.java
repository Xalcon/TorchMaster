package net.xalcon.torchmaster.logic.entityblocking;

import net.xalcon.torchmaster.logic.entityblocking.dreadlamp.DreadLampSerializer;
import net.xalcon.torchmaster.logic.entityblocking.megatorch.MegatorchSerializer;

import java.util.HashMap;
import java.util.Optional;

public class LightSerializerRegistry
{
    private static HashMap<String, ILightSerializer> registry = new HashMap<>();

    static
    {
        registerLightSerializer(MegatorchSerializer.INSTANCE);
        registerLightSerializer(DreadLampSerializer.INSTANCE);
    }

    public static void registerLightSerializer(ILightSerializer serializer)
    {
        String lightSerializerKey = serializer.getSerializerKey();

        if(lightSerializerKey == null)
            throw new RuntimeException("SerializerKey is null! " + serializer.getClass().getCanonicalName());

        if(registry.containsKey(lightSerializerKey))
            throw new RuntimeException("lightSerializer '" + lightSerializerKey + "' already exists");

        registry.put(lightSerializerKey, serializer);
    }

    public static Optional<ILightSerializer> getLightSerializer(String lightSerializerKey)
    {
        var serializer = registry.get(lightSerializerKey);
        if(serializer == null) return Optional.empty();
        return Optional.of(registry.get(lightSerializerKey));
    }
}
