package net.liddingen.lidmod.entity.client;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SnailModel extends AnimatedGeoModel<SnailEntity> {

    @Override
    public ResourceLocation getModelResource(SnailEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "geo/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SnailEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/snail_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SnailEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "animations/snail.animation.json");
    }
}