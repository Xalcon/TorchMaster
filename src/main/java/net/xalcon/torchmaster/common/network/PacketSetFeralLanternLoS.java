package net.xalcon.torchmaster.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;

import java.util.function.Supplier;

public class PacketSetFeralLanternLoS
{
    private boolean state;
    private BlockPos pos;
    private int dim;

    private PacketSetFeralLanternLoS() { }

    public PacketSetFeralLanternLoS(boolean state, TileEntityFeralFlareLantern tile)
    {
        this.state = state;
        this.pos = tile.getPos();
        this.dim = tile.getWorld().getDimension().getId();
    }

    public static class Handler
    {
        public static void Register(SimpleChannel channel, int index)
        {
            channel.messageBuilder(PacketSetFeralLanternLoS.class, index)
                    .encoder(Handler::encode)
                    .decoder(Handler::decode)
                    .consumer(Handler::consume);
        }

        private static void encode(PacketSetFeralLanternLoS p, PacketBuffer packetBuffer)
        {
            packetBuffer.writeBoolean(p.state);
            packetBuffer.writeBlockPos(p.pos);
            packetBuffer.writeInt(p.dim);
        }

        private static PacketSetFeralLanternLoS decode(PacketBuffer packetBuffer)
        {
            PacketSetFeralLanternLoS p = new PacketSetFeralLanternLoS();
            p.state = packetBuffer.readBoolean();
            p.pos = packetBuffer.readBlockPos();
            p.dim = packetBuffer.readInt();
            return p;
        }

        private static void consume(PacketSetFeralLanternLoS test, Supplier<NetworkEvent.Context> contextSupplier)
        {
            // TODO: Scheduled Task stuff
            /*FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
            {
                World world = DimensionManager.getWorld(test.dim);
                if(world != null)
                {
                    TileEntity te = world.getTileEntity(test.pos);
                    if(!(te instanceof TileEntityFeralFlareLantern))
                    {
                        TorchMasterMod.Log.error("Unable to find TileEntityFeralFlareLantern at {} in dim {}!", test.pos, test.dim);
                        return;
                    }

                    TileEntityFeralFlareLantern lantern = (TileEntityFeralFlareLantern) te;
                    lantern.setUseLineOfSight(test.state);
                }
            });*/
        }
    }
}
