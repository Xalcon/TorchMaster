package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchmasterConfig;
import net.xalcon.torchmaster.common.ModBlocks;

import javax.annotation.Nullable;
import java.util.List;

public class FrozenPearlItem extends Item
{
    public FrozenPearlItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        BlockPos.Mutable checkPos = new BlockPos.Mutable(0, 0, 0);
        BlockPos pos = new BlockPos(player.getPositionVec());
        ItemStack itemStack = player.getHeldItem(hand);
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.7f, 0.6f, true);
        for(int x = -15; x <= 15; x++)
        {
            for(int y = -15; y <= 15; y++)
            {
                for(int z = -15; z <= 15; z++)
                {
                    checkPos.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if(checkPos.getY() < 1) continue;
                    Block block = world.getBlockState(checkPos).getBlock();
                    if(block == ModBlocks.blockInvisibleLight)
                    {
                        world.removeBlock(checkPos, false);
                        if(this.isDamageable())
                            itemStack.damageItem(1, player, (entity) -> entity.sendBreakAnimation(hand));
                        if(itemStack.isEmpty())
                            return new ActionResult<>(ActionResultType.SUCCESS, ItemStack.EMPTY);
                    }
                }
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        if(TorchmasterConfig.GENERAL.beginnerTooltips.get())
            tooltip.add(new TranslationTextComponent(this.getTranslationKey(stack) + ".tooltip"));
    }
}
