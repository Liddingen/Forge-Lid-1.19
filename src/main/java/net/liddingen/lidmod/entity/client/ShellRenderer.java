package net.liddingen.lidmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.ShellEntity;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class ShellRenderer extends GeoEntityRenderer<ShellEntity> {
    public ShellRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShellModel());
        this.shadowRadius = 0.4f;   //Shadow
       }
    @Override
    public ResourceLocation getTextureLocation(ShellEntity instance) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/shell_texture.png");
    }


    @Override
    public RenderType getRenderType(ShellEntity entity, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(1.2f, 1.2f, 1.2f);  //Size x/y/z
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}

