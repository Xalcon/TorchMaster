package net.xalcon.torchmaster;

import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.Logger;

public class LogHax
{
    // Minecraft uses slf4j as a logger abstraction
    // and at the time of writing, the implementation is log4j2
    // Due to this abstraction, it is not directly possible to
    // filter out debug logging
    // This method enables debug logging only for the Torchmaster logger,
    // leaving everything else as is

    public static void enableDebugLogging(Logger logger)
    {
        try
        {
            var loggerField = Log4jLogger.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            var internalLogger = loggerField.get(logger);

            if(internalLogger instanceof org.apache.logging.log4j.core.Logger log4jLogger)
            {
                if(log4jLogger.getLevel() != org.apache.logging.log4j.Level.DEBUG)
                {
                    log4jLogger.setLevel(org.apache.logging.log4j.Level.DEBUG);
                }
            }
        }
        catch (Exception ignored) { }
    }
}
