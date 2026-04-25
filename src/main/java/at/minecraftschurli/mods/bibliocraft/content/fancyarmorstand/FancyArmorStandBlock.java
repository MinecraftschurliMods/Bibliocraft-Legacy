package at.minecraftschurli.mods.bibliocraft.content.fancyarmorstand;

import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import at.minecraftschurli.mods.bibliocraft.util.ShapeUtil;
import at.minecraftschurli.mods.bibliocraft.util.block.BCBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.block.BCFacingInteractibleBlock;
import at.minecraftschurli.mods.bibliocraft.util.block.BCItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LevelEvent;
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
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class FancyArmorStandBlock extends BCFacingInteractibleBlock {
    private static final VoxelShape Z_SHAPE_BOTTOM = ShapeUtil.combine(
            Shapes.box(0, 0, 0, 1, 0.125, 1),
            Shapes.box(0.375, 0.125, 0.375, 0.625, 1, 0.625));
    private static final VoxelShape Z_SHAPE_TOP = ShapeUtil.combine(
            Shapes.box(0.375, 0, 0.375, 0.625, 0.875, 0.625),
            Shapes.box(0.0625, 0.125, 0.375, 0.9375, 0.5, 0.625));
    private static final VoxelShape X_SHAPE_BOTTOM = ShapeUtil.rotate(Z_SHAPE_BOTTOM, Rotation.CLOCKWISE_90);
    private static final VoxelShape X_SHAPE_TOP = ShapeUtil.rotate(Z_SHAPE_TOP, Rotation.CLOCKWISE_90);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final List<EquipmentSlot> EQUIPMENT_SLOTS = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

    public FancyArmorStandBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE_TOP : Z_SHAPE_TOP;
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE_BOTTOM : Z_SHAPE_BOTTOM;
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos currentPos, Direction facing, BlockPos neighborPos, BlockState facingState, RandomSource random) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (facing.getAxis() == Direction.Axis.Y && half == DoubleBlockHalf.LOWER == (facing == Direction.UP) && (!facingState.is(this) || facingState.getValue(HALF) == half))
            return Blocks.AIR.defaultBlockState();
        return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, scheduledTickAccess, currentPos, facing, neighborPos, facingState, random);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        return level.isInsideBuildHeight(pos.getY() + 1) && level.getBlockState(pos.above()).canBeReplaced(context)
                ? BCUtil.nonNull(super.getStateForPlacement(context))
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HALF, DoubleBlockHalf.LOWER)
                : super.getStateForPlacement(context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) return super.canSurvive(state, level, pos);
        BlockState blockstate = level.getBlockState(pos.below());
        if (state.getBlock() != this) return super.canSurvive(state, level, pos);
        return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        BlockPos above = pos.above();
        level.setBlock(above, DoublePlantBlock.copyWaterloggedFrom(level, above, state.setValue(HALF, DoubleBlockHalf.UPPER)), Block.UPDATE_ALL);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            if (player.isCreative()) {
                //Copy of protected method DoublePlantBlock#preventDropFromBottomPart
                if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    BlockPos newPos = pos.below();
                    BlockState newState = level.getBlockState(newPos);
                    if (newState.is(state.getBlock()) && newState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                        level.setBlock(newPos, newState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                        level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(newState));
                    }
                }
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack stack) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), be, stack);
    }

    @SuppressWarnings("deprecation")
    @Override
    public long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        boolean isPowered = level.hasNeighborSignal(pos);
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.below();
            isPowered = isPowered || level.hasNeighborSignal(pos);
        } else {
            isPowered = isPowered || level.hasNeighborSignal(pos.above());
        }
        if (!player.isSecondaryUseActive() || !canAccessFromDirection(state, hit.getDirection())) {
            return super.useWithoutItem(state, level, pos, player, hit);
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof BCBlockEntity bcbe)) {
            return super.useWithoutItem(state, level, pos, player, hit);
        }
        BCItemHandler itemHandler = bcbe.getItemHandler();
        if (isPowered && trySwapArmorSet(itemHandler, player)) {
            return InteractionResult.SUCCESS;
        }
        int slot = lookingAtSlot(state, hit);
        EquipmentSlot equipmentSlot = EQUIPMENT_SLOTS.get(slot);
        ItemStack itemBySlot = player.getItemBySlot(equipmentSlot);
        if (BCUtil.swapItem(itemBySlot, s -> player.setItemSlot(equipmentSlot, s, true), itemHandler, slot, null)) {
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    private static boolean trySwapArmorSet(BCItemHandler itemHandler, Player player) {
        try (Transaction transaction = Transaction.openRoot()) {
            for (int slot = 0; slot < EQUIPMENT_SLOTS.size(); slot++) {
                EquipmentSlot equipmentSlot = EQUIPMENT_SLOTS.get(slot);
                ItemStack itemBySlot = player.getItemBySlot(equipmentSlot);
                if (!BCUtil.swapItem(itemBySlot, s -> player.setItemSlot(equipmentSlot, s, true), itemHandler, slot, transaction)) {
                    return false;
                }
            }
            transaction.commit();
        }
        return true;
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        boolean isPowered = level.hasNeighborSignal(pos);
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.below();
            isPowered = isPowered || level.hasNeighborSignal(pos);
        } else {
            isPowered = isPowered || level.hasNeighborSignal(pos.above());
        }
        if (!player.isSecondaryUseActive() || !canAccessFromDirection(state, hit.getDirection())) {
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof BCBlockEntity bcbe)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        }
        BCItemHandler itemHandler = bcbe.getItemHandler();
        if (stack.isEmpty() && isPowered && trySwapArmorSet(itemHandler, player)) {
            return InteractionResult.SUCCESS;
        }
        int slot = lookingAtSlot(state, hit);
        int playerSlot = hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() : Inventory.SLOT_OFFHAND;
        if (slot != -1 && BCUtil.swapItem(stack, s -> {
            player.getInventory().setItem(playerSlot, s);
            if (s.get(DataComponents.EQUIPPABLE) instanceof Equippable equipable) {
                level.playSound(null, player, equipable.equipSound().value(), SoundSource.PLAYERS, 1, 1);
            }
        }, itemHandler, slot, null)) {
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public int lookingAtSlot(BlockState state, BlockHitResult hit) {
        EquipmentSlot slot;
        double y = hit.getLocation().y - hit.getBlockPos().getY();
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            if (y < 0.5) {
                slot = EquipmentSlot.CHEST;
            } else {
                slot = EquipmentSlot.HEAD;
            }
        } else {
            if (y < 0.4375) {
                slot = EquipmentSlot.FEET;
            } else {
                slot = EquipmentSlot.LEGS;
            }
        }
        return 3 - slot.getIndex();
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return direction.getAxis() != Direction.Axis.Y;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? new FancyArmorStandBlockEntity(pos, state) : null;
    }
}
