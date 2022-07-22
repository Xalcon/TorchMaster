package net.xalcon.torchmaster.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.phys.Vec3;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * This Mixin provides the onVillageSiege event implementation
 * It hooks the invocation of findRandomSpawnPos() from inside tryToSetupSiege()
 * if findRandomSpawnPos() returns a non-null result, a siege is about to happen
 *
 * This code is untested - how the fuck to we force a village siege?!
 */
@Mixin(VillageSiege.class)
public class VillageSiegeMixin
{
    @Redirect(method = "tryToSetupSiege", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/village/VillageSiege;findRandomSpawnPos(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 torchmaster_tryToSetupSiege_invoke_findRandomSpawnPos
            (VillageSiege siege, ServerLevel level, BlockPos pos)
    {
        var result = ((VillageSiegeInvoker)siege).torchmaster_findRandomSpawnPos(level, pos);
        if(result != null)
        {
            var container = new EventResultContainer(EventResult.DEFAULT);
            Torchmaster.EventHandler.onVillageSiege(level, result, container);
            if(container.getResult() == EventResult.DENY)
                return null;
        }
        return  result;
    }
}
