package net.liddingen.lidmod.entity.client.armor;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.item.custom.SnailHouseItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SnailHouseModel extends AnimatedGeoModel<SnailHouseItem> {
    @Override
    public ResourceLocation getModelResource(SnailHouseItem object) {
        return new ResourceLocation(LidMod.MOD_ID, "geo/snail_house.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SnailHouseItem object) {
        return new ResourceLocation(LidMod.MOD_ID, "textures/models/armor/snail_house_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SnailHouseItem animatable) {
        return new ResourceLocation(LidMod.MOD_ID, "animations/snail_house_animation.json");
    }
}
