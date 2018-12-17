package net.xalcon.torchmaster.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.DyeUtils;
import net.xalcon.torchmaster.common.ModBlocks;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.HashMap;

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
        for(HashMap.Entry<BlockPos, DyeColor> torch : visualizedTorches.entrySet())
        {
            if(!world.isBlockLoaded(torch.getKey()) || world.getBlockState(torch.getKey()).getBlock() != ModBlocks.getMegaTorch())
            {
                remove(torch.getKey());
            }
        }
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
        GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        BufferBuilder vbo = Tessellator.getInstance().getBuffer();
        vbo.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for(HashMap.Entry<BlockPos, DyeColor> torch : visualizedTorches.entrySet())
        {
            BlockPos pos = torch.getKey();
            DyeColor color = torch.getValue();
            CreateCylinder(vbo, pos.getX(), pos.getY(), pos.getZ(), color);
            CreateCube(vbo, pos.getX(), pos.getY(), pos.getZ(), color);
        }
        Tessellator.getInstance().draw();
        GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

        vbo.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for(HashMap.Entry<BlockPos, DyeColor> torch : visualizedTorches.entrySet())
        {
            BlockPos pos = torch.getKey();
            DyeColor color = torch.getValue();
            CreateCylinder(vbo, pos.getX(), pos.getY(), pos.getZ(), color);
        }
        Tessellator.getInstance().draw();

        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static void CreateCylinder(BufferBuilder vbo, float x, float y, float z, DyeColor c)
    {
        double radius = 32, halfLength = 32;
        int slices = 32;
        for(int i = 0; i < slices; i++)
        {
            double theta = ((double)i)*2.0*Math.PI / slices;
            double nextTheta = ((double)i+1)*2.0*Math.PI / slices;
            /*vertex at middle of end */
            // CENTER vbo.pos(0.0, halfLength, 0.0);
            /*vertices at edges of circle*/
            vbo.pos(x + .5 + radius*Math.cos(theta), y + 1 + halfLength, z + .5 + radius*Math.sin(theta)).color(c.r, c.g, c.b, 0.4f).endVertex();
            vbo.pos (x + .5 + radius*Math.cos(nextTheta), y + 1 + halfLength, z + .5 + radius*Math.sin(nextTheta)).color(c.r, c.g, c.b, 0.4f).endVertex();
            /* the same vertices at the bottom of the cylinder*/
            vbo.pos (x + .5 + radius*Math.cos(nextTheta), y + 1 - halfLength, z + .5 + radius*Math.sin(nextTheta)).color(c.r, c.g, c.b, 0.4f).endVertex();
            vbo.pos(x + .5 + radius*Math.cos(theta), y + 1 - halfLength, z + .5 + radius*Math.sin(theta)).color(c.r, c.g, c.b, 0.4f).endVertex();
            // CENTER vbo.pos(0.0, -halfLength, 0.0);
        }
    }

    private static void CreateCube(BufferBuilder vbo, float x, float y, float z, DyeColor c)
    {
        vbo.pos(x + 0, y + 1, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 1, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 1, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 1, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 0, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 0, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 0, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 0, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 0, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 0, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 1, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 1, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 0, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 1, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 1, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 0, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 0, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 1, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 1, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 1, y + 0, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 0, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 0, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 1, z + 1).color(c.r, c.g, c.b, 0.6f).endVertex();
        vbo.pos(x + 0, y + 1, z + 0).color(c.r, c.g, c.b, 0.6f).endVertex();
    }
}
