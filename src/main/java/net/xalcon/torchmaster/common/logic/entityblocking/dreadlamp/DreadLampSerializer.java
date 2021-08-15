package net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.ILightSerializer;

public class DreadLampSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "dreadlamp";
    public static final DreadLampSerializer INSTANCE = new DreadLampSerializer();

    private DreadLampSerializer() { }

    @Override
    public CompoundTag serializeLight(String lightKey, IEntityBlockingLight ilight)
    {
        if(ilight == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(ilight instanceof DreadLampEntityBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + ilight.getClass().getCanonicalName() + "', expected '"+ DreadLampEntityBlockingLight.class.getCanonicalName() +"'");

        DreadLampEntityBlockingLight light = (DreadLampEntityBlockingLight) ilight;

        var nbt = new CompoundTag();
        nbt.put("pos", NbtUtils.writeBlockPos(light.getPos()));

        return nbt;
    }

    @Override
    public IEntityBlockingLight deserializeLight(String lightKey, CompoundTag nbt)
    {
        return new DreadLampEntityBlockingLight(NbtUtils.readBlockPos(nbt.getCompound("pos")));
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
