package net.xalcon.torchmaster.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.xalcon.torchmaster.client.VolumeRendererOverlay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityBlockingLightSettingsScreen extends net.minecraft.client.gui.screens.Screen
{

    public static class ColorIconButton extends Button {

        private final int colorRectWidth;
        private final int colorRectHeight;

        private int color;

        protected ColorIconButton(int x, int y, int width, int height, Button.OnPress onPress, @Nullable Button.CreateNarration createNarration) {
            super(x, y, width, height, Component.empty(), onPress, createNarration);
            colorRectWidth = width - 4;
            colorRectHeight = height - 4;
        }

        public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
            int i = this.getX() + this.getWidth() / 2 - this.colorRectWidth / 2;
            int j = this.getY() + this.getHeight() / 2 - this.colorRectHeight / 2;
            guiGraphics.fill(i + 1, j + 1, i + colorRectWidth - 1, j + colorRectHeight - 1, 0xFF000000);
            guiGraphics.fill(i + 2, j + 2, i + colorRectWidth - 2, j + colorRectHeight - 2, color);
        }

        public void renderString(@NotNull GuiGraphics guiGraphics, @NotNull Font font, int color) {
        }

        public void setColor(int color) {
            this.color = color;
        }
    }


    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PANEL_WIDTH = 220;
    private static final int PANEL_HEIGHT = 130;

    private final BlockPos torchPos;
    private boolean overlayOn;
    private int range;
    private Button toggleButton;
    private ColorIconButton colorCycleButton;

    public EntityBlockingLightSettingsScreen(BlockPos torchPos, int range)
    {
        super(Component.translatable("screen.torchmaster.volume_gui.title"));
        this.torchPos = torchPos.immutable();
        this.range = range;
    }

    @Override
    protected void init()
    {
        super.init();

        this.overlayOn = readCurrentOverlayState();

        int centerX = this.width / 2;
        int panelTop = (this.height - PANEL_HEIGHT) / 2;

        this.toggleButton = Button.builder(toggleLabel(), b -> onToggle())
                .bounds(centerX - BUTTON_WIDTH / 2, panelTop + 60, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addRenderableWidget(this.toggleButton);

        //noinspection SuspiciousNameCombination
        colorCycleButton = new ColorIconButton(centerX + BUTTON_WIDTH / 2, panelTop + 60, BUTTON_HEIGHT, BUTTON_HEIGHT, b -> onColorCycle(), null);
        colorCycleButton.visible = this.overlayOn;
        this.addRenderableWidget(colorCycleButton);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(centerX - BUTTON_WIDTH / 2 + 1, panelTop + PANEL_HEIGHT - 28, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        var info = VolumeRendererOverlay.getLightOverlay(level.dimension(), this.torchPos);
        if(info != null)
        {
            colorCycleButton.setColor(VolumeRendererOverlay.COLORS[info.colorIndex()]);
        }
    }

    private boolean readCurrentOverlayState()
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return false;

        return VolumeRendererOverlay.getLightOverlay(level.dimension(), this.torchPos) != null;
    }

    private void onToggle()
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        var info = VolumeRendererOverlay.getLightOverlay(level.dimension(), this.torchPos);
        if(info == null)
        {
            VolumeRendererOverlay.setLightOverlay(level.dimension(), this.torchPos, 0, range, true, true);
            colorCycleButton.setColor(VolumeRendererOverlay.COLORS[0]);
            this.overlayOn = true;
        }
        else
        {
            VolumeRendererOverlay.removeLightOverlay(level.dimension(), this.torchPos);
            this.overlayOn = false;
            colorCycleButton.setColor(VolumeRendererOverlay.COLORS[info.colorIndex()]);
        }
        colorCycleButton.visible = this.overlayOn;
        toggleButton.setMessage(toggleLabel());
    }

    private void onColorCycle() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        if (!this.overlayOn) return; // This shouldn't be possible... but just in case

        var info = VolumeRendererOverlay.getLightOverlay(level.dimension(), this.torchPos);
        int colorIndex = (info.colorIndex() + 1) % VolumeRendererOverlay.COLORS.length;
        VolumeRendererOverlay.setLightOverlay(level.dimension(), this.torchPos, colorIndex, info.range(), info.showVolume(), info.showLocation());
        colorCycleButton.setColor(VolumeRendererOverlay.COLORS[colorIndex]);
    }

    private Component toggleLabel()
    {
        return this.overlayOn
                ? Component.translatable("screen.torchmaster.volume_gui.toggle_on")
                : Component.translatable("screen.torchmaster.volume_gui.toggle_off");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int panelTop = (this.height - PANEL_HEIGHT) / 2;

        graphics.drawCenteredString(this.font, this.title, centerX, panelTop + 10, 0xFFFFFFFF);
        graphics.drawCenteredString(this.font,
                Component.translatable("screen.torchmaster.volume_gui.range", this.range),
                centerX, panelTop + 35, 0xFFCCCCCC);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}

