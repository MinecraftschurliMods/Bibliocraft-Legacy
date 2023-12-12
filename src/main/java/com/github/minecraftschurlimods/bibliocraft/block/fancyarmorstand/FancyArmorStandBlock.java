package com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlock;
import com.github.minecraftschurlimods.bibliocraft.block.BCBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.block.BCInteractibleBlock;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class FancyArmorStandBlock extends BCInteractibleBlock {
    private static final VoxelShape BOTTOM_NS = ShapeUtil.combine(
            Shapes.box(0, 0, 0, 1, 0.0625, 1),
            Shapes.box(0.375, 0.0625, 0.375, 0.625, 1, 0.625));
    private static final VoxelShape TOP_NS = ShapeUtil.combine(
            Shapes.box(0.375, 0, 0.375, 0.625, 0.875, 0.625),
            Shapes.box(0.0625, 0.125, 0.375, 0.9375, 0.5, 0.625));
    private static final VoxelShape BOTTOM_EW = ShapeUtil.rotate(BOTTOM_NS, Rotation.CLOCKWISE_90);
    private static final VoxelShape TOP_EW = ShapeUtil.rotate(TOP_NS, Rotation.CLOCKWISE_90);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public FancyArmorStandBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(FancyArmorStandBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? TOP_EW : TOP_NS;
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? BOTTOM_EW : BOTTOM_NS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context) ? defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HALF, DoubleBlockHalf.LOWER) : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        return state.getValue(HALF) == DoubleBlockHalf.LOWER || blockstate.is(this);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            //Copy of protected method DoublePlantBlock#preventCreativeDropFromBottomPart
            DoubleBlockHalf half = state.getValue(HALF);
            if (half == DoubleBlockHalf.UPPER) {
                BlockPos newPos = pos.below();
                BlockState newState = level.getBlockState(newPos);
                if (newState.is(state.getBlock()) && newState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    level.setBlock(newPos, newState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, newPos, Block.getId(newState));
                }
            } else {
                BlockPos newPos = pos.above();
                BlockState newState = level.getBlockState(newPos);
                if (newState.is(state.getBlock()) && newState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    level.setBlock(newPos, newState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, newPos, Block.getId(newState));
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isSecondaryUseActive() && canAccessFromDirection(state, hit.getDirection())) {
            int slot = lookingAtSlot(state, hit);
            if (slot != -1) {
                if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    pos = pos.below();
                }
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof BCBlockEntity bcbe) {
                    ItemStack stack = player.getInventory().getArmor(3 - slot);
                    ItemStack slotStack = bcbe.getItem(slot);
                    if (bcbe.canPlaceItem(slot, stack)) {
                        bcbe.setItem(slot, stack);
                        player.getInventory().setItem(39 - slot, slotStack);
                        if (slotStack.getItem() instanceof Equipable equipable) {
                            level.playSound(null, player, equipable.getEquipSound(), SoundSource.PLAYERS, 1, 1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.below();
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public int lookingAtSlot(BlockState state, BlockHitResult hit) {
        EquipmentSlot slot = null;
        double y = hit.getLocation().y - hit.getBlockPos().getY();
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            if (y < 0.5) {
                slot = EquipmentSlot.CHEST;
            } else if (y < 0.875) {
                slot = EquipmentSlot.HEAD;
            }
        } else {
            if (y < 0.375) {
                slot = EquipmentSlot.FEET;
            } else {
                slot = EquipmentSlot.LEGS;
            }
        }
        return slot != null ? 3 - slot.getIndex() : -1;
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return direction.getAxis() != Direction.Axis.Y;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FancyArmorStandBlockEntity(pos, state);
    }
}
