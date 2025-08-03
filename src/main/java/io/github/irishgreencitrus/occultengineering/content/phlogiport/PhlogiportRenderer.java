package io.github.irishgreencitrus.occultengineering.content.phlogiport;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class PhlogiportRenderer extends GeoBlockRenderer<PhlogiportBlockEntity> {
    public PhlogiportRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new PhlogiportModel());
    }

    @Override
    public RenderType getRenderType(PhlogiportBlockEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.translucent();
    }
}
