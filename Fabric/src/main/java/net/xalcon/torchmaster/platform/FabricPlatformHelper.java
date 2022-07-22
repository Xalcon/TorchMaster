package net.xalcon.torchmaster.platform;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.logic.entityblocking.IBlockingLightRegistry;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Optional;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Optional<IBlockingLightRegistry> getRegistryForLevel(Level level)
    {
        // TODO: Implement this!
        return Optional.empty();
    }

    private final CreativeModeTab creativeTab = FabricItemGroupBuilder
            .create(Constants.modLoc("creativetab"))
            .icon(() -> new ItemStack(ModRegistry.blockMegaTorch.get()))
            .build();

    @Override
    public CreativeModeTab getCreativeTab()
    {
        return creativeTab;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return FabricBlockEntityTypeBuilder.create(supplier::create, blocks).build(null);
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return new ITorchmasterConfig()
        {
            @Override
            public int getFeralFlareTickRate()
            {
                return 0;
            }

            @Override
            public int getFeralFlareLanternLightCountHardcap()
            {
                return 0;
            }

            @Override
            public int getFeralFlareRadius()
            {
                return 0;
            }

            @Override
            public int getFeralFlareMinLightLevel()
            {
                return 0;
            }

            @Override
            public int getDreadLampRadius()
            {
                return 0;
            }

            @Override
            public int getMegaTorchRadius()
            {
                return 0;
            }

            @Override
            public boolean getLogSpawnChecks()
            {
                return false;
            }

            @Override
            public boolean getAggressiveSpawnChecks()
            {
                return false;
            }

            @Override
            public boolean getBlockOnlyNaturalSpawns()
            {
                return false;
            }

            @Override
            public boolean getBlockVillageSieges()
            {
                return false;
            }
        };
    }
}
