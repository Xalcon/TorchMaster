package net.xalcon.torchmaster.logic.entityblocking.dreadlamp;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.xalcon.torchmaster.logic.entityblocking.IEntityBlockingLight;
import net.xalcon.torchmaster.logic.entityblocking.ILightSerializer;

import java.util.Optional;

public class DreadLampSerializer implements ILightSerializer
{
    public static final String SERIALIZER_KEY = "dreadlamp";
    public static final DreadLampSerializer INSTANCE = new DreadLampSerializer();

    private DreadLampSerializer() { }

    @Override
    public CompoundTag serializeLight(IEntityBlockingLight light)
    {
        if(light == null)
            throw new IllegalArgumentException("Unable to serialize null");

        if(!(light instanceof DreadLampEntityBlockingLight dreadLampLight))
            throw new IllegalArgumentException("Unable to serialize '" + light.getClass().getCanonicalName() + "', expected '"+ DreadLampEntityBlockingLight.class.getCanonicalName() +"'");

        var nbt = new CompoundTag();
        nbt.put("pos", NbtUtils.writeBlockPos(dreadLampLight.getPos()));

        return nbt;
    }

    @Override
    public Optional<IEntityBlockingLight> deserializeLight(CompoundTag nbt)
    {
        var pos = NbtUtils.readBlockPos(nbt, "pos");
        return pos.map(DreadLampEntityBlockingLight::new);
    }

    @Override
    public String getSerializerKey()
    {
        return SERIALIZER_KEY;
    }
}
