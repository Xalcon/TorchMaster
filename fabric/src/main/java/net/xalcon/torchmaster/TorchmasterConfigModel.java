package net.xalcon.torchmaster;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

@Modmenu(modId = Constants.MOD_ID)
@Config(name = "torchmaster-config", wrapperName = "TorchmasterConfig")
public class TorchmasterConfigModel
{
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1, max = 100, decimalPlaces = 0)
    public int feralFlareLanternTickRate = 2;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 0, max = 65535, decimalPlaces = 0)
    public int feralFlareLanternLightHardcap = 255;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1, max = 256, decimalPlaces = 0)
    public int feralFlareLanternRadius = 24;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 0, max = 15, decimalPlaces = 0)
    public int feralFlareLanternMinLightLevel = 7;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 0, max = 8192, decimalPlaces = 0)
    public int dreadLampRadius = 64;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 0, max = 8192, decimalPlaces = 0)
    public int megaTorchRadius = 64;
    @RestartRequired
    public boolean shouldLogSpawnChecks = false;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean aggressiveSpawnChecks = false;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean blockOnlyNaturalSpawns = true;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean blockVillageSieges = true;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public List<String> megaTorchEntityBlockListOverrides = new ArrayList<>();
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public List<String> dreadLampEntityBlockListOverrides = new ArrayList<>();
}
