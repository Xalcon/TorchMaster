package net.xalcon.torchmaster.compat;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.logic.ITorchRegistryContainer;

import java.io.File;
import java.io.IOException;

// TODO: REMOVE THIS IN A LATER RELEASE!
public class RegistryBackwardsCompat
{
    private static class TorchLocation
    {
        public int DimensionId;
        public BlockPos Position;

        public TorchLocation(int dimensionId, BlockPos position)
        {
            DimensionId = dimensionId;
            Position = position;
        }

        public TorchLocation(NBTTagCompound entry)
        {
            this.DimensionId = entry.getInteger("d");
            this.Position = new BlockPos(entry.getInteger("x"), entry.getInteger("y"), entry.getInteger("z"));
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TorchLocation that = (TorchLocation) o;

            if (DimensionId != that.DimensionId) return false;
            return Position != null ? Position.equals(that.Position) : that.Position == null;
        }

        @Override
        public int hashCode()
        {
            int result = DimensionId;
            result = 31 * result + (Position != null ? Position.hashCode() : 0);
            return result;
        }

        public NBTTagCompound toNbt()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("d", this.DimensionId);
            nbt.setInteger("x", this.Position.getX());
            nbt.setInteger("y", this.Position.getY());
            nbt.setInteger("z", this.Position.getZ());
            return nbt;
        }

        @Override
        public String toString()
        {
            return String.format("DIM: %d - %s", this.DimensionId, this.Position);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if(event.getWorld() == null)
        {
            // the world is null, for some reason. This seem to happen in only a few rare cases
            TorchMasterMod.Log.warn("Torchmaster retrieved the WorldEvent.Load but the world object is null! Queueing reload");
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(this::reloadRegistry);
            return;
        }

        if(event.getWorld().provider.getDimension() != 0) return;
        reloadRegistry();
    }

    private void reloadRegistry()
    {
        loadMegaTorchRegistry();
        loadDreadLampRegistry();
    }

    private void loadMegaTorchRegistry()
    {
        File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "data/torchmaster_mega_torch_reg.dat");
        if(!file.exists())
            return;
        NBTTagCompound nbt;

        try
        {
            nbt = CompressedStreamTools.read(file);
            if(nbt == null) return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        NBTTagList list = nbt.getTagList("list", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            TorchLocation loc = new TorchLocation(entry);
            World world = DimensionManager.getWorld(loc.DimensionId);
            if(world == null)
            {
                TorchMasterMod.Log.warn("Unable to restore torch @ " + loc + ", the world " + loc.DimensionId + " was not found");
                continue;
            }

            ITorchRegistryContainer container = world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
            if(container != null)
                container.getMegaTorchRegistry().register(loc.Position);
        }
        file.delete();
    }

    private void loadDreadLampRegistry()
    {
        File file = new File(DimensionManager.getCurrentSaveRootDirectory(), "data/torchmaster_dread_lamp_reg.dat");
        if(!file.exists())
            return;

        NBTTagCompound nbt;

        try
        {
            nbt = CompressedStreamTools.read(file);
            if(nbt == null) return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        NBTTagList list = nbt.getTagList("list", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            TorchLocation loc = new TorchLocation(entry);
            World world = DimensionManager.getWorld(loc.DimensionId);
            if(world == null)
            {
                TorchMasterMod.Log.warn("Unable to restore torch @ " + loc + ", the world " + loc.DimensionId + " was not found");
                continue;
            }

            ITorchRegistryContainer container = world.getCapability(ModCaps.TORCH_REGISTRY_CONTAINER, null);
            if(container != null)
                container.getDreadLampRegistry().register(loc.Position);
        }
        file.delete();
    }
}
