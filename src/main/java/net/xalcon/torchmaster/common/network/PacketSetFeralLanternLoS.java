package net.xalcon.torchmaster.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;

public class PacketSetFeralLanternLoS implements IMessage
{
    private boolean state;
    private BlockPos pos;
    private int dim;

    public PacketSetFeralLanternLoS()
    {
    }

    public PacketSetFeralLanternLoS(boolean state, TileEntityFeralFlareLantern tile)
    {
        this.state = state;
        this.pos = tile.getPos();
        this.dim = tile.getWorld().provider.getDimension();
    }


    @Override
    public void fromBytes(ByteBuf buf)
    {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        this.state = packetBuffer.readBoolean();
        this.pos = packetBuffer.readBlockPos();
        this.dim = packetBuffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeBoolean(this.state);
        packetBuffer.writeBlockPos(this.pos);
        packetBuffer.writeInt(this.dim);
    }

    public static IMessageHandler<PacketSetFeralLanternLoS, IMessage> Handler = (message, ctx) ->
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
        {
            World world = DimensionManager.getWorld(message.dim);
            if(world != null)
            {
                TileEntity te = world.getTileEntity(message.pos);
                if(!(te instanceof TileEntityFeralFlareLantern))
                {
                    TorchMasterMod.Log.error("Unable to find TileEntityFeralFlareLantern at {} in dim {}!", message.pos, message.dim);
                    return;
                }

                TileEntityFeralFlareLantern lantern = (TileEntityFeralFlareLantern) te;
                lantern.setUseLineOfSight(message.state);
            }
        });
        return null;
    };
}
