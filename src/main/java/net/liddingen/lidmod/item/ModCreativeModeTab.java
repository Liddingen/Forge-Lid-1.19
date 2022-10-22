package net.liddingen.lidmod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab LID_TAB = new CreativeModeTab("lidtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ELEKTRUM.get());
        }
    };
}
