package net.xalcon.torchmaster.utils;

import net.minecraftforge.eventbus.api.Event;
import net.xalcon.torchmaster.events.EventResult;
import net.xalcon.torchmaster.events.EventResultContainer;

public class EventUtils
{
    public static EventResultContainer asContainer(Event event)
    {
        return new EventResultContainer(switch(event.getResult())
        {
            case DEFAULT -> EventResult.DEFAULT;
            case ALLOW -> EventResult.ALLOW;
            case DENY -> EventResult.DENY;
        });
    }

    public static void setEventResult(Event event, EventResultContainer container)
    {
        event.setResult(switch(container.getResult())
        {
            case DEFAULT -> Event.Result.DEFAULT;
            case ALLOW -> Event.Result.ALLOW;
            case DENY -> Event.Result.DENY;
        });
    }
}
