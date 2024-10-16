package net.xalcon.torchmaster.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.TorchmasterNeoforgeConfig;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.util.Collection;
import java.util.List;

public class NeoForgePlatformHelper implements IPlatformHelper
{

    @Override
    public String getPlatformName() {

        return "NeoForge";
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
    public CreativeModeTab createCreativeModeTab(String name, Collection<RegistryObject<Item>> itemsToShow)
    {
        return CreativeModeTab.builder()
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
        return TorchmasterNeoforgeConfig.WRAPPED_CONFIG;
    }
}