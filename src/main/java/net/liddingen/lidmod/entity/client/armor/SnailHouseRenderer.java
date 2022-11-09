package net.liddingen.lidmod.entity.client.armor;

import net.liddingen.lidmod.item.custom.SnailHouseItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SnailHouseRenderer extends GeoArmorRenderer<SnailHouseItem> {
    public SnailHouseRenderer() {
        super(new SnailHouseModel());

        this.bodyBone = "armorBody";

    }
}
