package net.xalcon.torchmaster.common.logic.entityblocking;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.commands.TorchInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class LightsRegistryCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT>
{
    private ITEBLightRegistry container = new RegistryContainer();
    /**
     * forge encourages modders to cache the following optional and use its listening feature.
     * We are never invalidating the instance, but its probably not a bad idea to only have one instance
     */
    private LazyOptional optional = LazyOptional.of(() -> container);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == ModCaps.TEB_REGISTRY)
            return optional;

        return LazyOptional.empty();
    }

    public static Callable<ITEBLightRegistry> getDefaultFactory()
    {
        return RegistryContainer::new;
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        return container.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        container.deserializeNBT(nbt);
    }

    private static class RegistryContainer implements ITEBLightRegistry
    {
        private HashMap<String, IEntityBlockingLight> lights = new HashMap<>();

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT nbt = new CompoundNBT();

            ILightSerializer serializer = null;
            String cachedSerializerKey = null;

            for(HashMap.Entry<String, IEntityBlockingLight> lightEntry : lights.entrySet())
            {
                IEntityBlockingLight light = lightEntry.getValue();
                String lightKey = lightEntry.getKey();
                String serializerKey = light.getLightSerializerKey();

                if(serializerKey == null)
                {
                    Torchmaster.Log.error("Unable to serialize light '{}', the serializer was null", lightKey);
                    continue;
                }

                if(!serializerKey.equals(cachedSerializerKey))
                {
                    serializer = LightSerializerRegistry.getLightSerializer(serializerKey);
                    cachedSerializerKey = serializerKey;
                }

                if(serializer == null)
                {
                    Torchmaster.Log.error("Unable to serialize light '{}', the serializer '{}' was not found", lightKey, serializerKey);
                    continue;
                }

                try
                {
                    CompoundNBT lightNbt = serializer.serializeLight(lightKey, light);
                    if(lightNbt == null)
                    {
                        Torchmaster.Log.error("Unable to serialize light '{}', the serializer '{}' returned null", lightKey, serializerKey);
                        continue;
                    }
                    lightNbt.putString("lightSerializerKey", serializerKey);
                    nbt.put(lightKey, lightNbt);
                }
                catch (Exception ex)
                {
                    Torchmaster.Log.error("The serializer '{}' threw an error during serialization!", serializerKey);
                    Torchmaster.Log.error(ex);
                }
            }

            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt)
        {
            lights.clear();
            ILightSerializer serializer = null;
            String cachedSerializerKey = null;

            for(String lightKey : nbt.keySet())
            {
                CompoundNBT lightNbt = nbt.getCompound(lightKey);
                String serializerKey = lightNbt.getString("lightSerializerKey");

                if(!serializerKey.equals(cachedSerializerKey))
                {
                    serializer = LightSerializerRegistry.getLightSerializer(serializerKey);
                    cachedSerializerKey = serializerKey;
                }

                if(serializer == null)
                {
                    Torchmaster.Log.error("Unable to deserialize the light '{}', the serializer '{}' was not found", lightKey, serializerKey);
                    continue;
                }

                try
                {
                    IEntityBlockingLight light = serializer.deserializeLight(lightKey, lightNbt);
                    if(light == null)
                    {
                        Torchmaster.Log.error("Unable to deserialize the light '{}', the serializer returned null", lightKey);
                        continue;
                    }

                    lights.put(lightKey, light);
                }
                catch (Exception ex)
                {
                    Torchmaster.Log.error("The serializer '{}' threw an error during deserialization!", serializerKey);
                    Torchmaster.Log.error(ex);
                }
            }
        }

        @Override
        public boolean shouldBlockEntity(Entity entity, BlockPos pos)
        {
            for(HashMap.Entry<String, IEntityBlockingLight> lightEntry : lights.entrySet())
            {
                IEntityBlockingLight light = lightEntry.getValue();
                if(light.shouldBlockEntity(entity, pos))
                    return true;
            }
            return false;
        }

        @Override
        public void registerLight(String lightKey, IEntityBlockingLight light)
        {
            if(lightKey == null) throw new IllegalArgumentException("lightKey must not be null");
            if(light == null) throw new IllegalArgumentException("light must not be null");
            lights.put(lightKey, light);
        }

        @Override
        public void unregisterLight(String lightKey)
        {
            lights.remove(lightKey);
        }

        @Override
        @Nullable
        public IEntityBlockingLight getLight(String lightKey)
        {
            return lights.get(lightKey);
        }

        private int tickCounter;

        @Override
        public void onGlobalTick(World world)
        {
            if(tickCounter++ < 200)
                return;
            tickCounter = 0;
            lights.entrySet().removeIf(l -> l.getValue().cleanupCheck(world));
        }

        @Override
        public TorchInfo[] getEntries()
        {
            return this.lights.values().stream()
                .map(x -> new TorchInfo(x.getName(), x.getPos()))
                .toArray(TorchInfo[]::new);
        }
    }
}
