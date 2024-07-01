package net.xalcon.torchmaster.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

    @Redirect(method = "isValidPositionForMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"))
    private static boolean torchmaster_isValidPositionForMob_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType)
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

    @Redirect(method = "spawnMobsForChunkGeneration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"))
    private static boolean torchmaster_spawnMobsForChunkGeneration_checkSpawnRules(Mob mob, LevelAccessor level, MobSpawnType mobSpawnType)
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