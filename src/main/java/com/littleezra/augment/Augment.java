package com.littleezra.augment;

import com.littleezra.augment.block.ModBlocks;
import com.littleezra.augment.entity.ModEntityTypes;
import com.littleezra.augment.entity.client.ScutlRenderer;
import com.littleezra.augment.entity.client.SentinelRenderer;
import com.littleezra.augment.item.ModItems;
import com.littleezra.augment.item.enchantment.ModEnchantments;
import com.littleezra.augment.sound.ModSounds;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Augment.MOD_ID)
public class Augment
{
    public static final String MOD_ID = "augment";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Augment() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModSounds.register(modEventBus);

        ModEnchantments.register(modEventBus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void printDebug(String line){
        System.out.println(MOD_ID + ": [DEBUG] " + line);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntityTypes.SENTINEL.get(), SentinelRenderer::new);
            EntityRenderers.register(ModEntityTypes.SCUTL.get(), ScutlRenderer::new);
        }
    }
}
