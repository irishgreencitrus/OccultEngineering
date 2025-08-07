package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

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

        float progress = Mth.sin(percentage * Mth.HALF_PI);

        var newPos = this.startingPosition.lerp(targetPos.get(), progress);

        this.x = newPos.x();
        this.y = newPos.y();
        this.z = newPos.z();

        setPos(newPos.x, newPos.y, newPos.z);
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
