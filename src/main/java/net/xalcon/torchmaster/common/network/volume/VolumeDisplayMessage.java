package net.xalcon.torchmaster.common.network.volume;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VolumeDisplayMessage
{
    private Vec3i position;
    private int range;
    private int color;

    private boolean showVolume;
    private boolean showLocation;

    public static void encode(VolumeDisplayMessage msg, FriendlyByteBuf buffer)
    {
        buffer.writeInt(msg.position.getX());
        buffer.writeInt(msg.position.getY());
        buffer.writeInt(msg.position.getZ());
        buffer.writeInt(msg.range);
        buffer.writeInt(msg.color);

        buffer.writeBoolean(msg.showVolume);
        buffer.writeBoolean(msg.showLocation);
    }

    public static VolumeDisplayMessage decode(FriendlyByteBuf buffer)
    {
        var msg = new VolumeDisplayMessage();
        var x = buffer.readInt();
        var y = buffer.readInt();
        var z = buffer.readInt();
        msg.position = new Vec3i(x, y, z);
        msg.range = buffer.readInt();
        msg.color = buffer.readInt();

        msg.showVolume = buffer.readBoolean();
        msg.showLocation = buffer.readBoolean();
        return msg;
    }

    public static void dispatch(VolumeDisplayMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> VolumeDisplayMessageHandler.handle(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }

    public static VolumeDisplayMessage create(Vec3i pos, int range, int color, boolean showVolume, boolean showLocation)
    {
        var msg = new VolumeDisplayMessage();
        msg.position = pos;
        msg.range = range;
        msg.color = color;
        msg.showVolume = showVolume;
        msg.showLocation = showLocation;
        return msg;
    }

    public Vec3i getPosition()
    {
        return position;
    }

    public int getColor()
    {
        return color;
    }

    public int getRange()
    {
        return range;
    }

    public boolean showVolume()
    {
        return showVolume;
    }

    public boolean showLocation()
    {
        return showLocation;
    }
}
