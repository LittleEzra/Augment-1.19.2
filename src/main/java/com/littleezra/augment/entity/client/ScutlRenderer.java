package com.littleezra.augment.entity.client;

import com.littleezra.augment.Augment;
import com.littleezra.augment.entity.custom.Scutl;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ScutlRenderer extends GeoEntityRenderer<Scutl> {
    public ScutlRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ScutlModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Scutl scutl) {
        if (scutl.getFlower() == null) return new ResourceLocation(Augment.MOD_ID, "textures/entity/scutl_empty.png");

        String key = scutl.getFlower().toString();
        return new ResourceLocation(Augment.MOD_ID, "textures/entity/scutl_" + key.substring(16, key.length() - 1) + ".png");
    }
}
