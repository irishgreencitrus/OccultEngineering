package io.github.irishgreencitrus.occultengineering.compat.curios;

import com.simibubi.create.compat.curios.GogglesCurioRenderer;
import io.github.irishgreencitrus.occultengineering.registry.OccultEngineeringItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@OnlyIn(Dist.CLIENT)
public class OcEngCuriosRenderers {
    public static void register() {
        CuriosRendererRegistry.register(OccultEngineeringItems.COMBINED_GOGGLES.get(), () -> new GogglesCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GogglesCurioRenderer.LAYER)));
    }

    public static void onLayerRegister(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GogglesCurioRenderer.LAYER, () -> LayerDefinition.create(GogglesCurioRenderer.mesh(), 1, 1));
    }
}
