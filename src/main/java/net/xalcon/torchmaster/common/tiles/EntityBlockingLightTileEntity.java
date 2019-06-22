package net.xalcon.torchmaster.common.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.HashSet;

public class EntityBlockingLightTileEntity extends TileEntity
{
    public EntityBlockingLightTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void onLoad()
    {

    }

    @Override
    public void onChunkUnloaded()
    {

    }
}
