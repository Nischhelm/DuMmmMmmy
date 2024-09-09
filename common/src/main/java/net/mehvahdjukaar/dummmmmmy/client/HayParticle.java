package net.mehvahdjukaar.dummmmmmy.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class HayParticle extends TextureSheetParticle {

    protected HayParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    protected HayParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            var p = new HayParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            p.pickSprite(spriteSet);
            return p;
        }
    }
}
