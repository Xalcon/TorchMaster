package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

public class InvisibleLightBlock extends Block
{
    public InvisibleLightBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderType(BlockState p_149645_1_)
    {
        return BlockRenderType.MODEL;
    }
}
