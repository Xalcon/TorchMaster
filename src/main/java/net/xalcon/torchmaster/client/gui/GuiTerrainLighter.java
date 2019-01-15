package net.xalcon.torchmaster.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.xalcon.torchmaster.Torchmaster;
import net.xalcon.torchmaster.common.container.ContainerTerrainLighter;
import net.xalcon.torchmaster.common.tiles.TileEntityTerrainLighter;

import java.util.ArrayList;
import java.util.List;

public class GuiTerrainLighter extends GuiContainer
{
	private static final String NO_RS_SIGNAL_KEY = "gui." + Torchmaster.MODID + ".block.state.no_signal";
	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(Torchmaster.MODID + ":textures/gui/container/terrain_lighter.png");
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
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		// TODO: verify .getUnformattedComponentText() is correct here
		String s = "FIXME: TE Name"; // this.tile.getDisplayName().getUnformattedComponentText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 96 + 2, 4210752);

		int i = (this.width - this.xSize) / 2; //X asis on GUI
		int j = (this.height - this.ySize) / 2; //Y asis on GUI
		if (mouseX > (i + 162) && mouseX < (i + 162) + 6) //Basically checking if mouse is in the correct area
		{
			if (mouseY > (j + 17) && mouseY < (j + 17) + 52)
			{
				int value = tile.getTorchesPlaced();
				int max = tile.getTorchPlacedMax();
				List<String> list = new ArrayList<>();

				list.add(value + " / " + max);
				if(!tile.getWorld().isBlockPowered(tile.getPos()))
					list.add(new TextComponentTranslation(NO_RS_SIGNAL_KEY).getFormattedText());
				this.drawHoveringText(list, mouseX - i, mouseY - j, this.fontRenderer);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
