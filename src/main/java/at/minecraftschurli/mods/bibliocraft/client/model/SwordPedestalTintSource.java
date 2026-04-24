package at.minecraftschurli.mods.bibliocraft.client.model;

import at.minecraftschurli.mods.bibliocraft.content.swordpedestal.SwordPedestalBlock;
import at.minecraftschurli.mods.bibliocraft.content.swordpedestal.SwordPedestalBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class SwordPedestalTintSource implements BlockTintSource {
    public static final SwordPedestalTintSource INSTANCE = new SwordPedestalTintSource();

    private SwordPedestalTintSource() {
    }

    @Override
    public int color(BlockState state) {
        return 0xff000000 | SwordPedestalBlock.DEFAULT_COLOR.rgb();
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof SwordPedestalBlockEntity blockEntity ? blockEntity.getColor().rgb() : -1;
    }
}
