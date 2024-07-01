package net.xalcon.torchmaster.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.dreadlamp.DreadLampEntityBlockingLight;

import java.util.function.Function;

public enum LightType
{
    MegaTorch(
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            new Vec3(.5f, 1f, .5f),
            pos -> "MT_" +pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
            MegatorchEntityBlockingLight::new
    ),
    DreadLamp(
            Block.box(1, 1, 1, 15, 15, 15),
            new Vec3(.5f, .3f, .5f),
            pos -> "DL_" + pos.getX() + "_" + pos.getY() + "_" + pos.getZ(),
            DreadLampEntityBlockingLight::new
    );

    LightType(VoxelShape shape, Vec3 flameOffset, Function<BlockPos, String> keyFactory, Function<BlockPos, IEntityBlockingLight> lightFactory)
    {
        Shape = shape;
        FlameOffset = flameOffset;
        KeyFactory = keyFactory;
        LightFactory = lightFactory;
    }

    public final VoxelShape Shape;
    public final Vec3 FlameOffset;
    public final Function<BlockPos, String> KeyFactory;
    public final Function<BlockPos, IEntityBlockingLight> LightFactory;
}
