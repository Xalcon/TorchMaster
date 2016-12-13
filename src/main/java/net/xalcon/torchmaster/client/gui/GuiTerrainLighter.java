package net.xalcon.torchmaster.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.xalcon.torchmaster.common.container.ContainerTerrainLighter;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

public class GuiTerrainLighter extends GuiContainer
{
	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");
	private TileEntityTerrainLighter tile;
	private IInventory playerInv;

	public GuiTerrainLighter(IInventory playerInv, TileEntityTerrainLighter tileEntity)
	{
		super(new ContainerTerrainLighter(playerInv, tileEntity));
		this.xSize = 176;
		this.ySize = 166;
		this.tile = tileEntity;
		this.playerInv = playerInv;
	}

	@Override

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = this.tile.getDisplayName().getUnformattedText();
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}
}
