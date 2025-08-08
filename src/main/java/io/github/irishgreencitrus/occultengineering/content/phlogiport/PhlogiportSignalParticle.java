package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class PhlogiportSignalParticle extends TextureSheetParticle {
    private final PositionSource target;
    private final Vec3 startingPosition;

    protected PhlogiportSignalParticle(ClientLevel level, double x, double y, double z, PositionSource target, int lifetime) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.quadSize = 0.5F;
        this.target = target;
        this.lifetime = lifetime;

        startingPosition = new Vec3(x, y, z);
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            remove();
            return;
        }
        var targetPos = target.getPosition(level);
        if (targetPos.isEmpty()) {
            remove();
            return;
        }

        float percentage = (float) this.age / this.lifetime;

        //float progress = Mth.sin(percentage * Mth.HALF_PI);

        var newPos = this.startingPosition.lerp(targetPos.get(), percentage);

        this.x = newPos.x();
        this.y = newPos.y();
        this.z = newPos.z();

        setPos(newPos.x, newPos.y, newPos.z);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        var targetPos = target.getPosition(level);
        if (targetPos.isPresent()) {
            var currentPos = new Vec3(
                    Mth.lerp(partialTicks, this.xo, this.x),
                    Mth.lerp(partialTicks, this.yo, this.y),
                    Mth.lerp(partialTicks, this.zo, this.z)
            );
            var dir = targetPos.get().subtract(currentPos).normalize();

            /*
            var cameraForward = new Vec3(camera.getLookVector());
            var cameraUp = new Vec3(camera.getUpVector());
            var cameraRight = cameraForward.cross(cameraUp);
             */

            //var right = dir.dot(cameraRight);
            //var up = dir.dot(cameraUp);

            float rot = (float) Mth.atan2(dir.z, dir.x) + Mth.PI;
            rot = Math.round(rot / Mth.HALF_PI) * Mth.HALF_PI;
            this.oRoll = this.roll;
            this.roll = rot;
        }
        renderSignal(vertexConsumer, camera, partialTicks, (q) -> q.rotateZ(this.roll));
        renderSignal(vertexConsumer, camera, partialTicks, (q) -> q.rotateY(-Mth.PI).rotateZ(Mth.PI + this.roll));
    }

    private void renderSignal(VertexConsumer buffer, Camera camera, float partialTicks, Consumer<Quaternionf> quatConsumer) {
        var cameraPos = camera.getPosition();
        cameraPos = startingPosition;

        var particleX = Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x;
        var particleY = Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y;
        var particleZ = Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z;

        var rotAxis = new Vector3f(0.5F, 0.5F, 0.5F).normalize();

        Quaternionf rotation = new Quaternionf().setAngleAxis(0F, rotAxis.x, rotAxis.y, rotAxis.z);

        quatConsumer.accept(rotation);

        Vector3f[] quad = new Vector3f[]{
                new Vector3f(-1F, -1F, 0F),
                new Vector3f(-1F, 1F, 0F),
                new Vector3f(1F, 1F, 0F),
                new Vector3f(1F, -1F, 0F),
        };

        var quadSize = this.getQuadSize(partialTicks);
        for (var corner : quad) {
            corner.rotate(rotation);
            corner.mul(quadSize);
            corner.add((float) particleX, (float) particleY, (float) particleZ);
        }

        var u0 = this.getU0();
        var v0 = this.getV0();
        var u1 = this.getU1();
        var v1 = this.getV1();

        var ucoords = new float[]{u1, u1, u0, u0};
        var vcoords = new float[]{v1, v0, v0, v1};

        int lightLevel = this.getLightColor(partialTicks);

        for (int i = 0; i < 4; i++) {
            var corner = quad[i];
            buffer.vertex(corner.x, corner.y, corner.z)
                    .uv(ucoords[i], vcoords[i])
                    .color(this.rCol, this.gCol, this.bCol, this.alpha)
                    .uv2(lightLevel)
                    .endVertex();
        }
    }


    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<PhlogiportSignalParticleData> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(PhlogiportSignalParticleData type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PhlogiportSignalParticle particle = new PhlogiportSignalParticle(level, x, y, z, type.getDestination(), type.getLifetime());
            particle.pickSprite(this.sprite);
            particle.setAlpha(1.0F);
            return particle;
        }
    }
}
