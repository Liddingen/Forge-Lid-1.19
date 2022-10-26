package net.liddingen.lidmod.painting;

import net.liddingen.lidmod.LidMod;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, LidMod.MOD_ID);

    public static final RegistryObject<PaintingVariant> RECIPE = PAINTING_VARIANTS.register("recipe",
            () -> new PaintingVariant(48,48));

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
