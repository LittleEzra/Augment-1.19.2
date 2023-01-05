package com.littleezra.augment.event;

import com.littleezra.augment.Augment;
import com.littleezra.augment.entity.ModEntityTypes;
import com.littleezra.augment.entity.custom.Scutl;
import com.littleezra.augment.entity.custom.Sentinel;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = Augment.MOD_ID)
    public static class ForgeEvents{

    }

    @Mod.EventBusSubscriber(modid = Augment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents
    {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event)
        {
            event.put(ModEntityTypes.SENTINEL.get(), Sentinel.setAttributes());
            event.put(ModEntityTypes.SCUTL.get(), Scutl.setAttributes());
        }
    }
}
