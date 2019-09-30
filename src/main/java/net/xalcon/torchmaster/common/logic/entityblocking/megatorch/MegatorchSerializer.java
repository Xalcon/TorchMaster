package net.xalcon.torchmaster.common.logic.entityblocking.megatorch;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.xalcon.torchmaster.common.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.common.logic.entityblocking.ILightSerializer;

public class MegatorchSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "megatorch";

    public static final MegatorchSerializer INSTANCE = new MegatorchSerializer();

    private MegatorchSerializer() { }

    @Override
    public CompoundNBT serializeLight(String lightKey, IEntityBlockingLight ilight)
    {
        if(ilight == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(ilight instanceof MegatorchEntityBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + ilight.getClass().getCanonicalName() + "', expected '"+MegatorchEntityBlockingLight.class.getCanonicalName()+"'");

        MegatorchEntityBlockingLight light = (MegatorchEntityBlockingLight) ilight;

        CompoundNBT nbt = new CompoundNBT();
        nbt.put("pos", NBTUtil.writeBlockPos(light.getPos()));

        return nbt;
    }

    @Override
    public IEntityBlockingLight deserializeLight(String lightKey, CompoundNBT nbt)
    {
        return new MegatorchEntityBlockingLight(NBTUtil.readBlockPos(nbt.getCompound("pos")));
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
