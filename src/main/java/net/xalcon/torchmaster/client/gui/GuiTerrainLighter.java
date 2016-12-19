package net.xalcon.torchmaster.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.container.ContainerTerrainLighter;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

import java.util.ArrayList;
import java.util.List;

public class GuiTerrainLighter extends GuiContainer
{
	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(TorchMasterMod.MODID + ":textures/gui/container/terrain_lighter.png");
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

		int i = (this.width - this.xSize) / 2; //X asis on GUI
		int j = (this.height - this.ySize) / 2; //Y asis on GUI
		if (mouseX > (i + 162) && mouseX < (i + 162) + 6) //Basically checking if mouse is in the correct area
		{
			if (mouseY > (j + 17) && mouseY < (j + 17) + 52)
			{
				int value = tile.getTorchesPlaced();
				int max = tile.getTorchPlacedMax();
				List list = new ArrayList();
				list.add(value + " / " + max);
				if(tile.getWorld().isBlockIndirectlyGettingPowered(tile.getPos()) == 0)
					list.add(new TextComponentTranslation("state.no_signal").getFormattedText());
				this.drawHoveringText(list, (int)mouseX - i, (int)mouseY - j, this.fontRendererObj);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

		if (tile.isBurningFuel())
		{
			int k = tile.getBurnLeftScaled(14);
			this.drawTexturedModalRect(i + 49, j + 18 + 14 - k, 176, 14 - k, 14, k);
		}

		int l = tile.getProgressScaled(52);
		this.drawTexturedModalRect(i + 162, j + 17 + (52 - l), 176, 14, 6, l);
	}
}
