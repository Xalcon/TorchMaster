package net.xalcon.torchmaster.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xalcon.torchmaster.Torchmaster;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Torchmaster.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityBlockingVolumeRenderer
{
    private static final ResourceLocation FORCEFIELD_LOCATION = new ResourceLocation("textures/misc/forcefield.png");

    private static BoundingBox createVolume(Vec3i pos, int halfRange)
    {
        var min = pos.offset(-halfRange, -halfRange, -halfRange);
        var max = pos.offset(halfRange + 1, halfRange + 1, halfRange + 1);
        return BoundingBox.fromCorners(min, max);
    }

    private static void renderLightVolume(Vec3i pos, int torchRange, Camera cam, int color)
    {
        var mc = Minecraft.getInstance();
        var blockRenderDistance = mc.options.getEffectiveRenderDistance() * 16;
        var torchVol = createVolume(pos, torchRange);
        var playerVolume = createVolume(cam.getBlockPosition(), blockRenderDistance);
        if(!playerVolume.intersects(torchVol)) return;


        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        var camX = cam.getPosition().x;
        var camZ = cam.getPosition().z;
        var camY = cam.getPosition().y;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        // RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.setShaderTexture(0, FORCEFIELD_LOCATION);
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();

        RenderSystem.applyModelViewMatrix();
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        // Offset is used to work around z-fighting issues
        RenderSystem.polygonOffset(-3.0F, -3.0F);
        RenderSystem.enablePolygonOffset();
        // Render both sides
        RenderSystem.disableCull();
        float slide = (float)(Util.getMillis() % 3000L) / 3000.0F;

        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        var vMinX = torchVol.minX() - camX;
        var vMaxX = torchVol.maxX() - camX;
        var vMinY = torchVol.minY() - camY;
        var vMaxY = torchVol.maxY() - camY;
        var vMinZ = torchVol.minZ() - camZ;
        var vMaxZ = torchVol.maxZ() - camZ;

        var uv0 = slide;
        var uv1 = torchRange  + slide;

        // +X
        bufferbuilder.vertex(vMaxX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMaxZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMinZ).uv(uv0, uv1).endVertex();

        // -X
        bufferbuilder.vertex(vMinX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMinX, vMinY, vMaxZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMinZ).uv(uv0, uv1).endVertex();

        // +Z
        bufferbuilder.vertex(vMinX, vMinY, vMaxZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMaxZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMaxZ).uv(uv0, uv1).endVertex();

        // -Z
        bufferbuilder.vertex(vMinX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMinZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMinZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMinZ).uv(uv0, uv1).endVertex();

        // +Y
        bufferbuilder.vertex(vMinX, vMaxY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMinZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMaxZ).uv(uv0, uv1).endVertex();

        // -Y
        bufferbuilder.vertex(vMinX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMinZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMinY, vMaxZ).uv(uv0, uv1).endVertex();

        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.enableCull();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
    }

    private static final Map<Vec3i, Tuple<Integer, Integer>> volumeLights = new HashMap<>();
    private static final Map<Vec3i, Integer> locationLights = new HashMap<>();

    public static void showVolumeAt(Vec3i pos, int range, int color)
    {
        volumeLights.put(pos, new Tuple<>(range, color));
    }

    public static void removeVolumeAt(Vec3i pos)
    {
        volumeLights.remove(pos);
    }

    public static void showLocationAt(Vec3i pos, int color)
    {
        locationLights.put(pos, color);
    }

    public static void removeLocationAt(Vec3i pos)
    {
        locationLights.remove(pos);
    }

    public static void clearAll()
    {
        volumeLights.clear();
        locationLights.clear();
    }

    @SubscribeEvent
    public static void onRenderLevelStageEvent(RenderLevelStageEvent event)
    {
        if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;

        for (var light : volumeLights.entrySet())
        {
            renderLightVolume(light.getKey(), light.getValue().getA(), event.getCamera(), light.getValue().getB());
        }

        for (var light : locationLights.entrySet())
        {
            renderTorchLocation(light.getKey(), light.getValue(), event.getCamera());
        }
    }

    private static void renderTorchLocation(Vec3i pos, int color, Camera cam)
    {
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();

        var camX = cam.getPosition().x;
        var camZ = cam.getPosition().z;
        var camY = cam.getPosition().y;

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        //RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderTexture(0, FORCEFIELD_LOCATION);
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();

        RenderSystem.applyModelViewMatrix();
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        // Offset is used to work around z-fighting issues
        RenderSystem.polygonOffset(-3.0F, -3.0F);
        RenderSystem.enablePolygonOffset();
        // Render both sides
        RenderSystem.disableCull();
        float slide = (float)(Util.getMillis() % 3000L) / 3000.0F;

        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        var vMinX = pos.getX() - camX;
        var vMaxX = pos.getX() - camX + 1;
        var vMinY = pos.getY() - camY;
        var vMaxY = pos.getY() - camY + 1;
        var vMinZ = pos.getZ() - camZ;
        var vMaxZ = pos.getZ() - camZ + 1;

        var uv0 = 0;
        var uv1 = 1;

        // +X
        bufferbuilder.vertex(vMaxX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMaxZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMinZ).uv(uv0, uv1).endVertex();

        // -X
        bufferbuilder.vertex(vMinX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMinX, vMinY, vMaxZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMinZ).uv(uv0, uv1).endVertex();

        // +Z
        bufferbuilder.vertex(vMinX, vMinY, vMaxZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMaxZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMaxZ).uv(uv0, uv1).endVertex();

        // -Z
        bufferbuilder.vertex(vMinX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMinZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMinZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMinZ).uv(uv0, uv1).endVertex();

        // +Y
        bufferbuilder.vertex(vMinX, vMaxY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMinZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMaxY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMaxY, vMaxZ).uv(uv0, uv1).endVertex();

        // -Y
        bufferbuilder.vertex(vMinX, vMinY, vMinZ).uv(uv1, uv1).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMinZ).uv(uv1 , uv0).endVertex();
        bufferbuilder.vertex(vMaxX, vMinY, vMaxZ).uv(uv0, uv0).endVertex();
        bufferbuilder.vertex(vMinX, vMinY, vMaxZ).uv(uv0, uv1).endVertex();

        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.enableCull();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
    }
}
