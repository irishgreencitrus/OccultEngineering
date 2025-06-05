package io.github.irishgreencitrus.occultengineering.content.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PucaRenderer extends GeoEntityRenderer<PucaEntity> {
    public PucaRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PucaModel());
    }
}
