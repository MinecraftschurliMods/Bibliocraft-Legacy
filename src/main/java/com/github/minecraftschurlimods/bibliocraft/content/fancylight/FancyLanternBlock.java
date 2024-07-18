package com.github.minecraftschurlimods.bibliocraft.content.fancylight;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
            Shapes.box(0.46875, 0.8125, 0.46875, 0.53125, 0.9375, 0.53125),
            Shapes.box(0.375, 0.9375, 0.375, 0.625, 1, 0.625));
    private static final VoxelShape NORTH_WALL_SHAPE = ShapeUtil.combine(STANDING_SHAPE,
            Shapes.box(0.4375, 0.65625, 0.9375, 0.5625, 0.90625, 1),
            Shapes.box(0.46875, 0.70625, 0.875, 0.53125, 0.83125, 0.9375),
            Shapes.box(0.46875, 0.8125, 0.46875, 0.53125, 0.925, 0.9375));
    private static final VoxelShape EAST_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.CLOCKWISE_90);
    private static final VoxelShape SOUTH_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.CLOCKWISE_180);
    private static final VoxelShape WEST_WALL_SHAPE = ShapeUtil.rotate(NORTH_WALL_SHAPE, Rotation.COUNTERCLOCKWISE_90);

    public FancyLanternBlock(Properties properties) {
        super(properties);
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
        Vec3 offset = Vec3.atCenterOf(pos);
        if (random.nextFloat() < 0.3f) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0, 0, 0);
        }
        level.addParticle(ParticleTypes.SMALL_FLAME, offset.x, offset.y, offset.z, 0, 0, 0);
    }
}
