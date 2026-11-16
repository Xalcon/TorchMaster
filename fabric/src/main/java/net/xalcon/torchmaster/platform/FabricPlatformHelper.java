package net.xalcon.torchmaster.platform;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.TorchmasterOwOConfigWrapper;
import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.platform.services.IPlatformHelper;

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
        return FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(ModRegistry.itemMegaTorch.get()))
                .displayItems((parameters, output) -> itemsToShow.forEach(itemRef -> output.accept(new ItemStack(itemRef.get()))))
                .build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> supplier, Block... blocks)
    {
        return FabricBlockEntityTypeBuilder.create(supplier::create, blocks).build();
        // return new BlockEntityType(supplier::create, blocks).build(null);
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return TorchmasterOwOConfigWrapper.INSTANCE;
    }
}
