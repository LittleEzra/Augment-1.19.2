package com.littleezra.augment.item;

import com.littleezra.augment.Augment;
import com.littleezra.augment.entity.ModEntityTypes;
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

    public static final RegistryObject<Item> SENTINEL_SPAWN_EGG = ITEMS.register("sentinel_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SENTINEL, 0x4d494d, 0xc85a88, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SCUTL_SPAWN_EGG = ITEMS.register("scutl_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SCUTL, 0x3b393b, 0xfca790, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
