package com.littleezra.augment.entity.client;

import com.littleezra.augment.Augment;
import com.littleezra.augment.entity.custom.Scutl;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ScutlModel extends AnimatedGeoModel<Scutl> {
    @Override
    public ResourceLocation getModelResource(Scutl object) {
        return new ResourceLocation(Augment.MOD_ID, "geo/scutl.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Scutl scutl) {
        if (scutl.getFlower() == null) return new ResourceLocation(Augment.MOD_ID, "textures/entity/scutl_empty.png");

        String key = scutl.getFlower().toString();
        return new ResourceLocation(Augment.MOD_ID, "textures/entity/scutl_" + key.substring(16, key.length() - 1) + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Scutl object) {
        return new ResourceLocation(Augment.MOD_ID, "animations/scutl.animation.json");
    }
}
