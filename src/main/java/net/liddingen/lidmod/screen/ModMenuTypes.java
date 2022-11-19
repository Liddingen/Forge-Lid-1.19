package net.liddingen.lidmod.screen;

import net.liddingen.lidmod.LidMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, LidMod.MOD_ID);

    public static final RegistryObject<MenuType<AccumulatorMenu>> ACCUMULATOR_MENU =
            registerMenuType(AccumulatorMenu::new, "accumulator_menu");

    public static final RegistryObject<MenuType<NetheriteFrameMenu>> NETHERITE_FRAME_MENU =
            registerMenuType(NetheriteFrameMenu::new, "netherite_frane_menu");

   /* public static final RegistryObject<MenuType<SnailInventoryMenu>> SAIL_MENU =
            registerMenuType(SnailInventoryMenu::new, "snail_menu");*/


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
