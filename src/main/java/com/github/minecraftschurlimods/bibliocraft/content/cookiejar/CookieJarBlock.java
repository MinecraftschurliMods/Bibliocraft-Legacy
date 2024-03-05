package com.github.minecraftschurlimods.bibliocraft.content.cookiejar;

import com.github.minecraftschurlimods.bibliocraft.util.ShapeUtil;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCWaterloggedBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CookieJarBlock extends BCWaterloggedBlock {
    private static final VoxelShape OPEN_SHAPE = ShapeUtil.combine(
            Shapes.box(0.125, 0, 0.125, 0.875, 0.625, 0.875),
            Shapes.box(0.25, 0.625, 0.25, 0.75, 0.75, 0.75));
    private static final VoxelShape CLOSED_SHAPE = ShapeUtil.combine(OPEN_SHAPE,
            Shapes.box(0.1875, 0.75, 0.1875, 0.8125, 0.875, 0.8125));

    public CookieJarBlock(Properties properties) {
        super(properties);
    }
}
