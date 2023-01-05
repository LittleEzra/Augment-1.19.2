package com.littleezra.augment.block;

import com.littleezra.augment.Augment;
import com.littleezra.augment.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Augment.MOD_ID);

    public static final RegistryObject<Block> ROUGH_QUARTZ_GLASS = registerBlock("rough_quartz_glass",
            () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> CLEAR_QUARTZ_GLASS = registerBlock("clear_quartz_glass",
            () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)), CreativeModeTab.TAB_BUILDING_BLOCKS);



    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static LeavesBlock leaves(SoundType p_152615_) {
        return new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(p_152615_).noOcclusion().isValidSpawn(ModBlocks::ocelotOrParrot).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never));
    }
    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }
    private static Boolean ocelotOrParrot(BlockState p_50822_, BlockGetter p_50823_, BlockPos p_50824_, EntityType<?> p_50825_) {
        return (boolean)(p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT);
    }

    private static Block simpleFlammableBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new Block(properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static SlabBlock flammableSlabBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new SlabBlock(properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static StairBlock flammableStairBlock(Block base, BlockBehaviour.Properties properties, int flammability, int firespread){
        return new StairBlock(() -> base.defaultBlockState(), properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static FenceBlock flammableFenceBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new FenceBlock(properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static FenceGateBlock flammableFenceGateBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new FenceGateBlock(properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab)
    {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
