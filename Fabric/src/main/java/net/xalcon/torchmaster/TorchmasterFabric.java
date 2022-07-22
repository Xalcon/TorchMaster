package net.xalcon.torchmaster;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.xalcon.torchmaster.compat.VanillaCompat;

public class TorchmasterFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        Torchmaster.init();

        ServerLifecycleEvents.SERVER_STARTING.register(server ->
        {
            VanillaCompat.registerTorchEntities(Torchmaster.MegaTorchFilterRegistry);
            VanillaCompat.registerDreadLampEntities(Torchmaster.DreadLampFilterRegistry);
        });
    }
}
