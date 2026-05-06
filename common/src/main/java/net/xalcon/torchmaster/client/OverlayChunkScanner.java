package net.xalcon.torchmaster.client;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;

public final class OverlayChunkScanner
{
    private OverlayChunkScanner() {}

    public static void scan(LevelChunk chunk, ResourceKey<Level> dimension)
    {
        Block torchBlock = ModRegistry.blockMegaTorch.get();
        LevelChunkSection[] sections = chunk.getSections();
        ChunkPos cp = chunk.getPos();
        int chunkMinX = cp.getMinBlockX();
        int chunkMinZ = cp.getMinBlockZ();

        for (int sIdx = 0; sIdx < sections.length; sIdx++)
        {
            LevelChunkSection section = sections[sIdx];
            if (section == null || section.hasOnlyAir()) continue;
            if (!section.maybeHas(s -> s.is(torchBlock))) continue;

            int sectionMinY = chunk.getSectionYFromSectionIndex(sIdx) << 4;
            for (int xx = 0; xx < 16; xx++)
            {
                for (int yy = 0; yy < 16; yy++)
                {
                    for (int zz = 0; zz < 16; zz++)
                    {
                        BlockState state = section.getBlockState(xx, yy, zz);
                        if (state.is(torchBlock) && state.getValue(EntityBlockingLightBlock.OVERLAY_VISIBLE))
                        {
                            OverlayCache.add(dimension, new BlockPos(
                                    chunkMinX + xx,
                                    sectionMinY + yy,
                                    chunkMinZ + zz));
                        }
                    }
                }
            }
        }
    }
}
