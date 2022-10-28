package net.liddingen.lidmod.menus;

import net.liddingen.lidmod.block.ModBlocks;
import net.liddingen.lidmod.block.entity.NetheriteFrameEntity;
import net.liddingen.lidmod.menu.Menu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NetheriteFrameMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess levelAccess;
    private final ContainerData data;

    protected NetheriteFrameMenu(int id, Inventory playerInv, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(Menu.NETHERITE_FRAME.get(), id);
        this.levelAccess = ContainerLevelAccess.create(playerInv.player.getLevel(), pos);
        this.data = data;

        addSlot(new SlotItemHandler(slots,0,20,20));  //X/Y coords

        final int slotSizePlus2 = 18;
        final int startX = 8;
        final int startY = 84;
        final int hotbarY = 142;
        for(int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                addSlot(new Slot(playerInv, column + row * 9 + 9, startX + column * slotSizePlus2,
                        startY + row * slotSizePlus2));
            }
        }

        for (int column = 0; column < 9; ++column) {
            addSlot(new Slot(playerInv, column, startX + column * slotSizePlus2, hotbarY));
        }

        this.addDataSlots(this.data);
    }

    public static NetheriteFrameMenu getClientMenu(int id, Inventory playerInv) {
        return new NetheriteFrameMenu(id, playerInv, new ItemStackHandler(1), BlockPos.ZERO, new SimpleContainerData(1));
    }

    public static MenuConstructor getServerMenu(NetheriteFrameEntity blockEntity, BlockPos pos) {
        return (id, playerInv, player) -> new NetheriteFrameMenu(id, playerInv, blockEntity.getInventory(), pos, blockEntity.getContainerData());
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack current = slot.getItem();
            itemStack = current.copy();
            if (index < 1) {
                if (!this.moveItemStackTo(current, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                } else if (!this.moveItemStackTo(current, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }

                if (current.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
            }

            return itemStack;
        }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.levelAccess, player, ModBlocks.NETHERITE_FRAME.get());
    }

    public ContainerData getData() {
        return data;
    }
}
