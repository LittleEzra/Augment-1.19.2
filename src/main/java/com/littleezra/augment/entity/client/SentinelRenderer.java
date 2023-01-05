package com.littleezra.augment.entity.client;

import com.littleezra.augment.Augment;
import com.littleezra.augment.entity.custom.Sentinel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

public class SentinelRenderer extends ExtendedGeoEntityRenderer<Sentinel> {
    public SentinelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SentinelModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Sentinel instance) {
        return new ResourceLocation(Augment.MOD_ID, "textures/entity/sentinel.png");
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, Sentinel currentEntity) {
        return getTextureLocation(currentEntity);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, Sentinel currentEntity) {
        if (boneName.equals("itemRightHand")){
            return currentEntity.getItemInHand(InteractionHand.MAIN_HAND);
        }
        else if (boneName.equals("itemLeftHand")){
            return currentEntity.getItemInHand(InteractionHand.OFF_HAND);
        }
        return null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        if (boneName.equals("itemRightHand")){
            return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
        }
        else if (boneName.equals("itemLeftHand")){
            return ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
        }
        return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, Sentinel currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(PoseStack matrixStack, ItemStack item, String boneName, Sentinel currentEntity, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack matrixStack, BlockState block, String boneName, Sentinel currentEntity) {

    }

    @Override
    protected void postRenderItem(PoseStack matrixStack, ItemStack item, String boneName, Sentinel currentEntity, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack matrixStack, BlockState block, String boneName, Sentinel currentEntity) {

    }
}
