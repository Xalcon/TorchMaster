package net.xalcon.torchmaster.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.HashMap;
import java.util.Map;

public class VolumeRendererOverlay {
    public static final int[] COLORS = {
            0xFFE53935, // Red-ish
            0xFFFFA020, // Orange
            0xFFF4D03F, // Yellow-ish
            0xFFA0FF20, // Lime
            0xFF43A047, // Leafy Green
            0xFF20FFA0, // Cyan
            0xFF1E88E5, // Sky Blue
            0xFF3F51B5, // Indigo
            0xFFA020FF, // Violet
            0xFFD81B60, // Magenta
            0xFFF06292, // Pink
            0xFF9C27B0  // Purple
    };


    private static final ResourceLocation FORCEFIELD_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/misc/forcefield.png");

    private record LightKey(ResourceLocation dimension, Vec3i pos) {
        public LightKey(ResourceKey<Level> dimension, Vec3i pos) {
            this(dimension.location(), pos);
        }
    }

    public record LightInfo(int colorIndex, int range, boolean showVolume, boolean showLocation) {}

    private static final Map<LightKey, LightInfo> lights = new HashMap<>();

    private static BoundingBox createVolume(Vec3i pos, int halfRange)
    {
        var min = pos.offset(-halfRange, -halfRange, -halfRange);
        var max = pos.offset(halfRange + 1, halfRange + 1, halfRange + 1);
        return BoundingBox.fromCorners(min, max);
    }

    private static void renderWireframeCube(Vec3i pos, int torchRange, int color, Camera cam)
    {

        var mc = Minecraft.getInstance();
        var blockRenderDistance = mc.options.getEffectiveRenderDistance() * 16;
        var torchVol = createVolume(pos, torchRange);
        var playerVolume = createVolume(cam.getBlockPosition(), blockRenderDistance);
        if(!playerVolume.intersects(torchVol)) return;

        var camX = cam.getPosition().x;
        var camZ = cam.getPosition().z;
        var camY = cam.getPosition().y;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        // Set color multiplier
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        RenderSystem.setShaderColor(red, green, blue, 1f);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        // Offset is used to work around z-fighting issues
        RenderSystem.polygonOffset(-3.0F, -3.0F);
        RenderSystem.enablePolygonOffset();
        // Render both sides
        RenderSystem.disableCull();

        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        var vMinX = (float)(torchVol.minX() - camX);
        var vMaxX = (float)(torchVol.maxX() - camX);
        var vMinY = (float)(torchVol.minY() - camY);
        var vMaxY = (float)(torchVol.maxY() - camY);
        var vMinZ = (float)(torchVol.minZ() - camZ);
        var vMaxZ = (float)(torchVol.maxZ() - camZ);

        // We need to set the base color for the lines to white so the multiplier for shader color can work properly.
        // And yea, it's pretty hacky, whatever.
        color = 0xFFFFFFFF;

        // Bottom face
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setColor(color);
        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setColor(color);

        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setColor(color);
        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setColor(color);

        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setColor(color);
        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setColor(color);

        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setColor(color);
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setColor(color);

        // Top face
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setColor(color);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setColor(color);

        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setColor(color);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setColor(color);

        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setColor(color);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setColor(color);

        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setColor(color);
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setColor(color);

        // Vertical edges
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setColor(color);
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setColor(color);

        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setColor(color);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setColor(color);

        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setColor(color);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setColor(color);

        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setColor(color);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setColor(color);

        var meshdata = bufferbuilder.build();
        if(meshdata != null) {
            BufferUploader.drawWithShader(meshdata);
        }

        RenderSystem.enableCull();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
    }

    private static void renderLightVolume(Vec3i pos, int torchRange, int color, Camera cam)
    {
        // Logic is similar to vanillas LevelRenderer.renderWorldBorder()
        var mc = Minecraft.getInstance();
        var blockRenderDistance = mc.options.getEffectiveRenderDistance() * 16;
        var torchVol = createVolume(pos, torchRange);
        var playerVolume = createVolume(cam.getBlockPosition(), blockRenderDistance);
        if(!playerVolume.intersects(torchVol)) return;

        var camX = cam.getPosition().x;
        var camZ = cam.getPosition().z;
        var camY = cam.getPosition().y;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.setShaderTexture(0, FORCEFIELD_LOCATION);
        RenderSystem.depthMask(Minecraft.useShaderTransparency());
        // Set color multiplier
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

        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        var vMinX = (float)(torchVol.minX() - camX);
        var vMaxX = (float)(torchVol.maxX() - camX);
        var vMinY = (float)(torchVol.minY() - camY);
        var vMaxY = (float)(torchVol.maxY() - camY);
        var vMinZ = (float)(torchVol.minZ() - camZ);
        var vMaxZ = (float)(torchVol.maxZ() - camZ);

        var uv0 = (float)(Util.getMillis() % 3000L) / 3000.0F; // slide the texture over 3s
        var uv1 = torchRange  + uv0;

        // +X
        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setUv(uv0, uv1);

        // -X
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setUv(uv0, uv1);

        // +Z
        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setUv(uv0, uv1);

        // -Z
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setUv(uv0, uv1);

        // +Y
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setUv(uv0, uv1);

        // -Y
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setUv(uv0, uv1);

        var meshdata = bufferbuilder.build();
        if(meshdata != null) {
            BufferUploader.drawWithShader(meshdata);
        }

        RenderSystem.enableCull();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
    }

    private static void renderTorchLocation(Vec3i pos, int color, Camera cam)
    {
        var camX = cam.getPosition().x;
        var camZ = cam.getPosition().z;
        var camY = cam.getPosition().y;

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        //RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderTexture(0, FORCEFIELD_LOCATION);
        RenderSystem.depthMask(Minecraft.useShaderTransparency());

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

        var bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        var vMinX = (float)(pos.getX() - camX);
        var vMaxX = (float)(pos.getX() - camX + 1);
        var vMinY = (float)(pos.getY() - camY);
        var vMaxY = (float)(pos.getY() - camY + 1);
        var vMinZ = (float)(pos.getZ() - camZ);
        var vMaxZ = (float)(pos.getZ() - camZ + 1);

        var uv0 = 0;
        var uv1 = 1;

        // +X
        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setUv(uv0, uv1);

        // -X
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setUv(uv0, uv1);

        // +Z
        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setUv(uv0, uv1);

        // -Z
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setUv(uv0, uv1);

        // +Y
        bufferbuilder.addVertex(vMinX, vMaxY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMinZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMaxY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMaxY, vMaxZ).setUv(uv0, uv1);

        // -Y
        bufferbuilder.addVertex(vMinX, vMinY, vMinZ).setUv(uv1, uv1);
        bufferbuilder.addVertex(vMaxX, vMinY, vMinZ).setUv(uv1 , uv0);
        bufferbuilder.addVertex(vMaxX, vMinY, vMaxZ).setUv(uv0, uv0);
        bufferbuilder.addVertex(vMinX, vMinY, vMaxZ).setUv(uv0, uv1);

        var meshdata = bufferbuilder.build();
        if(meshdata != null) {
            BufferUploader.drawWithShader(meshdata);
        }

        RenderSystem.enableCull();
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
    }

    public static LightInfo getLightOverlay(ResourceKey<Level> dimension, Vec3i pos)
    {
        return lights.get(new LightKey(dimension, pos));
    }

    public static void setLightOverlay(ResourceKey<Level> dimension, Vec3i pos, int colorIndex, int range, boolean showVolume, boolean showLocation) {
        lights.put(new LightKey(dimension, pos), new LightInfo(colorIndex % COLORS.length, range, showVolume, showLocation));
    }

    public static void removeLightOverlay(ResourceKey<Level> dimension, Vec3i pos) {
        lights.remove(new LightKey(dimension, pos));
    }

    public static void clearAll()
    {
        lights.clear();
    }

    public static void onRenderLevel(ResourceKey<Level> dimension, Camera camera) {
        for(var light : lights.entrySet())
        {
            var key = light.getKey();
            var info = light.getValue();
            if(info.showVolume)
            {
                int color = COLORS[info.colorIndex];
                renderLightVolume(key.pos, info.range, color, camera);
                renderWireframeCube(key.pos, info.range, color, camera);
            }

            if(info.showLocation)
            {
                int color = COLORS[info.colorIndex];
                renderTorchLocation(key.pos, color, camera);
                renderWireframeCube(key.pos, 0, color, camera);
            }
        }
    }
}
