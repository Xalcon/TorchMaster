package net.xalcon.torchmaster.mixin;

import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// TODO: Port this to current version
// @Mixin(SpawnUtil.class)
public abstract class SpawnUtilMixin
{
    //@Redirect(method = "trySpawnMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"))
    public boolean torchmaster_trySpawnMob_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);
        TorchmasterEventHandler.onCheckSpawn(mobSpawnType, mob, mob.position(), container);
        return switch(container.getResult())
        {
            case DEFAULT -> mob.checkSpawnRules(level, mobSpawnType);
            case ALLOW -> true;
            case DENY -> false;
        };
    }
}
