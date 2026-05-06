package net.xalcon.torchmaster.client;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongConsumer;

public final class OverlayCache
{
    private static final Map<ResourceKey<Level>, LongSet> CACHE = new HashMap<>();

    private OverlayCache() {}

    public static synchronized void add(ResourceKey<Level> dimension, BlockPos pos)
    {
        CACHE.computeIfAbsent(dimension, k -> new LongOpenHashSet()).add(pos.asLong());
    }

    public static synchronized void remove(ResourceKey<Level> dimension, BlockPos pos)
    {
        LongSet set = CACHE.get(dimension);
        if (set != null) set.remove(pos.asLong());
    }

    public static synchronized void toggle(ResourceKey<Level> dimension, BlockPos pos)
    {
        LongSet set = CACHE.computeIfAbsent(dimension, k -> new LongOpenHashSet());
        long key = pos.asLong();
        if (!set.add(key)) set.remove(key);
    }

    public static synchronized boolean contains(ResourceKey<Level> dimension, BlockPos pos)
    {
        LongSet set = CACHE.get(dimension);
        return set != null && set.contains(pos.asLong());
    }

    public static synchronized void clearLevel(ResourceKey<Level> dimension)
    {
        CACHE.remove(dimension);
    }

    public static synchronized void clearChunk(ResourceKey<Level> dimension, ChunkPos chunkPos)
    {
        LongSet set = CACHE.get(dimension);
        if (set == null) return;
        int minX = chunkPos.getMinBlockX();
        int minZ = chunkPos.getMinBlockZ();
        int maxX = chunkPos.getMaxBlockX();
        int maxZ = chunkPos.getMaxBlockZ();
        set.removeIf((long packed) ->
        {
            int x = BlockPos.getX(packed);
            int z = BlockPos.getZ(packed);
            return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
        });
    }

    public static synchronized void forEach(ResourceKey<Level> dimension, LongConsumer consumer)
    {
        LongSet set = CACHE.get(dimension);
        if (set == null) return;
        set.forEach(consumer);
    }

    public static synchronized int size(ResourceKey<Level> dimension)
    {
        LongSet set = CACHE.get(dimension);
        return set == null ? 0 : set.size();
    }
}
