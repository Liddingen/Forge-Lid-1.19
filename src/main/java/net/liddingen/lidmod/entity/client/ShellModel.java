package net.liddingen.lidmod.entity.client;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.entity.custom.ShellEntity;
import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShellModel extends AnimatedGeoModel<ShellEntity> {
    @Override
    public ResourceLocation getModelResource(ShellEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "geo/shell.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShellEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/entity/shell_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShellEntity entity) {
        return new ResourceLocation(LidMod.MOD_ID, "animations/shell.animation.json");
    }
}