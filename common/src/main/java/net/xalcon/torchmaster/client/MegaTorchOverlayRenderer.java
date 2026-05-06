package net.xalcon.torchmaster.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public final class MegaTorchOverlayRenderer
{
    private static final int OVERLAY_COLOR_ARGB = 0x60_00C800;
    private static final double MAX_RENDER_DISTANCE_SQR = 256.0D * 256.0D;
    private static final int MAX_OVERLAYS_PER_FRAME = 32;

    private MegaTorchOverlayRenderer() {}

    public static void render(PoseStack poseStack, Camera camera, Frustum frustum, ClientLevel level, int radius)
    {
        if (level == null || radius <= 0) return;

        Vec3 camPos = camera.getPosition();
        List<Candidate> candidates = new ArrayList<>();

        OverlayCache.forEach(level.dimension(), (long packed) ->
        {
            int x = BlockPos.getX(packed);
            int y = BlockPos.getY(packed);
            int z = BlockPos.getZ(packed);

            double minX = x - radius;
            double minY = y - radius;
            double minZ = z - radius;
            double maxX = x + radius + 1;
            double maxY = y + radius + 1;
            double maxZ = z + radius + 1;

            double centerX = (minX + maxX) * 0.5;
            double centerY = (minY + maxY) * 0.5;
            double centerZ = (minZ + maxZ) * 0.5;

            double dx = centerX - camPos.x;
            double dy = centerY - camPos.y;
            double dz = centerZ - camPos.z;
            double distSqr = dx * dx + dy * dy + dz * dz;
            if (distSqr > MAX_RENDER_DISTANCE_SQR) return;

            AABB aabb = new AABB(minX, minY, minZ, maxX, maxY, maxZ);
            if (!frustum.isVisible(aabb)) return;

            candidates.add(new Candidate(minX, minY, minZ, maxX, maxY, maxZ, distSqr));
        });

        if (candidates.isEmpty()) return;

        candidates.sort((a, b) -> Double.compare(a.distSqr, b.distSqr));
        int limit = Math.min(candidates.size(), MAX_OVERLAYS_PER_FRAME);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(RangeBoxRenderType.INSTANCE);

        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
        Matrix4f pose = poseStack.last().pose();

        for (int i = 0; i < limit; i++)
        {
            Candidate c = candidates.get(i);
            drawBox(consumer, pose,
                    (float) c.minX, (float) c.minY, (float) c.minZ,
                    (float) c.maxX, (float) c.maxY, (float) c.maxZ,
                    OVERLAY_COLOR_ARGB);
        }

        poseStack.popPose();
        bufferSource.endBatch(RangeBoxRenderType.INSTANCE);
    }

    private static void drawBox(VertexConsumer c, Matrix4f m,
                                float x0, float y0, float z0,
                                float x1, float y1, float z1,
                                int argb)
    {
        // Bottom (y = y0)
        c.addVertex(m, x0, y0, z0).setColor(argb);
        c.addVertex(m, x1, y0, z0).setColor(argb);
        c.addVertex(m, x1, y0, z1).setColor(argb);
        c.addVertex(m, x0, y0, z1).setColor(argb);

        // Top (y = y1)
        c.addVertex(m, x0, y1, z0).setColor(argb);
        c.addVertex(m, x0, y1, z1).setColor(argb);
        c.addVertex(m, x1, y1, z1).setColor(argb);
        c.addVertex(m, x1, y1, z0).setColor(argb);

        // North (z = z0)
        c.addVertex(m, x0, y0, z0).setColor(argb);
        c.addVertex(m, x0, y1, z0).setColor(argb);
        c.addVertex(m, x1, y1, z0).setColor(argb);
        c.addVertex(m, x1, y0, z0).setColor(argb);

        // South (z = z1)
        c.addVertex(m, x0, y0, z1).setColor(argb);
        c.addVertex(m, x1, y0, z1).setColor(argb);
        c.addVertex(m, x1, y1, z1).setColor(argb);
        c.addVertex(m, x0, y1, z1).setColor(argb);

        // West (x = x0)
        c.addVertex(m, x0, y0, z0).setColor(argb);
        c.addVertex(m, x0, y0, z1).setColor(argb);
        c.addVertex(m, x0, y1, z1).setColor(argb);
        c.addVertex(m, x0, y1, z0).setColor(argb);

        // East (x = x1)
        c.addVertex(m, x1, y0, z0).setColor(argb);
        c.addVertex(m, x1, y1, z0).setColor(argb);
        c.addVertex(m, x1, y1, z1).setColor(argb);
        c.addVertex(m, x1, y0, z1).setColor(argb);
    }

    private record Candidate(double minX, double minY, double minZ,
                             double maxX, double maxY, double maxZ,
                             double distSqr) {}
}
