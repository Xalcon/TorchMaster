package net.xalcon.torchmaster.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
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
import net.minecraft.world.World;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import net.xalcon.torchmaster.common.creativetabs.CreativeTabTorchMaster;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrozenPearl extends Item
{
    public ItemFrozenPearl()
    {
        this.setMaxDamage(TorchmasterConfig.frozenPearlDurability);
        this.setCreativeTab(CreativeTabTorchMaster.INSTANCE);
        this.setTranslationKey(TorchMasterMod.MODID + ".frozen_pearl");
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
                    if(block == ModBlocks.getInvisibleLight())
                    {
                        worldIn.setBlockToAir(checkPos);
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(I18n.format(this.getTranslationKey(stack) + ".tooltip"));
    }
}
