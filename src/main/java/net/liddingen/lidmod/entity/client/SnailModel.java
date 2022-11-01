package net.liddingen.lidmod.entity.client;


import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SnailModel extends AnimatedGeoModel<SnailEntity> {
    @Override
    public ResourceLocation getModelResource(SnailEntity object) {
        return new ResourceLocation(LidMod.MOD_ID, "geo/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SnailEntity object) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/snail_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SnailEntity animatable) {
        return new ResourceLocation(LidMod.MOD_ID, "animations/snail.animation.json");
    }
}