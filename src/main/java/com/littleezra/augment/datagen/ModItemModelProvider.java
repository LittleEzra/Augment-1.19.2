package com.littleezra.augment.datagen;

import com.littleezra.augment.Augment;
import com.littleezra.augment.block.ModBlocks;
import com.littleezra.augment.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Augment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEggItem(ModItems.SCUTL_SPAWN_EGG.get());
        spawnEggItem(ModItems.SENTINEL_SPAWN_EGG.get());

        simpleItem(ModItems.TENDRIL_CLIPPINGS.get());
        simpleItem(ModItems.ALLOCITE_CRYSTAL.get());
        flatBlockItem(ModBlocks.WHIRL_TENDRIL.get());
        simpleBlockItem(ModBlocks.PIGLINS_SNOUT.get());

        simpleItem(ModItems.EMPTY_RUNE.get());
        simpleItem(ModItems.ALLOCITE_RUNE.get());
        simpleItem(ModItems.AMETHYST_RUNE.get());
        simpleItem(ModItems.EMERALD_RUNE.get());

        // Block Items

        blockItem(ModBlocks.ROUGH_QUARTZ_GLASS.get());
        blockItem(ModBlocks.CLEAR_QUARTZ_GLASS.get());
        blockItem(ModBlocks.ALLOCITE_BLOCK.get());
        flatBlockItem(ModBlocks.ALLOCITE_CLUSTER.get());
    }

    public String blockName(Block block){
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    private ItemModelBuilder blockItem(Block block) {
        return withExistingParent(blockName(block),
                new ResourceLocation(Augment.MOD_ID, "block/" + blockName(block)));
    }
    private ItemModelBuilder spawnEggItem(Item item) {
        return withExistingParent(item.toString(),
                new ResourceLocation("item/template_spawn_egg"));
    }
    private ItemModelBuilder simpleItem(Item item) {
        return withExistingParent(item.toString(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Augment.MOD_ID,"item/" + item.toString()));
    }
    private ItemModelBuilder flatBlockItem(Block block) {
        return withExistingParent(blockName(block),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Augment.MOD_ID,"block/" + blockName(block)));
    }
    private ItemModelBuilder simpleBlockItem(Block block) {
        return withExistingParent(blockName(block),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Augment.MOD_ID,"item/" + blockName(block)));
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(item.toString(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Augment.MOD_ID,"item/" + item.toString()));
    }
}
