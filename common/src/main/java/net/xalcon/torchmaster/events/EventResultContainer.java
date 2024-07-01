package net.xalcon.torchmaster.events;

public class EventResultContainer
{
    private EventResult result;

    public EventResultContainer(EventResult result)
    {
        this.result = result;
    }

    public EventResult getResult()
    {
        return result;
    }

    public void setResult(EventResult result)
    {
        this.result = result;
    }
}
