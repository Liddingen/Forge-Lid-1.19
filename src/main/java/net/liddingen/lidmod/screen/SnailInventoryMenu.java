package net.liddingen.lidmod.screen;


import net.liddingen.lidmod.entity.custom.SnailEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SnailInventoryMenu extends AbstractContainerMenu {
    private final Container snailContainer;
    private final SnailEntity snail;

    public SnailInventoryMenu(int pContainerId, Inventory pPlayerInventory, Container pContainer, final SnailEntity pSnail) {
        super(ModMenuTypes.SNAIL_MENU.get(), pContainerId);
        this.snailContainer = pContainer;
        this.snail = pSnail;
        int i = 3;
        pContainer.startOpen(pPlayerInventory.player);
        int j = -18;
        this.addSlot(new Slot(pContainer, 0, 8, 18) {

            public boolean mayPlace(ItemStack p_39677_) {
                return p_39677_.is(Items.SADDLE) && !this.hasItem() && pSnail.isSaddleable();
            }


            public boolean isActive() {
                return pSnail.isSaddleable();
            }
        });
        this.addSlot(new Slot(pContainer, 1, 8, 36) {

            public boolean mayPlace(ItemStack p_39690_) {
                return pSnail.isArmor(p_39690_);
            }


            public boolean isActive() {
                return pSnail.canWearArmor();
            }


            public int getMaxStackSize() {
                return 1;
            }
        });
        if (this.hasChest(pSnail)) {
            for(int k = 0; k < 3; ++k) {
                for(int l = 0; l < ((AbstractChestedHorse)pSnail).getInventoryColumns(); ++l) {
                    this.addSlot(new Slot(pContainer, 2 + l + k * ((AbstractChestedHorse)pSnail).getInventoryColumns(), 80 + l * 18, 18 + k * 18));
                }
            }
        }

        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(pPlayerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(pPlayerInventory, j1, 8 + j1 * 18, 142));
        }

    }


    public boolean stillValid(Player pPlayer) {
        return !this.snail.hasInventoryChanged(this.snailContainer) && this.snailContainer.stillValid(pPlayer) && this.snail.isAlive() && this.snail.distanceTo(pPlayer) < 8.0F;
    }

    private boolean hasChest(SnailEntity pSnail) {
        return pSnail.hasChest();
    }

    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.snailContainer.getContainerSize();
            if (pIndex < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (pIndex >= j && pIndex < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= i && pIndex < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.snailContainer.stopOpen(pPlayer);
    }
}
