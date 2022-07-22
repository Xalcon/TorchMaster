package net.xalcon.torchmaster.logic.entityblocking.megatorch;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.ILightSerializer;

public class MegatorchSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "megatorch";

    public static final MegatorchSerializer INSTANCE = new MegatorchSerializer();

    private MegatorchSerializer() { }

    @Override
    public CompoundTag serializeLight(String lightKey, IEntityBlockingLight ilight)
    {
        if(ilight == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(ilight instanceof MegatorchEntityBlockingLight))
            throw new IllegalArgumentException("Unable to serialize '" + ilight.getClass().getCanonicalName() + "', expected '"+MegatorchEntityBlockingLight.class.getCanonicalName()+"'");

        MegatorchEntityBlockingLight light = (MegatorchEntityBlockingLight) ilight;

        var nbt = new CompoundTag();
        nbt.put("pos", NbtUtils.writeBlockPos(light.getPos()));

        return nbt;
    }

    @Override
    public IEntityBlockingLight deserializeLight(String lightKey, CompoundTag nbt)
    {
        return new MegatorchEntityBlockingLight(NbtUtils.readBlockPos(nbt.getCompound("pos")));
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
