package net.xalcon.torchmaster.logic.blockers;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.logic.EntityBlocker;

public class MegaTorchEntityBlocker implements EntityBlocker
{
    private static final ResourceLocation TYPE = ResourceLocation.tryBuild(Constants.MOD_ID, "megatorch");

    private final String identifier;
    private int radius;

    public MegaTorchEntityBlocker(String identifier)
    {
        this.identifier = identifier;
    }

    public static MegaTorchEntityBlocker from(Level level, BlockPos pos)

    @Override
    public String getIdentifier()
    {
        return identifier;
    }

    @Override
    public ResourceLocation getType()
    {
        return TYPE;
    }

    @Override
    public boolean shouldBlockEntitySpawn(Entity entity, Level level, MobSpawnType spawnType)
    {
        return false;
    }

    @Override
    public boolean shouldBlockVillageSiege(Level level, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean shouldBlockVillageRaid(Level level, BlockPos pos)
    {
        return false;
    }
}
