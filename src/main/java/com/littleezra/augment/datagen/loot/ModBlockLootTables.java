package com.littleezra.augment.datagen.loot;

import com.littleezra.augment.block.ModBlocks;
import com.littleezra.augment.item.ModItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {

    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    private static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();
    private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
    private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
    private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();

    @Override
    protected void addTables() {
        this.dropWhenSilkTouch(ModBlocks.ROUGH_QUARTZ_GLASS.get());
        this.dropWhenSilkTouch(ModBlocks.CLEAR_QUARTZ_GLASS.get());
        this.dropSelf(ModBlocks.PIGLINS_SNOUT.get());
        this.dropSelf(ModBlocks.RESONATOR.get());

        this.add(ModBlocks.ALLOCITE_BLOCK.get(), createAllociteDrops(ModBlocks.ALLOCITE_BLOCK.get()));
        this.add(ModBlocks.ALLOCITE_CLUSTER.get(), createAllociteDrops(ModBlocks.ALLOCITE_CLUSTER.get()));

        this.add(ModBlocks.WHIRL_TENDRIL.get(), createSeedlessBushDrops(ModBlocks.WHIRL_TENDRIL.get(), ModItems.TENDRIL_CLIPPINGS.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public LootTable.Builder createSeedlessBushDrops(Block block, ItemLike item){
        return createShearsDispatchTable(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))));
    }

    public LootTable.Builder createAllociteDrops(Block block){
        return createSilkTouchDispatchTable(block, LootItem.lootTableItem(ModItems.ALLOCITE_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(ModItems.ALLOCITE_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))));
    }
}
