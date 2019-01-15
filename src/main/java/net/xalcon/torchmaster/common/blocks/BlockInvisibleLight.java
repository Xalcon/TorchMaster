package net.xalcon.torchmaster.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumBlockRenderType;
import net.xalcon.torchmaster.Torchmaster;

public class BlockInvisibleLight extends Block
{
    private final static BooleanProperty DECAY = BooleanProperty.create("decay");

    public static final String INTERNAL_NAME = "invisible_light";
    private IBlockState decayState;

    public BlockInvisibleLight()
    {
        super(Builder.create(Material.AIR).lightValue(15));
        this.setRegistryName(Torchmaster.MODID, INTERNAL_NAME);
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
}
