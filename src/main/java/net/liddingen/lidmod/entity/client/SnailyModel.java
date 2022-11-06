package net.liddingen.lidmod.entity.client;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.liddingen.lidmod.entity.custom.SnailyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SnailyModel extends AnimatedGeoModel<SnailyEntity> {
    @Override
    public ResourceLocation getModelResource(SnailyEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "geo/snaily.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SnailyEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/snaily_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SnailyEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "animations/snaily.animation.json");
    }
}