package net.xalcon.torchmaster.logic.entityblocking;

import net.xalcon.torchmaster.logic.entityblocking.dreadlamp.DreadLampSerializer;
import net.xalcon.torchmaster.logic.entityblocking.megatorch.MegatorchSerializer;

import java.util.HashMap;

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

    public static ILightSerializer getLightSerializer(String lightSerializerKey)
    {
        return registry.get(lightSerializerKey);
    }
}
