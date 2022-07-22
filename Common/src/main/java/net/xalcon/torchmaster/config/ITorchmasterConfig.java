package net.xalcon.torchmaster.config;

public interface ITorchmasterConfig
{
    int getFeralFlareTickRate();

    int getFeralFlareLanternLightCountHardcap();

    int getFeralFlareRadius();

    int getFeralFlareMinLightLevel();

    int getDreadLampRadius();

    int getMegaTorchRadius();

    boolean getLogSpawnChecks();

    boolean getAggressiveSpawnChecks();

    boolean getBlockOnlyNaturalSpawns();

    boolean getBlockVillageSieges();
}
