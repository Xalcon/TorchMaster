package net.xalcon.torchmaster.client.gui;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.menu.FeralFlareLanternMenu;

import java.util.List;

public class FeralFlareLanternScreen extends AbstractContainerScreen<FeralFlareLanternMenu>
{
    private static final ResourceLocation PANEL_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/panel.png");
    private static final int PANEL_WIDTH = 176;
    private static final int PANEL_HEIGHT = 90;
    private static final int EYE_BUTTON_SIZE = 24;

    private EyeOfEnderButton lineOfSightButton;

    public FeralFlareLanternScreen(FeralFlareLanternMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
        imageWidth = PANEL_WIDTH;
        imageHeight = PANEL_HEIGHT;
    }

    @Override
    protected void init()
    {
        super.init();

        lineOfSightButton = new EyeOfEnderButton(
                leftPos + (imageWidth - EYE_BUTTON_SIZE) / 2,
                topPos + 53,
                button -> toggleLineOfSight());
        lineOfSightButton.setLineOfSightEnabled(menu.isUsingLineOfSight());
        addRenderableWidget(lineOfSightButton);
    }

    private void toggleLineOfSight()
    {
        Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, FeralFlareLanternMenu.TOGGLE_LINE_OF_SIGHT);
    }

    private List<Component> lineOfSightTooltip()
    {
        return menu.isUsingLineOfSight()
                ? List.of(
                        Component.translatable("screen.torchmaster.feral_flare_lantern.line_of_sight.enabled"),
                        Component.translatable("screen.torchmaster.feral_flare_lantern.line_of_sight.enabled.description"))
                : List.of(
                        Component.translatable("screen.torchmaster.feral_flare_lantern.line_of_sight.disabled"),
                        Component.translatable("screen.torchmaster.feral_flare_lantern.line_of_sight.disabled.description"));
    }

    @Override
    protected void containerTick()
    {
        super.containerTick();
        lineOfSightButton.setLineOfSightEnabled(menu.isUsingLineOfSight());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        graphics.blit(PANEL_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
    {
        var description = Component.translatable("screen.torchmaster.feral_flare_lantern.description");
        var guiCenter = this.imageWidth / 2;
        var titleX = guiCenter - font.width(title) / 2;
        var descriptionX = guiCenter - font.width(description) / 2;
        graphics.drawString(font, title, titleX, 8, 0xFF404040, false);
        graphics.drawString(font, description, descriptionX, 20, 0xFF404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(graphics, mouseX, mouseY, partialTick);

        if(lineOfSightButton.isHovered())
            graphics.renderTooltip(font, lineOfSightTooltip().stream().map(Component::getVisualOrderText).toList(), mouseX, mouseY);
    }

    private static class EyeOfEnderButton extends Button
    {
        private boolean lineOfSightEnabled;

        private EyeOfEnderButton(int x, int y, OnPress onPress)
        {
            super(x, y, EYE_BUTTON_SIZE, EYE_BUTTON_SIZE, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        }

        private void setLineOfSightEnabled(boolean lineOfSightEnabled)
        {
            this.lineOfSightEnabled = lineOfSightEnabled;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
        {
            super.renderWidget(graphics, mouseX, mouseY, partialTick);

            int itemX = getX() + (getWidth() - 16) / 2;
            int itemY = getY() + (getHeight() - 16) / 2;
            graphics.renderItem(new ItemStack(Items.ENDER_EYE), itemX, itemY);

            if(!lineOfSightEnabled)
            {
                graphics.pose().pushPose();
                graphics.pose().translate(getX() + getWidth() / 2.0F, getY() + getHeight() / 2.0F, 200.0F);
                graphics.pose().mulPose(Axis.ZP.rotationDegrees(-45.0F));
                graphics.fill(-10, -1, 10, 2, 0xFFFF4040);
                graphics.fill(10, -1, -10, 2, 0xFFFF4040);
                graphics.pose().popPose();
            }
        }

        @Override
        public void renderString(GuiGraphics graphics, Font font, int color)
        {
        }
    }
}
