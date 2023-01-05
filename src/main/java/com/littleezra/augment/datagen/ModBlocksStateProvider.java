package com.littleezra.augment.datagen;

import com.littleezra.augment.Augment;
import com.littleezra.augment.block.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
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

    }

    public void simpleBlockWithRenderType(Block block, String rendertype){
        simpleBlock(block, models().cubeAll(name(block), blockTexture(block)).renderType(rendertype));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
}
