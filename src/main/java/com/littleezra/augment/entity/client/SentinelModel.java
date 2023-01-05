package com.littleezra.augment.entity.client;

import com.littleezra.augment.Augment;
import com.littleezra.augment.entity.custom.Sentinel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SentinelModel extends AnimatedGeoModel<Sentinel> {
    @Override
    public ResourceLocation getModelResource(Sentinel object) {
        return new ResourceLocation(Augment.MOD_ID, "geo/sentinel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Sentinel object) {
        return new ResourceLocation(Augment.MOD_ID, "textures/entity/sentinel.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Sentinel object) {
        return new ResourceLocation(Augment.MOD_ID, "animations/sentinel.animation.json");
    }
}
