package net.xalcon.torchmaster.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin
{

    // Mob#checkSpawnRules is called just before spawning the entity
    // This method is called twice in the NaturalSpawner class
    // - isValidPositionForMob, which is used during the natural spawning cycle of an entity (i.e. low light)
    // - spawnMobsForChunkGeneration, which is used during chunk generation
    // In both methods we hook the invocation of onCheckSpawn to check the spawn type and entity against our lists
    // If a mod spawns an entity differently, this will not be detected
    // Hooking Mob#checkSpawnRules is technically possible, but if a mod entity overrides the method
    // without calling base, our hook will not be executed
    // If this happens, we will need to work something out

    @WrapOperation(
            method = "net/minecraft/world/level/NaturalSpawner.isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"
            )
    )
    private static boolean torchmaster_isValidPositionForMob_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType, Operation<Boolean> original)
    {
        return mob_checkSpawnRules(mob, level, mobSpawnType, original);
    }

    @WrapOperation(
            method = "spawnMobsForChunkGeneration(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/util/RandomSource;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"
            )
    )
    private static boolean torchmaster_spawnMobsForChunkGeneration_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType, Operation<Boolean> original)
    {
        return mob_checkSpawnRules(mob, level, mobSpawnType, original);
    }

    private static boolean mob_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType, Operation<Boolean> original)
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