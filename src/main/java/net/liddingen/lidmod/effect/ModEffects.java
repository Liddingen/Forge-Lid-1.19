package net.liddingen.lidmod.effect;

import net.liddingen.lidmod.LidMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, LidMod.MOD_ID);

    public static final RegistryObject<MobEffect> HIDING = MOB_EFFECTS.register("hide",
            () -> new HidingEffect(MobEffectCategory.BENEFICIAL, 151515));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
