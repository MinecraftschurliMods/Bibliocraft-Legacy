package com.github.minecraftschurlimods.bibliocraft.block.potionshelf;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCTags;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class PotionShelfBlock extends BCBlock {
    private static final VoxelShape NORTH_SHAPE = ShapeUtil.combine(
            Shapes.box(0, 0.0625, 0.75, 1, 1, 1),
            Shapes.box(0, 0, 0.75, 0.0625, 0.0625, 1),
            Shapes.box(0.9375, 0, 0.75, 1, 0.0625, 1));
    private static final VoxelShape EAST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_SHAPE = ShapeUtil.rotate(NORTH_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public PotionShelfBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotionShelfBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            if (level.isClientSide()) return InteractionResult.SUCCESS;
            NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) level.getBlockEntity(pos), pos);
            return InteractionResult.SUCCESS;
        }
        Direction direction = state.getValue(FACING);
        if (hit.getDirection() != direction && hit.getDirection() != direction.getOpposite())
            return super.use(state, level, pos, player, hand, hit);
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && !stack.is(BCTags.Items.POTION_SHELF_POTIONS))
            return super.use(state, level, pos, player, hand, hit);
        int slot = lookingAtSlot(state, hit);
        if (slot != -1) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PotionShelfBlockEntity potionShelf) {
                ItemStack slotStack = potionShelf.getItem(slot);
                if (!stack.isEmpty() || !slotStack.isEmpty()) {
                    potionShelf.setItem(slot, stack);
                    player.setItemInHand(hand, slotStack);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    private static int lookingAtSlot(BlockState state, BlockHitResult hit) { // constant double values computed from PotionShelfBER#render
        Direction.Axis axis = state.getValue(FACING).getClockWise().getAxis();
        double hitX = 1.125 - (hit.getLocation().get(axis) - hit.getBlockPos().get(axis));
        double hitY = hit.getLocation().y - hit.getBlockPos().getY();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                double minX = 0.171875 + j * 0.21875;
                double maxX = minX + 0.21875;
                double maxY = 0.90625 - i * 0.3125;
                double minY = maxY - 0.21875;
                if (hitX >= minX && hitX < maxX && hitY >= minY && hitY < maxY) return i * 4 + j;
            }
        }
        return -1;
    }
}
