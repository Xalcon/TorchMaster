package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumBlockRenderType;

public class BlockInvisibleLight extends BlockBase
{
    private final static BooleanProperty DECAY = BooleanProperty.create("decay");

    public static final String INTERNAL_NAME = "invisible_light";
    private IBlockState decayState;

    public BlockInvisibleLight()
    {
        super(INTERNAL_NAME, Builder.create(Material.AIR)
                //.lightLevel(15)
        );
        this.setDefaultState(this.getDefaultState().with(DECAY, false));
        this.decayState = this.getDefaultState().with(DECAY, true);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(DECAY);
    }

    @Override @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public IBlockState getDecayState()
    {
        return this.decayState;
    }

    @Override
    public Item createItemBlock()
    {
        return null;
    }
}
