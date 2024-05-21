package com.github.minecraftschurlimods.bibliocraft.content.dinnerplate;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCEntityBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class DinnerPlateBlock extends BCEntityBlock {
    public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 0, 3);
    public static final VoxelShape SHAPE = ShapeUtil.combine(
            Shapes.box(0.25, 0, 0.3125, 0.75, 0.0625, 0.6875),
            Shapes.box(0.1875, 0, 0.375, 0.25, 0.0625, 0.625),
            Shapes.box(0.75, 0, 0.375, 0.8125, 0.0625, 0.625),
            Shapes.box(0.71875, 0.03125, 0.34375, 0.78125, 0.09375, 0.65625),
            Shapes.box(0.34375, 0.03125, 0.21875, 0.65625, 0.09375, 0.28125),
            Shapes.box(0.21875, 0.03125, 0.34375, 0.28125, 0.09375, 0.65625),
            Shapes.box(0.34375, 0.03125, 0.71875, 0.65625, 0.09375, 0.78125),
            Shapes.box(0.28125, 0.03125, 0.28125, 0.40625, 0.09375, 0.34375),
            Shapes.box(0.28125, 0.03125, 0.34375, 0.34375, 0.09375, 0.40625),
            Shapes.box(0.59375, 0.03125, 0.28125, 0.71875, 0.09375, 0.34375),
            Shapes.box(0.65625, 0.03125, 0.34375, 0.71875, 0.09375, 0.40625),
            Shapes.box(0.59375, 0.03125, 0.65625, 0.71875, 0.09375, 0.71875),
            Shapes.box(0.65625, 0.03125, 0.59375, 0.71875, 0.09375, 0.65625),
            Shapes.box(0.28125, 0.03125, 0.65625, 0.40625, 0.09375, 0.71875),
            Shapes.box(0.28125, 0.03125, 0.59375, 0.34375, 0.09375, 0.65625));
    public static final MapCodec<DinnerPlateBlock> CODEC = simpleCodec(DinnerPlateBlock::new);

    public DinnerPlateBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(WATERLOGGED, false).setValue(PROGRESS, 0));
    }

    @Override
    protected MapCodec<DinnerPlateBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DinnerPlateBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockState newState = state;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof DinnerPlateBlockEntity plate))
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        ItemStack slotStack = plate.getItem(0);
        if (stack.getFoodProperties(player) != null) {
            if (slotStack.isEmpty()) {
                ItemStack foodStack = stack.copy();
                foodStack.setCount(1);
                plate.setItem(0, foodStack);
                stack.shrink(1);
                newState = newState.setValue(PROGRESS, 0);
            } else {
                newState = newState.setValue(PROGRESS, newState.getValue(PROGRESS) + 1);
                triggerItemUseEffects(player, slotStack, 5);
            }
        } else {
            newState = newState.setValue(PROGRESS, newState.getValue(PROGRESS) + 1);
            triggerItemUseEffects(player, slotStack, 5);
        }
        if (newState.getValue(PROGRESS) == 3) {
            newState = newState.setValue(PROGRESS, 0);
            triggerItemUseEffects(player, slotStack, 16);
            player.eat(level, slotStack);
            plate.setItem(0, ItemStack.EMPTY);
        }
        level.setBlock(pos, newState, Block.UPDATE_ALL);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PROGRESS);
    }

    /**
     * @see net.minecraft.world.entity.LivingEntity#triggerItemUseEffects(ItemStack, int)
     */
    private void triggerItemUseEffects(Player player, ItemStack stack, int amount) {
        if (stack.getUseAnimation() == UseAnim.DRINK) {
            player.playSound(stack.getDrinkingSound(), 0.5f, player.level().random.nextFloat() * 0.1f + 0.9f);
        }
        if (stack.getUseAnimation() == UseAnim.EAT) {
            RandomSource random = player.getRandom();
            for (int i = 0; i < amount; ++i) {
                Vec3 speed = new Vec3(((double) random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0).xRot(-player.getXRot() * (float) (Math.PI / 180)).yRot(-player.getYRot() * (float) (Math.PI / 180));
                Vec3 pos = new Vec3(((double) random.nextFloat() - 0.5) * 0.3, (double) (-random.nextFloat()) * 0.6 - 0.3, 0.6).xRot(-player.getXRot() * (float) (Math.PI / 180)).yRot(-player.getYRot() * (float) (Math.PI / 180)).add(player.getX(), player.getEyeY(), player.getZ());
                player.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.x, pos.y, pos.z, speed.x, speed.y + 0.05, speed.z);
            }
            player.playSound(player.getEatingSound(stack), 0.5f + 0.5f * (float) random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2f + 1f);
        }
    }
}
