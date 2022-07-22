package net.xalcon.torchmaster.platform;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModCaps;
import net.xalcon.torchmaster.common.TorchmasterCreativeTab;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.logic.entityblocking.IBlockingLightRegistry;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

import java.util.Optional;

public class ForgePlatformHelper implements IPlatformHelper
{

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public Optional<IBlockingLightRegistry> getRegistryForLevel(Level level)
    {
        return level.getCapability(ModCaps.TEB_REGISTRY).resolve();
    }

    @Override
    public CreativeModeTab getCreativeTab()
    {
        return TorchmasterCreativeTab.INSTANCE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.of(supplier::create, blocks).build(null);
    }



    @Override
    public ITorchmasterConfig getConfig()
    {
        return TorchmasterConfig.ConfigWrapper;
    }
}
