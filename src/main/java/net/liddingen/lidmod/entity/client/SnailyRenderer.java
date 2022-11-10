package net.liddingen.lidmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.liddingen.lidmod.entity.custom.SnailyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SnailyRenderer extends GeoEntityRenderer<SnailyEntity> {
    public SnailyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnailyModel());
        this.shadowRadius = 0.3f;   //Shadow
       }
    @Override
    public ResourceLocation getTextureLocation(SnailyEntity instance) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/snaily_texture.png");
    }


    @Override
    public RenderType getRenderType(SnailyEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        if(animatable.isBaby()) {
            stack.scale(0.8F, 0.8F, 0.8F);  //Size of baby x/y/z
        } else {
            stack.scale(1.6f, 1.6f, 1.6f);  //Size x/y/z
        }
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}

