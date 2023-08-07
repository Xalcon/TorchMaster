package net.xalcon.torchmaster.common.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;

public class MegatorchTileEntity extends BlockEntity
{
    private int fuelLevel;
    private boolean isEnabled;

    public int getFuelLevel()
    {
        return this.fuelLevel;
    }

    public boolean isEnabled()
    {
        return this.isEnabled;
    }

    public MegatorchTileEntity(BlockPos pos, BlockState state)
    {
        super(ModBlocks.tileMegaTorch, pos, state);
    }

    public static <T extends BlockEntity> void dispatchTickBlockEntity(Level level, BlockPos pos, BlockState state, T blockEntity)
    {
        if(blockEntity instanceof MegatorchTileEntity)
            ((MegatorchTileEntity)blockEntity).tick();
    }

    private void tick()
    {
        if(level == null || level.isClientSide)
            return;

        this.fuelLevel--;
        if(this.fuelLevel <= 0)
            this.fuelLevel = 0;

        if(this.fuelLevel > 0 && this.fuelLevel % TorchmasterConfig.GENERAL.megatorchBurnoutDataRefreshRate.get() == 0)
        {
            var state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
        }

        if(this.fuelLevel == 0 && this.isEnabled)
        {
            // TODO: replace with burned out torch state
            this.isEnabled = false;
        }

        if(this.fuelLevel > 0 && !this.isEnabled)
        {
            // TODO: replace with active torch state
            this.isEnabled = true;
        }
    }

    public boolean refuel(Player player, ItemStack itemStack)
    {
        var itemKey = itemStack.getItem().getRegistryName();
        if(TorchmasterConfig.GENERAL.fuelMap.containsKey(itemKey))
        {
            var fuel = TorchmasterConfig.GENERAL.fuelMap.get(itemKey);
            this.fuelLevel += fuel;
        }
        return true;
    }
}
