package net.xalcon.torchmaster.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class GuiItemIconButton extends GuiButton
{
    private boolean locked;
    private ItemStack itemStack;
    private ItemStack disabled = new ItemStack(Blocks.BARRIER);
    private Supplier<Boolean> isEnabled;
    private Minecraft mc;

    public GuiItemIconButton(int buttonId, int x, int y, ItemStack itemStack, Supplier<Boolean> isEnabled)
    {
        super(buttonId, x, y, 20, 20, "");
        this.itemStack = itemStack;
        this.isEnabled = isEnabled;
        this.mc = Minecraft.getInstance();
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
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            super.render(mouseX, mouseY, partialTicks);

            mc.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getItemRenderer().renderItemIntoGUI(this.itemStack, x + 2, y + 2);

            if(!this.isEnabled.get())
                mc.getItemRenderer().renderItemIntoGUI(disabled, x + 2, y + 2);
        }
    }
}
