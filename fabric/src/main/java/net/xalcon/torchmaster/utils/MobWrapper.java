package net.xalcon.torchmaster.utils;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;

public abstract class MobWrapper
{
    public static boolean checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType, Operation<Boolean> original)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        TorchmasterEventHandler.onCheckSpawn(mobSpawnType, mob, mob.position(), container);
        return switch(container.getResult())
        {
            // Make sure we call the origínal and not mob.checkSpawnRules() directly
            // otherwise we skip other mods down the chain (if any)
            // We may still run into compat issues in cases where we deny or explicitly allow the spawn,
            // but I'll look at those when necessary.
            case DEFAULT -> original.call(mob, level, mobSpawnType);
            case ALLOW -> true;
            case DENY -> false;
        };
    }
}
