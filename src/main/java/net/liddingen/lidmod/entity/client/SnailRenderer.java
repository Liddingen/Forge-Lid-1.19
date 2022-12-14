package net.liddingen.lidmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.liddingen.lidmod.entity.layers.SnailChestLayer;
import net.liddingen.lidmod.entity.layers.SnailSaddleLayer;
import net.liddingen.lidmod.entity.layers.SnailSwagLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SnailRenderer extends GeoEntityRenderer<SnailEntity> {

    public SnailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnailModel());
        this.shadowRadius = 5f;   //Shadow

        addLayer(new SnailSaddleLayer(this));
        addLayer(new SnailChestLayer(this));
        addLayer(new SnailSwagLayer(this));

       }
    @Override
    public ResourceLocation getTextureLocation(SnailEntity instance) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/snail_texture.png");
    }

    @Override
    public RenderType getRenderType(SnailEntity entity, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(3.5f, 3.5f, 3.5f);  //Size x/y/z
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}

