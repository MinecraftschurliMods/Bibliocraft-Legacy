package com.github.minecraftschurlimods.bibliocraft.content.swordpedestal;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCFacingInteractibleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class SwordPedestalBlock extends BCFacingInteractibleBlock {
    public static final DyedItemColor DEFAULT_COLOR = new DyedItemColor(DyeColor.GREEN.getTextureDiffuseColor(), true);
    private static final VoxelShape Z_SHAPE = ShapeUtil.combine(
            Shapes.box(0, 0, 0.25, 1, 0.0625, 0.75),
            Shapes.box(0.0625, 0.0625, 0.25, 0.9375, 0.125, 0.75),
            Shapes.box(0.125, 0.125, 0.25, 0.875, 0.1875, 0.75),
            Shapes.box(0.1875, 0.1875, 0.25, 0.8125, 0.25, 0.75));
    private static final VoxelShape X_SHAPE = ShapeUtil.rotate(Z_SHAPE, Rotation.CLOCKWISE_90);

    public SwordPedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
    }

    @Override
    public int lookingAtSlot(BlockState state, BlockHitResult hit) {
        return 0;
    }

    @Override
    protected boolean canAccessFromDirection(BlockState state, Direction direction) {
        return direction != Direction.DOWN;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SwordPedestalBlockEntity(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        level.getBlockEntity(pos, BCBlockEntities.SWORD_PEDESTAL.get()).ifPresent(e -> e.saveToItem(stack, level.registryAccess()));
        return stack;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.isClientSide() || !(level.getBlockEntity(pos) instanceof SwordPedestalBlockEntity spbe))
            return super.getAnalogOutputSignal(state, level, pos);
        return spbe.getItem(0).isEmpty() ? 0 : 15;
    }
}
