package com.littleezra.augment.datagen.loot;

import com.littleezra.augment.block.ModBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        this.dropWhenSilkTouch(ModBlocks.ROUGH_QUARTZ_GLASS.get());
        this.dropWhenSilkTouch(ModBlocks.CLEAR_QUARTZ_GLASS.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
