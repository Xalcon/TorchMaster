package net.xalcon.torchmaster.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.DyeUtils;
import net.xalcon.torchmaster.TorchMasterMod;
import net.xalcon.torchmaster.common.ModBlocks;
import net.xalcon.torchmaster.common.TorchmasterConfig;
import org.lwjgl.opengl.GL11;

import java.util.*;

@Mod.EventBusSubscriber(Side.CLIENT)
public class TorchVolumeRenderHandler
{

    public static class DyeColor
    {
        float r;
        float g;
        float b;

        private static DyeColor[] colors = Arrays.stream(ItemDye.DYE_COLORS).mapToObj(DyeColor::new).toArray(DyeColor[]::new);

        public DyeColor(int color)
        {
            r = ((color & 0xFF0000) >> 16) / 255.0f;
            g = ((color & 0x00FF00) >> 8) / 255.0f;
            b = (color & 0x0000FF) / 255.0f;
        }

        public static DyeColor FromItemStack(ItemStack stack)
        {
            int meta = DyeUtils.rawDyeDamageFromStack(stack);
            if(meta < 0 || meta > 15) return null;
            return colors[meta];
        }
    }

    private static HashMap<BlockPos, DyeColor> visualizedTorches = new HashMap<>();

    public static void toggle(BlockPos pos, DyeColor color)
    {
        DyeColor r = visualizedTorches.remove(pos);
        if(r == null)
            visualizedTorches.put(pos, color);
    }

    public static void remove(BlockPos pos)
    {
        visualizedTorches.remove(pos);
    }

    public static void clearList()
    {
        visualizedTorches.clear();
    }

    public static void onGlobalTick(World world)
    {
        visualizedTorches.entrySet()
                .removeIf(torch -> !world.isBlockLoaded(torch.getKey()) || world.getBlockState(torch.getKey()).getBlock() != ModBlocks.getMegaTorch());
    }

    @SubscribeEvent
    public static void onRender(RenderWorldLastEvent event)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        GlStateManager.glLineWidth(2.0F);
        double xD = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)event.getPartialTicks();
        double yD = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)event.getPartialTicks();
        double zD = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)event.getPartialTicks();
        GlStateManager.translate(-xD, -yD, -zD);

        GlStateManager.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        BufferBuilder vbo = Tessellator.getInstance().getBuffer();

        int segmentCount = (int) ((TorchmasterConfig.MegaTorchRange * 2 + 1) / TorchmasterConfig.TorchVisualizerSegmentationFactor);

        for(HashMap.Entry<BlockPos, DyeColor> torch : visualizedTorches.entrySet())
        {
            BlockPos pos = torch.getKey();
            DyeColor color = torch.getValue();

            vbo.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            CreateSegmentedCube(vbo, pos.getX(), pos.getY(), pos.getZ(), TorchmasterConfig.MegaTorchRange, color, segmentCount);
            Tessellator.getInstance().draw();
        }
        GlStateManager.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);

        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void CreateSegmentedCube(BufferBuilder vbo, float x, float y, float z, int torchRange, DyeColor color, int segmentCount)
    {
        float o = (torchRange + .5f) + .005f;
        float cx = x + .5f;
        float cy = y + .5f;
        float cz = z + .5f;

        float segmentSize = ((torchRange * 2f) + 1) / segmentCount;
        GlStateManager.glLineWidth(1);

        for(float f = -o; f < o; f += segmentSize)
        {
            vbo.pos(cx + f, cy + o, cz - o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + f, cy + o, cz + o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + f, cy - o, cz + o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + f, cy - o, cz - o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + f, cy + o, cz - o).color(color.r, color.g, color.b, 1f).endVertex();
        }

        for(float f = -o; f < o; f += segmentSize)
        {
            vbo.pos(cx - o, cy + o, cz + f).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + o, cy + o, cz + f).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + o, cy - o, cz + f).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx - o, cy - o, cz + f).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx - o, cy + o, cz + f).color(color.r, color.g, color.b, 1f).endVertex();
        }

        for(float f = -o; f < o; f += segmentSize)
        {
            vbo.pos(cx - o, cy + f, cz + o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + o, cy + f, cz + o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx + o, cy + f, cz - o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx - o, cy + f, cz - o).color(color.r, color.g, color.b, 1f).endVertex();
            vbo.pos(cx - o, cy + f, cz + o).color(color.r, color.g, color.b, 1f).endVertex();
        }
    }
}
