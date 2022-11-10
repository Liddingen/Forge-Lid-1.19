package net.liddingen.lidmod.event;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.block.entity.ModBlockEntities;
import net.liddingen.lidmod.block.entity.renderer.NetheriteFrameEntityRenderer;
import net.liddingen.lidmod.entity.client.armor.SnailHouseRenderer;
import net.liddingen.lidmod.item.custom.SnailHouseItem;
import net.liddingen.lidmod.networking.ModMessages;
import net.liddingen.lidmod.networking.packet.ShellHidingC2SPacket;
import net.liddingen.lidmod.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = LidMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.HIDING_KEY.consumeClick()) {
                ModMessages.sendToServer(new ShellHidingC2SPacket()); //What happens if "x" is pressed
            }
        }
    }

@Mod.EventBusSubscriber(modid = LidMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
            @SubscribeEvent
            public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
                event.registerBlockEntityRenderer(ModBlockEntities.NETHERITE_FRAME.get(),
                        NetheriteFrameEntityRenderer::new);
            }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.HIDING_KEY);
    }

    @SubscribeEvent
    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(SnailHouseItem.class, () -> new SnailHouseRenderer());
            }
    }
}