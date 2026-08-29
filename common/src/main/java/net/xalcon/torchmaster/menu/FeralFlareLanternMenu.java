package net.xalcon.torchmaster.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.blocks.FeralFlareLanternBlockEntity;

public class FeralFlareLanternMenu extends AbstractContainerMenu
{
    public static final int TOGGLE_LINE_OF_SIGHT = 0;

    private static final int DATA_USE_LINE_OF_SIGHT = 0;
    private static final int DATA_COUNT = 1;

    private final ContainerLevelAccess access;
    private final ContainerData data;

    public FeralFlareLanternMenu(int containerId, Inventory playerInventory)
    {
        this(containerId, playerInventory, ContainerLevelAccess.NULL, new SimpleContainerData(DATA_COUNT));
    }

    public FeralFlareLanternMenu(int containerId, Inventory playerInventory, FeralFlareLanternBlockEntity lantern)
    {
        this(containerId, playerInventory, ContainerLevelAccess.create(lantern.getLevel(), lantern.getBlockPos()),
                new ContainerData()
                {
                    @Override
                    public int get(int index)
                    {
                        return index == DATA_USE_LINE_OF_SIGHT && lantern.shouldUseLineOfSight() ? 1 : 0;
                    }

                    @Override
                    public void set(int index, int value)
                    {
                    }

                    @Override
                    public int getCount()
                    {
                        return DATA_COUNT;
                    }
                });
    }

    private FeralFlareLanternMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access, ContainerData data)
    {
        super(ModRegistry.menuFeralFlareLantern.get(), containerId);
        this.access = access;
        this.data = data;
        addDataSlots(data);
    }

    public boolean isUsingLineOfSight()
    {
        return data.get(DATA_USE_LINE_OF_SIGHT) != 0;
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId)
    {
        if(buttonId != TOGGLE_LINE_OF_SIGHT || !stillValid(player))
            return false;

        return access.evaluate((level, pos) ->
        {
            if(!(level.getBlockEntity(pos) instanceof FeralFlareLanternBlockEntity lantern))
                return false;

            lantern.setUseLineOfSight(!lantern.shouldUseLineOfSight());
            return true;
        }, false);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return access.evaluate((level, pos) ->
                        level.getBlockState(pos).is(ModRegistry.blockFeralFlareLantern.get())
                                && player.distanceToSqr(pos.getCenter()) <= 64.0D,
                false);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        return ItemStack.EMPTY;
    }
}
