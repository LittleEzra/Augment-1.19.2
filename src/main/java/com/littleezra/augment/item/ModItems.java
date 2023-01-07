package com.littleezra.augment.item;

import com.littleezra.augment.Augment;
import com.littleezra.augment.block.ModBlocks;
import com.littleezra.augment.entity.ModEntityTypes;
import com.littleezra.augment.item.custom.PiglinsSnoutItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Augment.MOD_ID);

    public static final RegistryObject<Item> TENDRIL_CLIPPINGS = ITEMS.register("tendril_clippings",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SENTINEL_SPAWN_EGG = ITEMS.register("sentinel_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SENTINEL, 0x4d494d, 0xc85a88, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SCUTL_SPAWN_EGG = ITEMS.register("scutl_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SCUTL, 0x3b393b, 0xfca790, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> PIGLINS_SNOUT_ITEM = ITEMS.register("piglins_snout",
            () -> new PiglinsSnoutItem(ModBlocks.PIGLINS_SNOUT.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> ALLOCITE_CRYSTAL = ITEMS.register("allocite_crystal",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> EMPTY_RUNE = ITEMS.register("empty_rune",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> ALLOCITE_RUNE = ITEMS.register("allocite_rune",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> AMETHYST_RUNE = ITEMS.register("amethyst_rune",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> EMERALD_RUNE = ITEMS.register("emerald_rune",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
