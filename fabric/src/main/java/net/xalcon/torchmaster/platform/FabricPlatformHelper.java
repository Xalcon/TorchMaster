package net.xalcon.torchmaster.platform;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.TorchmasterOwOConfigWrapper;
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
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuSupplier<T> supplier)
    {
        return new ExtendedScreenHandlerType<>(
                (containerId, inventory, data) -> supplier.create(containerId, inventory),
                StreamCodec.unit(Unit.INSTANCE));
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider)
    {
        player.openMenu(new ExtendedScreenHandlerFactory<Unit>()
        {
            @Override
            public Component getDisplayName()
            {
                return provider.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inventory, net.minecraft.world.entity.player.Player menuPlayer)
            {
                return provider.createMenu(containerId, inventory, menuPlayer);
            }

            @Override
            public Unit getScreenOpeningData(ServerPlayer serverPlayer)
            {
                return Unit.INSTANCE;
            }
        });
    }

    @Override
    public ITorchmasterConfig getConfig()
    {
        return TorchmasterOwOConfigWrapper.INSTANCE;
    }
}
