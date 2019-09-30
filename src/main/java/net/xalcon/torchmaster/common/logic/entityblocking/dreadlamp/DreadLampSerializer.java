package net.xalcon.torchmaster.common.logic.entityblocking.dreadlamp;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.ILightSerializer;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.megatorch.MegatorchSerializer;

public class DreadLampSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "dreadlamp";
    public static final DreadLampSerializer INSTANCE = new DreadLampSerializer();

    private DreadLampSerializer() { }

    @Override
    public CompoundNBT serializeLight(String lightKey, IEntityBlockingLight ilight)
    {
        if(ilight == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(ilight instanceof DreadLampEntityBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + ilight.getClass().getCanonicalName() + "', expected '"+ DreadLampEntityBlockingLight.class.getCanonicalName() +"'");

        DreadLampEntityBlockingLight light = (DreadLampEntityBlockingLight) ilight;

        CompoundNBT nbt = new CompoundNBT();
        nbt.put("pos", NBTUtil.writeBlockPos(light.getPos()));

        return nbt;
    }

    @Override
    public IEntityBlockingLight deserializeLight(String lightKey, CompoundNBT nbt)
    {
        return new DreadLampEntityBlockingLight(NBTUtil.readBlockPos(nbt.getCompound("pos")));
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
