package com.littleezra.augment.entity;

import com.littleezra.augment.Augment;
import com.littleezra.augment.entity.custom.Scutl;
import com.littleezra.augment.entity.custom.Sentinel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Augment.MOD_ID);

    public static final RegistryObject<EntityType<Sentinel>> SENTINEL = ENTITY_TYPES.register("sentinel",
            () -> EntityType.Builder.of(Sentinel::new, MobCategory.MONSTER).sized( 0.625f, 2.5f)
                    .build(new ResourceLocation(Augment.MOD_ID, "sentinel").toString()));

    public static final RegistryObject<EntityType<Scutl>> SCUTL = ENTITY_TYPES.register("scutl",
            () -> EntityType.Builder.of(Scutl::new, MobCategory.MONSTER).sized( 0.375f, 0.75f)
                    .build(new ResourceLocation(Augment.MOD_ID, "scutl").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
