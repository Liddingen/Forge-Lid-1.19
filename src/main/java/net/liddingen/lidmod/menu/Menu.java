package net.liddingen.lidmod.menu;

import net.liddingen.lidmod.LidMod;
import net.liddingen.lidmod.menus.NetheriteFrameMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Menu {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            LidMod.MOD_ID);

    public static final RegistryObject<MenuType<NetheriteFrameMenu>> NETHERITE_FRAME = MENUS.register("netherite_frame", () ->
            new MenuType<>(NetheriteFrameMenu::getClientMenu));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
