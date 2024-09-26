package net.xalcon.torchmaster.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.xalcon.torchmaster.utils.MobWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpawnUtil.class)
public abstract class SpawnUtilMixin
{
    @WrapOperation(
            method = "trySpawnMob",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"
            )
    )
    private static boolean torchmaster_trySpawnMob_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType, Operation<Boolean> original)
    {
        return MobWrapper.checkSpawnRules(mob, level, mobSpawnType, original);
    }
}
