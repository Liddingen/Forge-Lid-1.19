package net.liddingen.lidmod;

import com.mojang.logging.LogUtils;
import net.liddingen.lidmod.block.ModBlocks;
import net.liddingen.lidmod.block.entity.ModBlockEntities;
import net.liddingen.lidmod.entity.ModEntityTypes;
import net.liddingen.lidmod.entity.client.SnailRenderer;
import net.liddingen.lidmod.entity.client.SnailyRenderer;
import net.liddingen.lidmod.item.ModItems;
import net.liddingen.lidmod.networking.ModMessages;
import net.liddingen.lidmod.painting.ModPaintings;
import net.liddingen.lidmod.screen.AccumulatorScreen;
import net.liddingen.lidmod.screen.ModMenuTypes;
import net.liddingen.lidmod.screen.NetheriteFrameScreen;
import net.liddingen.lidmod.villager.ModVillagers;
import net.liddingen.lidmod.world.feature.ModConfiguredFeatures;
import net.liddingen.lidmod.world.feature.ModPlacedFeatures;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LidMod.MOD_ID)
public class LidMod {
    public static final String MOD_ID = "lidmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public LidMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModPaintings.register(modEventBus);

        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        ModVillagers.register(modEventBus);
        ModEntityTypes.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        GeckoLib.initialize();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
            ModVillagers.registerPOIs();
        });
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.ACCUMULATOR_MENU.get(), AccumulatorScreen::new);
            MenuScreens.register(ModMenuTypes.NETHERITE_FRAME_MENU.get(), NetheriteFrameScreen::new);

            EntityRenderers.register(ModEntityTypes.SNAIL.get(), SnailRenderer::new);
            EntityRenderers.register(ModEntityTypes.SNAILY.get(), SnailyRenderer::new);
        }
    }
}
