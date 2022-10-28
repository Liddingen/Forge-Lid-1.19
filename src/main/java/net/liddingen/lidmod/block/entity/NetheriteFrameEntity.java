package net.liddingen.lidmod.block.entity;

import net.liddingen.lidmod.LidMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NetheriteFrameEntity extends BlockEntity {

    private static final int MAX_PROGRESS = 100;
    private int progress;

    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final LazyOptional<IItemHandlerModifiable> optional = LazyOptional.of(() -> this.inventory);
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {  //SUS
            return switch (index) {
                case 0 -> NetheriteFrameEntity.this.progress;
                case 1 -> NetheriteFrameEntity.MAX_PROGRESS;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {   //SUS
            switch(index) {
                case 0 -> NetheriteFrameEntity.this.progress = value;
                default -> {}
            }

        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public void tick() {
        if (level == null)
            return;

        progress++;
        if (progress > MAX_PROGRESS) {
            progress = 0;
            var pig = new Pig(EntityType.PIG, this.level);
            pig.setPos(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ());
            this.level.addFreshEntity(pig);
        }

    }

    public static final Component TITLE = Component.translatable("container." + LidMod.MOD_ID + ".netherite_frame");

    public NetheriteFrameEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NETHERITE_FRAME.get(), pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.progress = nbt.getInt("Progress");
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("Progress", this.progress);
        nbt.put("Inventory", this.inventory.serializeNBT());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.optional.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
       this.optional.invalidate();
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ContainerData getContainerData() {
        return this.data;

    }
}
