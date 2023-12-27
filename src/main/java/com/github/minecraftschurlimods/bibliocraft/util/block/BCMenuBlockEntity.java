package com.github.minecraftschurlimods.bibliocraft.util.block;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public abstract class BCMenuBlockEntity extends BCBlockEntity implements MenuProvider, Nameable {
    private static final String NAME_KEY = "CustomName";
    private final Component defaultName;
    private Component name;

    /**
     * @param type          The {@link BlockEntityType} to use.
     * @param containerSize The size of the container.
     * @param defaultName         The title of the title, shown in GUIs.
     * @param pos           The position of this BE.
     * @param state         The state of this BE.
     */
    public BCMenuBlockEntity(BlockEntityType<?> type, int containerSize, Component defaultName, BlockPos pos, BlockState state) {
        super(type, containerSize, pos, state);
        this.defaultName = defaultName;
    }

    /**
     * @param name The name to use.
     * @return A title component of the format "container.bibliocraft.<name>".
     */
    public static Component defaultName(String name) {
        return Component.translatable("container." + Bibliocraft.MOD_ID + "." + name);
    }

    @Override
    protected void loadTag(CompoundTag tag) {
        super.loadTag(tag);
        if (tag.contains(NAME_KEY, Tag.TAG_STRING)) {
            this.name = Component.Serializer.fromJson(tag.getString(NAME_KEY));
        }
    }

    @Override
    protected void saveTag(CompoundTag tag) {
        super.saveTag(tag);
        if (name != null) {
            tag.putString(NAME_KEY, Component.Serializer.toJson(name));
        }
    }

    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getName() {
        return name != null ? name : defaultName;
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return name;
    }

    public boolean canOpen(Player player) {
        return true;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return this.canOpen(player) ? this.createMenu(id, inventory) : null;
    }

    protected abstract AbstractContainerMenu createMenu(int id, Inventory inventory);
}
