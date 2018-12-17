package net.xalcon.torchmaster.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLockIconButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.container.ContainerTerrainLighter;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class GuiItemIconButton extends GuiButton
{
    private boolean locked;
    private ItemStack itemStack;
    private ItemStack disabled = new ItemStack(Blocks.BARRIER);
    private Supplier<Boolean> isEnabled;

    public GuiItemIconButton(int buttonId, int x, int y, ItemStack itemStack, Supplier<Boolean> isEnabled)
    {
        super(buttonId, x, y, 20, 20, "");
        this.itemStack = itemStack;
        this.isEnabled = isEnabled;
    }

    public boolean isLocked()
    {
        return this.locked;
    }

    public void setLocked(boolean lockedIn)
    {
        this.locked = lockedIn;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            super.drawButton(mc, mouseX, mouseY, partialTicks);

            mc.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(this.itemStack, x + 2, y + 2);

            if(!this.isEnabled.get())
                Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(disabled, x + 2, y + 2);
        }
    }
}
