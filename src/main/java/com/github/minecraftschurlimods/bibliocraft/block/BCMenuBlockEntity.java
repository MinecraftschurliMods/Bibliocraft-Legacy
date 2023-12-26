package com.github.minecraftschurlimods.bibliocraft.block;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BCMenuBlockEntity extends BCBlockEntity implements MenuProvider {
    private final Component title;

    /**
     * @param type          The {@link BlockEntityType} to use.
     * @param containerSize The size of the container.
     * @param title         The title of the title, shown in GUIs.
     * @param pos           The position of this BE.
     * @param state         The state of this BE.
     */
    public BCMenuBlockEntity(BlockEntityType<?> type, int containerSize, Component title, BlockPos pos, BlockState state) {
        super(type, containerSize, pos, state);
        this.title = title;
    }

    /**
     * @param name The name to use.
     * @return A title component of the format "container.bibliocraft.<name>".
     */
    public static Component title(String name) {
        return Component.translatable("container." + Bibliocraft.MOD_ID + "." + name);
    }

    @Override
    public Component getDisplayName() {
        return title;
    }
}
