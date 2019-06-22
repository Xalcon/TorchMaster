package net.xalcon.torchmaster.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.client.gui.elements.GuiItemIconButton;
import net.xalcon.torchmaster.common.tiles.TileEntityFeralFlareLantern;

public class GuiFeralLantern extends GuiScreen
{
    private static final String TEXT_ENABLED_SHORT = "gui." + Torchmaster.MODID + ".feral_flare_lantern.state.los_on.short";
    private static final String TEXT_DISABLED_SHORT = "gui." + Torchmaster.MODID + ".feral_flare_lantern.state.los_off.short";
    private static final String TEXT_ENABLED_DESC = "gui." + Torchmaster.MODID + ".feral_flare_lantern.state.los_on.description";
    private static final String TEXT_DISABLED_DESC = "gui." + Torchmaster.MODID + ".feral_flare_lantern.state.los_off.description";
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("minecraft:textures/gui/demo_background.png");
    private final TileEntityFeralFlareLantern tile;
    private GuiItemIconButton button;

    public GuiFeralLantern(TileEntityFeralFlareLantern tile)
    {
        this.tile = tile;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.buttons.add(this.button = new GuiItemIconButton(0, 114 + i, 30 + j, new ItemStack(Items.ENDER_EYE, 1), tile::shouldUseLineOfSight));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        int j = (this.height - 166) / 2;
        this.drawDefaultBackground();
        this.drawGuiBackground();
        super.render(mouseX, mouseY, partialTicks);
        if(tile.shouldUseLineOfSight())
        {
            int k = drawCenteredMultilineString(I18n.format(TEXT_ENABLED_SHORT), this.width / 2, 60 + j, -1);
            drawCenteredMultilineString(I18n.format(TEXT_ENABLED_DESC), this.width / 2, 70 + j + k, -1);
        }
        else
        {
            int k = drawCenteredMultilineString(I18n.format(TEXT_DISABLED_SHORT), this.width / 2, 60 + j, -1);
            drawCenteredMultilineString(I18n.format(TEXT_DISABLED_DESC), this.width / 2, 70 + j + k, -1);
        }
        //this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void confirmResult(boolean p_confirmResult_1_, int id)
    {
        super.confirmResult(p_confirmResult_1_, id);
        if(id == this.button.id)
        {
            // TODO: Reimplement
            // TorchmasterNetwork.getNetwork().sendToServer(new PacketSetFeralLanternLoS(!tile.shouldUseLineOfSight(), tile));
        }
    }

    private void drawGuiBackground()
    {
        this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
    }

    private int drawCenteredMultilineString(String text, int x, int y, int color)
    {
        String[] lines = text.split("\\$n");
        for(int i = 0; i < lines.length; i++)
        {
            drawCenteredString(this.mc.fontRenderer, lines[i].trim(), x, y + i * 12, color);
        }
        return lines.length * 12;
    }
}
