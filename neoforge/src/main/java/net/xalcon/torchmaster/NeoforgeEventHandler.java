package net.xalcon.torchmaster;

import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.village.VillageSiegeEvent;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;
import net.xalcon.torchmaster.events.TorchmasterEventHandler;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoforgeEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCheckSpawn(MobSpawnEvent.PositionCheck event)
    {
        var container = new EventResultContainer(switch(event.getResult())
        {
            case DEFAULT -> EventResult.DEFAULT;
            case SUCCEED -> EventResult.ALLOW;
            case FAIL -> EventResult.DENY;
        });

        var spawnType = event.getSpawnType();
        var entity = event.getEntity();
        var pos = new Vec3(event.getX(), event.getY(), event.getZ());
        TorchmasterEventHandler.onCheckSpawn(spawnType, entity, pos, container);

        event.setResult(switch(container.getResult())
        {
            case DEFAULT -> MobSpawnEvent.PositionCheck.Result.DEFAULT;
            case ALLOW -> MobSpawnEvent.PositionCheck.Result.SUCCEED;
            case DENY -> MobSpawnEvent.PositionCheck.Result.FAIL;
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDoSpecialSpawn(MobSpawnEvent.PositionCheck event)
    {
        var container = new EventResultContainer(switch(event.getResult())
        {
            case DEFAULT -> EventResult.DEFAULT;
            case SUCCEED -> EventResult.ALLOW;
            case FAIL -> EventResult.DENY;
        });

        var spawnType = event.getSpawnType();
        var entity = event.getEntity();
        var pos = new Vec3(event.getX(), event.getY(), event.getZ());
        TorchmasterEventHandler.onCheckSpawn(spawnType, entity, pos, container);

        event.setResult(switch(container.getResult())
        {
            case DEFAULT -> MobSpawnEvent.PositionCheck.Result.DEFAULT;
            case ALLOW -> MobSpawnEvent.PositionCheck.Result.SUCCEED;
            case DENY -> MobSpawnEvent.PositionCheck.Result.FAIL;
        });
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onVillageSiegeEvent(VillageSiegeEvent event)
    {
        var container = new EventResultContainer(EventResult.DEFAULT);

        TorchmasterEventHandler.onVillageSiege(event.getLevel(), event.getAttemptedSpawnPos(), container);

        if(container.getResult() == EventResult.DENY)
            event.setCanceled(true);
    }
}
