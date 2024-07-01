package net.xalcon.torchmaster.logic.entityblocking.megatorch;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.ILightSerializer;

import java.util.Optional;

public class MegatorchSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "megatorch";

    public static final MegatorchSerializer INSTANCE = new MegatorchSerializer();

    private MegatorchSerializer() { }

    @Override
    public CompoundTag serializeLight(IEntityBlockingLight light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof MegatorchEntityBlockingLight megatorchLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+MegatorchEntityBlockingLight.class.getCanonicalName()+"'");

        var nbt = new CompoundTag();
        nbt.put("pos", NbtUtils.writeBlockPos(megatorchLight.getPos()));

        return nbt;
    }

    @Override
    public Optional<IEntityBlockingLight> deserializeLight(CompoundTag nbt)
    {
        var pos = NbtUtils.readBlockPos(nbt, "pos");
        return pos.map(MegatorchEntityBlockingLight::new);
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
