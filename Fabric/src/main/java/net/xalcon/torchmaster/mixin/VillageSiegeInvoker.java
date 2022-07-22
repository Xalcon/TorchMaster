package net.xalcon.torchmaster.mixin;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VillageSiege.class)
interface VillageSiegeInvoker
{
    @Invoker("findRandomSpawnPos")
    Vec3 torchmaster_findRandomSpawnPos(ServerLevel serverLevel, BlockPos blockPos);
}