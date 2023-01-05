package com.littleezra.augment.datagen;

import com.littleezra.augment.Augment;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Augment.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    private ItemModelBuilder simpleItem(Item item) {
        return withExistingParent(item.toString(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Augment.MOD_ID,"item/" + item.toString()));
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(item.toString(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Augment.MOD_ID,"item/" + item.toString()));
    }
}
