package net.xalcon.torchmaster.common.logic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;

public class TorchRegistryContainer implements ITorchRegistryContainer
{
    private ITorchRegistry megaTorchRegistry = new TorchRegistry()
    {
        @Override
        protected boolean shouldHandleEntityType(Class<? extends Entity> entityClass)
        {
            return TorchMasterMod.MegaTorchFilterRegistry.containsEntity(entityClass);
        }

        @Override
        protected int getTorchRange()
        {
            return TorchmasterConfig.MegaTorchRange;
        }

        @Override
        protected boolean isBlockStateValid(IBlockState state)
        {
            return state.getBlock() == ModBlocks.getMegaTorch();
        }
    };

    private ITorchRegistry dreadLampRegistry = new TorchRegistry()
    {
        @Override
        protected boolean shouldHandleEntityType(Class<? extends Entity> entityClass)
        {
            return TorchMasterMod.DreadLampFilterRegistry.containsEntity(entityClass);
        }

        @Override
        protected int getTorchRange()
        {
            return TorchmasterConfig.DreadLampRange;
        }

        @Override
        protected boolean isBlockStateValid(IBlockState state)
        {
            return state.getBlock() == ModBlocks.getDreadLamp();
        }
    };

    @Override
    public ITorchRegistry getMegaTorchRegistry()
    {
        return megaTorchRegistry;
    }

    @Override
    public ITorchRegistry getDreadLampRegistry()
    {
        return dreadLampRegistry;
    }

    @Override
    public boolean shouldEntityBeBlocked(Entity entity)
    {
        return megaTorchRegistry.shouldEntityBeBlocked(entity) || dreadLampRegistry.shouldEntityBeBlocked(entity);
    }

    @Override
    public void onGlobalTick(World world)
    {
        megaTorchRegistry.onGlobalTick(world);
        dreadLampRegistry.onGlobalTick(world);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("mega_torch", megaTorchRegistry.serializeNBT());
        nbt.setTag("dread_lamp", dreadLampRegistry.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        if(nbt.hasKey("mega_torch"))
            megaTorchRegistry.deserializeNBT(nbt.getTagList("mega_torch", Constants.NBT.TAG_COMPOUND));
        if(nbt.hasKey("dread_lamp"))
            dreadLampRegistry.deserializeNBT(nbt.getTagList("dread_lamp", Constants.NBT.TAG_COMPOUND));
    }
}
