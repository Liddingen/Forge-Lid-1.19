package net.liddingen.lidmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SnailRenderer extends GeoEntityRenderer<SnailEntity> {
    public SnailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnailModel());
        this.shadowRadius = 5f;   //Shadow
    }

    @Override
    public ResourceLocation getTextureLocation(SnailEntity instance) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/snail_texture.png");
    }

    @Override
    public RenderType getRenderType(SnailEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(3.5f, 3.5f, 3.5f);  //Größe in alle x/y/z richtungen ändern
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
