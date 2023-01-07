package com.littleezra.augment.datagen;

import com.littleezra.augment.Augment;
import com.littleezra.augment.block.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocksStateProvider extends BlockStateProvider {

    public ModBlocksStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Augment.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithRenderType(ModBlocks.ROUGH_QUARTZ_GLASS.get(), "translucent");
        simpleBlockWithRenderType(ModBlocks.CLEAR_QUARTZ_GLASS.get(), "cutout");
        simpleBlock(ModBlocks.ALLOCITE_BLOCK.get());

        simpleBlock(ModBlocks.RESONATOR.get(), models().withExistingParent("resonator",
                new ResourceLocation(Augment.MOD_ID, "block/resonator_base")));

        directionalBlock(ModBlocks.ALLOCITE_CLUSTER.get(), models().cross(name(ModBlocks.ALLOCITE_CLUSTER.get()),
                new ResourceLocation(Augment.MOD_ID, "block/" + name(ModBlocks.ALLOCITE_CLUSTER.get()))).renderType("cutout"));

        crossBlockWithRenderType(ModBlocks.WHIRL_TENDRIL.get());
        crossBlockWithRenderType(ModBlocks.PIGLINS_SNOUT.get());
    }

    public void simpleBlockWithRenderType(Block block, String renderType){
        simpleBlock(block, models().cubeAll(name(block), blockTexture(block)).renderType(renderType));
    }
    public void crossBlockWithRenderType(Block block){
        crossBlockWithRenderType(block, "cutout");
    }
    public void crossBlockWithRenderType(Block block, String renderType){
        simpleBlock(block, models().cross(name(block),
                new ResourceLocation(Augment.MOD_ID, "block/" + name(block))).renderType(renderType));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
}
