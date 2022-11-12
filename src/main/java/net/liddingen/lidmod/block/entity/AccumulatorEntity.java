package net.liddingen.lidmod.block.entity;

import net.liddingen.lidmod.item.ModItems;
import net.liddingen.lidmod.networking.ModMessages;
import net.liddingen.lidmod.networking.packet.EnergySyncS2CPacket;
import net.liddingen.lidmod.screen.AccumulatorMenu;
import net.liddingen.lidmod.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AccumulatorEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public AccumulatorEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACCUMULATOR.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AccumulatorEntity.this.progress;
                    case 1 -> AccumulatorEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AccumulatorEntity.this.progress = value;
                    case 1 -> AccumulatorEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Accumulator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AccumulatorMenu(id, inventory, this, this.data);
    }

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();//Energy

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        } //Energy

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE); //Energy
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate(); //Energy
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("accumulator.progress", this.progress);
        nbt.putInt("accumulator.energy", ENERGY_STORAGE.getEnergyStored()); //Energy

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("accumulator_progress");
        ENERGY_STORAGE.setEnergy(nbt.getInt("accumulator.energy")); //Energy
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AccumulatorEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (hasAccumulatorSlot(pEntity)) {
            pEntity.ENERGY_STORAGE.receiveEnergy(64,false);
        }

        if (hasRecipe(pEntity) && hasEnoughEnergy(pEntity)) {
            pEntity.progress++;
            extractEnergy(pEntity);
            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private static void extractEnergy(AccumulatorEntity pEntity) {
        pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy(AccumulatorEntity pEntity) {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * pEntity.maxProgress;
    }

    private static boolean hasAccumulatorSlot(AccumulatorEntity pEntity) {
       return pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.THUNDER_JUG.get();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(AccumulatorEntity pEntity) {

        if(hasRecipe(pEntity)) {

            boolean hasJugInFirstSlot = pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.JUG.get();
            boolean hasThunderJugInFirstSlot = pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.THUNDER_JUG.get();
            pEntity.itemHandler.extractItem(1, 1, false);

            if (hasJugInFirstSlot) {
                pEntity.itemHandler.setStackInSlot(2, new ItemStack(ModItems.THUNDER_JUG.get(),  //hier steht auch was zu ergebnis!
                    pEntity.itemHandler.getStackInSlot(2).getCount() + 1));

            } else if (hasThunderJugInFirstSlot) {
                pEntity.itemHandler.setStackInSlot(2, new ItemStack(ModItems.JUG.get(),
                        pEntity.itemHandler.getStackInSlot(2).getCount() + 1));
            }

            pEntity.resetProgress();
        }

    }

    private static boolean hasRecipe(AccumulatorEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        boolean hasJugInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.JUG.get();
        boolean hasThunderJugInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.THUNDER_JUG.get();

        return (hasJugInFirstSlot && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, new ItemStack(ModItems.THUNDER_JUG.get(), 1))) ||  //gefüllter thunder Jug erstellen

                (hasThunderJugInFirstSlot && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, new ItemStack(ModItems.JUG.get(), 1)));

    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }

    //Energy

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(51200, 320) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };
    private static final int ENERGY_REQ = 32;

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }
}
