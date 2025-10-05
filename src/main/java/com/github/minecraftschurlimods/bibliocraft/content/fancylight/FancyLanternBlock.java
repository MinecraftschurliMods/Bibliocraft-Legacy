package com.github.minecraftschurlimods.bibliocraft.content.fancylight;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.Lazy;

public class FancyLanternBlock extends AbstractFancyLightBlock {
    private static final VoxelShape STANDING_SHAPE = ShapeUtil.combine(
            Shapes.box(0.25, 0, 0.25, 0.75, 0.0625, 0.75),
            Shapes.box(0.3125, 0.0625, 0.3125, 0.375, 0.625, 0.375),
            Shapes.box(0.625, 0.0625, 0.3125, 0.6875, 0.625, 0.375),
            Shapes.box(0.3125, 0.0625, 0.625, 0.375, 0.625, 0.6875),
            Shapes.box(0.625, 0.0625, 0.625, 0.6875, 0.625, 0.6875),
            Shapes.box(0.34375, 0.0625, 0.34375, 0.65625, 0.625, 0.65625),
            Shapes.box(0.25, 0.625, 0.25, 0.75, 0.6875, 0.75),
            Shapes.box(0.3125, 0.6875, 0.3125, 0.6875, 0.75, 0.6875),
            Shapes.box(0.375, 0.75, 0.375, 0.625, 0.8125, 0.625));
    private static final VoxelShape HANGING_SHAPE = ShapeUtil.combine(STANDING_SHAPE,
            Shapes.box(0.40625, 0.8125, 0.40625, 0.59375, 1.0, 0.59375));
    private static final VoxelShape NORTH_WALL_SHAPE = ShapeUtil.combine(STANDING_SHAPE,
            Shapes.box(0.4375, 0.65625, 0.9375, 0.5625, 0.90625, 1),
            Shapes.box(0.46875, 0.70625, 0.875, 0.53125, 0.83125, 0.9375),
            Shapes.box(0.46875, 0.8125, 0.46875, 0.53125, 0.925, 0.9375));
    private static final VoxelShape EAST_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.COUNTERCLOCKWISE_90);
    private static final ResourceLocation DEFAULT_PARTICLE = BCUtil.mcLoc("small_flame");
    private final Lazy<ParticleOptions> particle;

    public FancyLanternBlock(Properties properties) {
        this(properties, DEFAULT_PARTICLE);
    }

    public FancyLanternBlock(Properties properties, ResourceLocation particle) {
        super(properties);
        this.particle = Lazy.of(() -> BuiltInRegistries.PARTICLE_TYPE.getValue(particle) instanceof ParticleOptions options ? options : null);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(TYPE)) {
            case STANDING -> STANDING_SHAPE;
            case HANGING -> HANGING_SHAPE;
            case WALL -> switch (state.getValue(FACING)) {
                default -> NORTH_WALL_SHAPE;
                case SOUTH -> SOUTH_WALL_SHAPE;
                case WEST -> WEST_WALL_SHAPE;
                case EAST -> EAST_WALL_SHAPE;
            };
        };
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        Vec3 offset = Vec3.atCenterOf(pos);
        if (random.nextFloat() < 0.3f) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0, 0, 0);
        }
        level.addParticle(BCUtil.nonNull(particle.get()), offset.x, offset.y, offset.z, 0, 0, 0);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty() && player.getAbilities().mayBuild && state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL_IMMEDIATE);
            level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1f, 1f);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        return itemAbility == ItemAbilities.FIRESTARTER_LIGHT && !state.getValue(LIT) ? state.setValue(LIT, true) : super.getToolModifiedState(state, context, itemAbility, simulate);
    }
}
