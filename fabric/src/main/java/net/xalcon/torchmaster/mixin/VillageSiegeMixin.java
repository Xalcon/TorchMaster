package net.xalcon.torchmaster.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * This Mixin provides the onVillageSiege event implementation
 * It hooks the invocation of findRandomSpawnPos() from inside tryToSetupSiege()
 * if findRandomSpawnPos() returns a non-null result, a siege is about to happen
 *
 * This code is untested - how the fuck to we force a village siege?!
 */
@Mixin(VillageSiege.class)
public abstract class VillageSiegeMixin
{
    @WrapOperation(
            method = "tryToSetupSiege(Lnet/minecraft/server/level/ServerLevel;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/village/VillageSiege;findRandomSpawnPos(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private Vec3 torchmaster_tryToSetupSiege_findRandomSpawnPos(VillageSiege siege, ServerLevel level, BlockPos pos, Operation<Vec3> original)
    {
        var result = original.call(siege, level, pos);
        if(result != null)
        {
            var container = new EventResultContainer(EventResult.DEFAULT);
            TorchmasterEventHandler.onVillageSiege(level, result, container);
            if(container.getResult() == EventResult.DENY)
                return null;
        }
        return  result;
    }
}
