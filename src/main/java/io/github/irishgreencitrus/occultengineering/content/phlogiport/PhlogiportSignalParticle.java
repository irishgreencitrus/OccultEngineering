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
import org.joml.Matrix3f;
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

        var newPos = this.startingPosition.lerp(targetPos.get().add(PhlogiportSignalParticleData.ANTENNA_OFFSET), percentage);

        this.x = newPos.x();
        this.y = newPos.y();
        this.z = newPos.z();

        setPos(newPos.x, newPos.y, newPos.z);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        renderSignal(vertexConsumer, camera, partialTicks, (q) -> q.rotateZ(roll + Mth.PI));
        renderSignal(vertexConsumer, camera, partialTicks, (q) -> q.rotateY(-Mth.PI).rotateZ(roll));
    }

    private void renderSignal(VertexConsumer buffer, Camera camera, float partialTicks, Consumer<Quaternionf> quatConsumer) {
        Vec3 currentPos = new Vec3(
                Mth.lerp(partialTicks, this.xo, this.x),
                Mth.lerp(partialTicks, this.yo, this.y),
                Mth.lerp(partialTicks, this.zo, this.z)
        );

        Quaternionf rotation = new Quaternionf();

        var targetPosOpt = target.getPosition(level);
        if (targetPosOpt.isPresent()) {
            Vector3f travelAxis = targetPosOpt.get().add(PhlogiportSignalParticleData.ANTENNA_OFFSET).subtract(currentPos).normalize().toVector3f();

            var localX = travelAxis.normalize();
            localX.normalize();

            var localY = new Vector3f(0F, 1F, 0F);
            localY = camera.getUpVector();
            localY.normalize();

            var localZ = new Vector3f();
            localX.cross(localY, localZ);
            localZ.normalize();

            localZ.cross(localX, localY);
            localY.normalize();

            Matrix3f rotMatrix = new Matrix3f(localX, localY, localZ);

            rotation.setFromNormalized(rotMatrix);
        } else {
            rotation.set(camera.rotation());
        }

        var cameraPos = camera.getPosition();
        var particleX = currentPos.x - cameraPos.x;
        var particleY = currentPos.y - cameraPos.y;
        var particleZ = currentPos.z - cameraPos.z;

        Vector3f[] quad = new Vector3f[]{
                new Vector3f(-1F, -1F, 0F),
                new Vector3f(-1F, 1F, 0F),
                new Vector3f(1F, 1F, 0F),
                new Vector3f(1F, -1F, 0F),
        };

        quatConsumer.accept(rotation);

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
