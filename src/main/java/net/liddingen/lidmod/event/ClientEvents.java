package net.liddingen.lidmod.event;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.block.entity.ModBlockEntities;
import net.liddingen.lidmod.block.entity.renderer.NetheriteFrameEntityRenderer;
import net.liddingen.lidmod.networking.ModMessages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = LidMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        }

@Mod.EventBusSubscriber(modid = LidMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
            @SubscribeEvent
            public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
                event.registerBlockEntityRenderer(ModBlockEntities.NETHERITE_FRAME.get(),
                        NetheriteFrameEntityRenderer::new);
            }
        }
    }