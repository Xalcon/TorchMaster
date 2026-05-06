package net.xalcon.torchmaster.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.xalcon.torchmaster.ModRegistry;
import net.xalcon.torchmaster.blocks.EntityBlockingLightBlock;
import net.xalcon.torchmaster.platform.Services;

public class MegaTorchScreen extends net.minecraft.client.gui.screens.Screen
{
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PANEL_WIDTH = 220;
    private static final int PANEL_HEIGHT = 130;

    private final BlockPos torchPos;
    private boolean overlayOn;
    private int radius;
    private Button toggleButton;

    public MegaTorchScreen(BlockPos torchPos)
    {
        super(Component.translatable("screen.torchmaster.megatorch.title"));
        this.torchPos = torchPos.immutable();
    }

    @Override
    protected void init()
    {
        super.init();

        this.radius = Services.PLATFORM.getConfig().getMegaTorchRadius();
        this.overlayOn = readCurrentOverlayState();

        int centerX = this.width / 2;
        int panelTop = (this.height - PANEL_HEIGHT) / 2;

        this.toggleButton = Button.builder(toggleLabel(), b -> onToggle())
                .bounds(centerX - BUTTON_WIDTH / 2, panelTop + 60, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addRenderableWidget(this.toggleButton);

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(centerX - BUTTON_WIDTH / 2, panelTop + PANEL_HEIGHT - 28, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }

    private boolean readCurrentOverlayState()
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return false;
        BlockState state = level.getBlockState(this.torchPos);
        if (!state.is(ModRegistry.blockMegaTorch.get())) return false;
        return state.getValue(EntityBlockingLightBlock.OVERLAY_VISIBLE);
    }

    private void onToggle()
    {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        Services.PLATFORM.getNetwork().sendToggleOverlayToServer(this.torchPos);
        this.overlayOn = !this.overlayOn;
        OverlayCache.toggle(level.dimension(), this.torchPos);
        this.toggleButton.setMessage(toggleLabel());
    }

    private Component toggleLabel()
    {
        return this.overlayOn
                ? Component.translatable("screen.torchmaster.megatorch.toggle_on")
                : Component.translatable("screen.torchmaster.megatorch.toggle_off");
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
                Component.translatable("screen.torchmaster.megatorch.radius", this.radius),
                centerX, panelTop + 35, 0xFFCCCCCC);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
