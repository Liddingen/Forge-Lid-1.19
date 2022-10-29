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

    public static final RegistryObject<PaintingVariant> RECIPE_ACCUMULATOR = PAINTING_VARIANTS.register("recipe_accumulator",
            () -> new PaintingVariant(48,48));

    public static final RegistryObject<PaintingVariant> RECIPE_COMPRESS_STATION = PAINTING_VARIANTS.register("recipe_compress_station",
            () -> new PaintingVariant(48,48));

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
