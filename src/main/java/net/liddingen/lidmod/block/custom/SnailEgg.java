package net.liddingen.lidmod.block.custom;


import com.google.common.annotations.VisibleForTesting;
import net.liddingen.lidmod.entity.ModEntityTypes;
import net.liddingen.lidmod.entity.custom.SnailyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrogspawnBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SnailEgg extends FrogspawnBlock {
    private static final int MIN_TADPOLES_SPAWN = 2;
    private static final int MAX_TADPOLES_SPAWN = 5;
    private static final int DEFAULT_MIN_HATCH_TICK_DELAY = 3600;
    private static final int DEFAULT_MAX_HATCH_TICK_DELAY = 12000;
    private static int minHatchTickDelay = 3600;
    private static int maxHatchTickDelay = 12000;

    public SnailEgg(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE =
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.5D, 16.0D);

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
    public boolean canSurvive(BlockState p_221209_, LevelReader p_221210_, BlockPos p_221211_) {
        return mayPlaceOn(p_221210_, p_221211_.below());
    }

    public void onPlace(BlockState p_221227_, Level p_221228_, BlockPos p_221229_, BlockState p_221230_, boolean p_221231_) {
        p_221228_.scheduleTick(p_221229_, this, getSnailyspawnHatchDelay(p_221228_.getRandom()));
    }

    private static int getSnailyspawnHatchDelay(RandomSource p_221186_) {
        return p_221186_.nextInt(minHatchTickDelay, maxHatchTickDelay);
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos1) {
        return !this.canSurvive(state, levelAccessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, blockState, levelAccessor, pos, pos1);
    }

    public void tick(BlockState p_221194_, ServerLevel p_221195_, BlockPos p_221196_, RandomSource p_221197_) {
        if (!this.canSurvive(p_221194_, p_221195_, p_221196_)) {
            this.destroyBlock(p_221195_, p_221196_);
        } else {
            this.hatchSnailyspawn(p_221195_, p_221196_, p_221197_);
        }
    }

    public void entityInside(BlockState p_221204_, Level p_221205_, BlockPos p_221206_, Entity p_221207_) {
        if (p_221207_.getType().equals(EntityType.FALLING_BLOCK)) {
            this.destroyBlock(p_221205_, p_221206_);
        }

    }

    private static boolean mayPlaceOn(BlockGetter getter, BlockPos pos) {
        BlockState blockState = getter.getBlockState(pos);
        //BlockState blockState1 = getter.getBlockState(pos.above());
        return blockState.getBlock() == Blocks.DIRT ;
    }

    private void hatchSnailyspawn(ServerLevel level, BlockPos pos, RandomSource source) {
        this.destroyBlock(level, pos);
        level.playSound((Player)null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.spawnSnailys(level, pos, source);
    }

    private void destroyBlock(Level p_221191_, BlockPos p_221192_) {
        p_221191_.destroyBlock(p_221192_, false);
    }

    private void spawnSnailys(ServerLevel p_221221_, BlockPos p_221222_, RandomSource p_221223_) {
        int i = p_221223_.nextInt(2, 6);

        for(int j = 1; j <= i; ++j) {
            SnailyEntity snaily = ModEntityTypes.SNAILY.get().create(p_221221_);
            snaily.setAge(-24000);
            double d0 = (double)p_221222_.getX() + this.getRandomTadpolePositionOffset(p_221223_);
            double d1 = (double)p_221222_.getZ() + this.getRandomTadpolePositionOffset(p_221223_);
            int k = p_221223_.nextInt(1, 361);
            snaily.moveTo(d0, (double)p_221222_.getY() + 0.5D, d1, (float)k, 0.0F);
            snaily.setPersistenceRequired();
            p_221221_.addFreshEntity(snaily);
        }

    }

    private double getRandomTadpolePositionOffset(RandomSource p_221225_) {
        double d0 = (double)(Tadpole.HITBOX_WIDTH / 2.0F);
        return Mth.clamp(p_221225_.nextDouble(), d0, 1.0D - d0);
    }

    @VisibleForTesting
    public static void setHatchDelay(int p_221179_, int p_221180_) {
        minHatchTickDelay = p_221179_;
        maxHatchTickDelay = p_221180_;
    }

    @VisibleForTesting
    public static void setDefaultHatchDelay() {
        minHatchTickDelay = 3600;
        maxHatchTickDelay = 12000;
    }
}
