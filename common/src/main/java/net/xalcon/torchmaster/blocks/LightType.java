package net.xalcon.torchmaster.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public enum LightType
{
    MegaTorch(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), new Vec3(.5f, 1f, .5f)),
    DreadLamp(Block.box(1, 1, 1, 15, 15, 15), new Vec3(.5f, .3f, .5f)),
    DreadTorch(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D), new Vec3(.5f, 1f, .5f));

    LightType(VoxelShape shape, Vec3 flameOffset)
    {
        Shape = shape;
        FlameOffset = flameOffset;
    }

    public final VoxelShape Shape;
    public final Vec3 FlameOffset;
}
