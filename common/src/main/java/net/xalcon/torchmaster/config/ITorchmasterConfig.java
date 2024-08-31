package net.xalcon.torchmaster.config;

import java.util.List;

public interface ITorchmasterConfig
{
    int getFeralFlareTickRate();

    int getFeralFlareLanternLightCountHardcap();

    int getFeralFlareRadius();

    int getFeralFlareMinLightLevel();

    int getDreadLampRadius();

    int getMegaTorchRadius();

    boolean getAggressiveSpawnChecks();

    boolean getBlockOnlyNaturalSpawns();

    boolean getBlockVillageSieges();

    List<String> getMegaTorchEntityBlockListOverrides();
    List<String> getDreadLampEntityBlockListOverrides();
}
