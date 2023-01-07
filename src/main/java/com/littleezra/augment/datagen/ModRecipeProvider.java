package com.littleezra.augment.datagen;

import com.littleezra.augment.block.ModBlocks;
import com.littleezra.augment.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shaped(ModBlocks.ROUGH_QUARTZ_GLASS.get())
                .define('Q', Items.QUARTZ)
                .define('G', Blocks.GLASS)
                .pattern(" Q ")
                .pattern("QGQ")
                .pattern(" Q ")
                .unlockedBy("has_quartz", has(Items.QUARTZ))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ALLOCITE_BLOCK.get())
                .define('A', ModItems.ALLOCITE_CRYSTAL.get())
                .pattern("AA")
                .pattern("AA")
                .unlockedBy("has_allocite_crystal", has(ModItems.ALLOCITE_CRYSTAL.get()))
                .save(pFinishedRecipeConsumer);

        surround4(ModItems.EMPTY_RUNE.get(), ModItems.ALLOCITE_CRYSTAL.get(), ModItems.EMPTY_RUNE.get(), pFinishedRecipeConsumer);
        surround4(ModItems.ALLOCITE_RUNE.get(), ModItems.ALLOCITE_CRYSTAL.get(), ModItems.EMPTY_RUNE.get(), pFinishedRecipeConsumer);
        surround4(ModItems.AMETHYST_RUNE.get(), Items.AMETHYST_SHARD, ModItems.EMPTY_RUNE.get(), pFinishedRecipeConsumer);
        surround4(ModItems.EMERALD_RUNE.get(), Items.EMERALD, ModItems.EMPTY_RUNE.get(), pFinishedRecipeConsumer);

        // inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.ROUGH_QUARTZ_GLASS.get()), ModBlocks.CLEAR_QUARTZ_GLASS.get().asItem()
                , 0.1F, 200)
                .unlockedBy("has_rough_quartz_glass", has(ModBlocks.ROUGH_QUARTZ_GLASS.get())).save(pFinishedRecipeConsumer);

        /*
        For making a shapeless recipe:
        ShapelessRecipeBuilder.shapeless(<ItemLike:RESULT>)
            .requires(INGREDIENT: <ItemLike> / <Tag>)
            .unlockedBy(<as normal>)
            .save(pFinishedRecipeConsumer);
         */
    }

    public void surround4(ItemLike result, ItemLike surround, ItemLike center, Consumer<FinishedRecipe> pFinishedRecipeConsumer){
        ShapedRecipeBuilder.shaped(result)
                .define('A', surround)
                .define('B', center)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .save(pFinishedRecipeConsumer);
    }
}
