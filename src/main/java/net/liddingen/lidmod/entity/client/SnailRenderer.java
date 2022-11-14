package net.liddingen.lidmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.liddingen.lidmod.entity.layers.SnailSaddleLayer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.renderer.entity.layer.GeoExampleLayer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SnailRenderer extends GeoEntityRenderer<SnailEntity> {

    public SnailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnailModel());
        this.shadowRadius = 5f;   //Shadow
        addLayer(new SnailSaddleLayer(this));

        /*super(p_174167_, new LlamaModel<>(p_174167_.bakeLayer(ModelLayers.HORSE)), 1.1F);
        this.addLayer(new SaddleLayer(this, new PigRenderer<>(renderManager.bakeLayer(ModelLayers.PIG_SADDLE))
                , new ResourceLocation(LidMod.MOD_ID,"textures/entity/snail_saddle.png")));

         */
       /* this.addLayer(new SaddleLayer<>(this, new SnailModel<>, (renderManager.bakeLayer(ModelLayers.STRIDER_SADDLE))
                ,new ResourceLocation(LidMod.MOD_ID,"textures/entity/snail_saddle.png")));*/
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

 /*Saddle
        //this.addLayer(new Layer(this));
        this.addLayer(new TofuPigTypeLayer(this));
        this.addLayer(new SaddleLayer<>(this, new PigModel<>(p_174304_.bakeLayer(ModelLayers.PIG_SADDLE)),
         new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }
    */

