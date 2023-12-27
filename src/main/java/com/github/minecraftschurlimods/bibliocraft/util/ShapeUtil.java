package com.github.minecraftschurlimods.bibliocraft.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Reduced and simplified version of <a href="https://github.com/mekanism/Mekanism/blob/1.20.x/src/main/java/mekanism/common/util/VoxelShapeUtils.java">Mekanism's VoxelShapeUtils</a>.
 */
@SuppressWarnings("unused")
public final class ShapeUtil {
    private static final Vec3 FROM_ORIGIN = new Vec3(-0.5, -0.5, -0.5);

    /**
     * Rotates an {@link AABB} to a specific side, similar to how block states rotate models.
     *
     * @param box  The {@link AABB} to rotate.
     * @param side The side to rotate it to.
     * @return The rotated {@link AABB}.
     */
    public static AABB rotate(AABB box, Direction side) {
        return switch (side) {
            case DOWN -> box;
            case UP -> new AABB(box.minX, -box.minY, -box.minZ, box.maxX, -box.maxY, -box.maxZ);
            case NORTH -> new AABB(box.minX, -box.minZ, box.minY, box.maxX, -box.maxZ, box.maxY);
            case SOUTH -> new AABB(-box.minX, -box.minZ, -box.minY, -box.maxX, -box.maxZ, -box.maxY);
            case WEST -> new AABB(box.minY, -box.minZ, -box.minX, box.maxY, -box.maxZ, -box.maxX);
            case EAST -> new AABB(-box.minY, -box.minZ, box.minX, -box.maxY, -box.maxZ, box.maxX);
        };
    }

    /**
     * Rotates an {@link AABB} according to a specific rotation.
     *
     * @param box      The {@link AABB} to rotate.
     * @param rotation The rotation we are performing.
     * @return The rotated {@link AABB}.
     */
    public static AABB rotate(AABB box, Rotation rotation) {
        return switch (rotation) {
            case NONE -> box;
            case CLOCKWISE_90 -> new AABB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
            case CLOCKWISE_180 -> new AABB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
            case COUNTERCLOCKWISE_90 -> new AABB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
        };
    }

    /**
     * Rotates an {@link AABB} to a specific side horizontally.
     *
     * @param box  The {@link AABB} to rotate.
     * @param side The side to rotate it to.
     * @return The rotated {@link AABB}.
     */
    public static AABB rotateHorizontal(AABB box, Direction side) {
        return switch (side) {
            case NORTH -> rotate(box, Rotation.NONE);
            case SOUTH -> rotate(box, Rotation.CLOCKWISE_180);
            case WEST -> rotate(box, Rotation.COUNTERCLOCKWISE_90);
            case EAST -> rotate(box, Rotation.CLOCKWISE_90);
            default -> box;
        };
    }

    /**
     * Rotates a {@link VoxelShape} to a specific side, similar to how block states rotate models.
     *
     * @param shape The {@link VoxelShape} to rotate.
     * @param side  The side to rotate it to.
     * @return The rotated {@link VoxelShape}.
     */
    public static VoxelShape rotate(VoxelShape shape, Direction side) {
        return rotate(shape, box -> rotate(box, side));
    }

    /**
     * Rotates a {@link VoxelShape} according to a specific rotation.
     *
     * @param shape    The {@link VoxelShape} to rotate.
     * @param rotation The rotation we are performing.
     * @return The rotated {@link VoxelShape}.
     */
    public static VoxelShape rotate(VoxelShape shape, Rotation rotation) {
        return rotate(shape, box -> rotate(box, rotation));
    }

    /**
     * Rotates a {@link VoxelShape} to a specific side horizontally.
     *
     * @param shape The {@link VoxelShape} to rotate.
     * @param side  The side to rotate it to.
     * @return The rotated {@link VoxelShape}.
     */
    public static VoxelShape rotateHorizontal(VoxelShape shape, Direction side) {
        return rotate(shape, box -> rotateHorizontal(box, side));
    }

    /**
     * Rotates a {@link VoxelShape} using a specific transformation function for each {@link AABB} in the {@link VoxelShape}.
     *
     * @param shape          The {@link VoxelShape} to rotate.
     * @param rotateFunction The transformation function to apply to each {@link AABB} in the {@link VoxelShape}.
     * @return The rotated {@link VoxelShape}.
     */
    public static VoxelShape rotate(VoxelShape shape, UnaryOperator<AABB> rotateFunction) {
        List<VoxelShape> rotatedPieces = new ArrayList<>();
        for (AABB sourceBoundingBox : shape.toAabbs()) {
            rotatedPieces.add(Shapes.create(rotateFunction.apply(sourceBoundingBox.move(FROM_ORIGIN.x, FROM_ORIGIN.y, FROM_ORIGIN.z)).move(-FROM_ORIGIN.x, -FROM_ORIGIN.z, -FROM_ORIGIN.z)));
        }
        return combine(rotatedPieces);
    }

    /**
     * Used for mass combining shapes.
     *
     * @param shapes The list of {@link VoxelShape}s to include.
     * @return A simplified {@link VoxelShape} including everything that is part of the input shapes.
     */
    public static VoxelShape combine(VoxelShape... shapes) {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    /**
     * Used for mass combining shapes.
     *
     * @param shapes The collection of {@link VoxelShape}s to include.
     * @return A simplified {@link VoxelShape} including everything that is part of the input shapes.
     */
    public static VoxelShape combine(Collection<VoxelShape> shapes) {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    /**
     * Used for cutting shapes out of a full cube.
     *
     * @param shapes The list of {@link VoxelShape}s to cut out.
     * @return A {@link VoxelShape} including everything that is not part of the input shapes.
     */
    public static VoxelShape exclude(VoxelShape... shapes) {
        return batchCombine(Shapes.block(), BooleanOp.ONLY_FIRST, true, shapes);
    }

    /**
     * Used for mass combining shapes using a specific {@link BooleanOp} and a given start shape.
     *
     * @param initial  The {@link VoxelShape} to start with.
     * @param function The {@link BooleanOp} to perform.
     * @param simplify true if the returned shape should run {@link VoxelShape#optimize()}, false otherwise.
     * @param shapes   The collection of {@link VoxelShape}s to include.
     * @return A {@link VoxelShape} based on the input parameters.
     */
    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, Collection<VoxelShape> shapes) {
        VoxelShape combinedShape = initial;
        for (VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
        }
        return simplify ? combinedShape.optimize() : combinedShape;
    }

    /**
     * Used for mass combining shapes using a specific {@link BooleanOp} and a given start shape.
     *
     * @param initial  The {@link VoxelShape} to start with.
     * @param function The {@link BooleanOp} to perform.
     * @param simplify true if the returned shape should run {@link VoxelShape#optimize()}, false otherwise.
     * @param shapes   The list of {@link VoxelShape}s to include.
     * @return A {@link VoxelShape} based on the input parameters.
     */
    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, VoxelShape... shapes) {
        VoxelShape combinedShape = initial;
        for (VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
        }
        return simplify ? combinedShape.optimize() : combinedShape;
    }
}
