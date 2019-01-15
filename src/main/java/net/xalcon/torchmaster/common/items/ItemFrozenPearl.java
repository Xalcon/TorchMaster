package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.xalcon.torchmaster.common.init.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.itemgroups.ItemGroupTorchMaster;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrozenPearl extends Item
{
    public ItemFrozenPearl()
    {
        super(new Builder()
                .defaultMaxDamage(TorchmasterConfig.frozenPearlDurability)
                .group(ItemGroupTorchMaster.INSTANCE));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(0, 0, 0);
        BlockPos pos = player.getPosition();
        ItemStack itemStack = player.getHeldItem(hand);
        worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.7f, 0.6f, true);
        for(int x = -15; x <= 15; x++)
        {
            for(int y = -15; y <= 15; y++)
            {
                for(int z = -15; z <= 15; z++)
                {
                    checkPos.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if(checkPos.getY() < 1) continue;
                    Block block = worldIn.getBlockState(checkPos).getBlock();
                    if(block == ModBlocks.InvisibleLight)
                    {
                        worldIn.removeBlock(checkPos);
                        if(this.isDamageable())
                            itemStack.damageItem(1, player);
                        if(itemStack.isEmpty())
                            return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(new TextComponentTranslation(this.getTranslationKey(stack) + ".tooltip"));
    }
}
