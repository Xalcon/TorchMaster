package net.xalcon.torchmaster;

import net.xalcon.torchmaster.config.ITorchmasterConfig;

import java.util.List;

public class TorchmasterOwOConfigWrapper implements ITorchmasterConfig
{
    public final static ITorchmasterConfig INSTANCE = new TorchmasterOwOConfigWrapper();

    @Override
    public int getFeralFlareTickRate()
    {
        return TorchmasterFabric.CONFIG.feralFlareLanternTickRate();
    }

    @Override
    public int getFeralFlareLanternLightCountHardcap()
    {
        return TorchmasterFabric.CONFIG.feralFlareLanternLightHardcap();
    }

    @Override
    public int getFeralFlareRadius()
    {
        return TorchmasterFabric.CONFIG.feralFlareLanternRadius();
    }

    @Override
    public int getFeralFlareMinLightLevel()
    {
        return TorchmasterFabric.CONFIG.feralFlareLanternMinLightLevel();
    }

    @Override
    public int getDreadLampRadius()
    {
        return TorchmasterFabric.CONFIG.dreadLampRadius();
    }

    @Override
    public int getMegaTorchRadius()
    {
        return TorchmasterFabric.CONFIG.megaTorchRadius();
    }

    @Override
    public boolean getAggressiveSpawnChecks()
    {
        return true; // This is a forge only feature and has no effect on fabric, the result will be the same
    }

    @Override
    public boolean getBlockOnlyNaturalSpawns()
    {
        return TorchmasterFabric.CONFIG.blockOnlyNaturalSpawns();
    }

    @Override
    public boolean getBlockVillageSieges()
    {
        return TorchmasterFabric.CONFIG.blockVillageSieges();
    }

    @Override
    public List<String> getMegaTorchEntityBlockListOverrides()
    {
        return TorchmasterFabric.CONFIG.megaTorchEntityBlockListOverrides();
    }

    @Override
    public List<String> getDreadLampEntityBlockListOverrides()
    {
        return TorchmasterFabric.CONFIG.dreadLampEntityBlockListOverrides();
    }
}
