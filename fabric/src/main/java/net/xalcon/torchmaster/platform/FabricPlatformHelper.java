package net.xalcon.torchmaster.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Collection;

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
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(ModRegistry.itemMegaTorch.get()))
                .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return BlockEntityType.Builder.of(supplier::create, blocks).build(null);
    }

    @Override
    public ITorchmasterConfig getConfig()
    {

        return new ITorchmasterConfig()
        {
            @Override
            public int getFeralFlareTickRate()
            {
                return 5;
            }

            @Override
            public int getFeralFlareLanternLightCountHardcap()
            {
                return 255;
            }

            @Override
            public int getFeralFlareRadius()
            {
                return 32;
            }

            @Override
            public int getFeralFlareMinLightLevel()
            {
                return 7;
            }

            @Override
            public int getDreadLampRadius()
            {
                return 64;
            }

            @Override
            public int getMegaTorchRadius()
            {
                return 64;
            }

            @Override
            public boolean shouldLogSpawnChecks()
            {
                return true;
            }

            @Override
            public boolean getAggressiveSpawnChecks()
            {
                return false;
            }

            @Override
            public boolean getBlockOnlyNaturalSpawns()
            {
                return true;
            }

            @Override
            public boolean getBlockVillageSieges()
            {
                return true;
            }
        };
    }
}
