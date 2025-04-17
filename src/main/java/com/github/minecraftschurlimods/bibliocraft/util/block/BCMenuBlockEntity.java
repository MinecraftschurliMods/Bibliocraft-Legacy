package com.github.minecraftschurlimods.bibliocraft.util.block;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Abstract superclass for block entities with an associated menu.
 */
@SuppressWarnings("unused")
public abstract class BCMenuBlockEntity extends BCBlockEntity implements MenuProvider, Nameable {
    private static final String NAME_KEY = "CustomName";
    private final Component defaultName;
    private Component name;

    /**
     * @param type          The {@link BlockEntityType} to use.
     * @param containerSize The size of the container.
     * @param defaultName   The title of the title, shown in GUIs.
     * @param pos           The position of this BE.
     * @param state         The state of this BE.
     */
    public BCMenuBlockEntity(BlockEntityType<?> type, int containerSize, Component defaultName, BlockPos pos, BlockState state) {
        super(type, containerSize, pos, state);
        this.defaultName = defaultName;
    }

    /**
     * Creates a menu instance for this block entity.
     *
     * @param id        The menu id.
     * @param inventory The player inventory to use.
     * @return A menu instance for this block entity.
     */
    protected abstract AbstractContainerMenu createMenu(int id, Inventory inventory);

    @Override
    public Component getName() {
        return name != null ? name : defaultName;
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return name;
    }

    /**
     * Sets a custom name for this block entity.
     *
     * @param name The name to set.
     */
    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    /**
     * @param name The name to use.
     * @return A title component of the format {@code "container.bibliocraft.<name>"}.
     */
    public static Component defaultName(String name) {
        return Component.translatable("container." + BibliocraftApi.MOD_ID + "." + name);
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return BaseContainerBlockEntity.canUnlock(player, getLockKey(), getDisplayName()) ? this.createMenu(id, inventory) : null;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(NAME_KEY, Tag.TAG_STRING)) {
            this.name = Component.Serializer.fromJson(tag.getString(NAME_KEY), registries);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (name != null) {
            tag.putString(NAME_KEY, Component.Serializer.toJson(name, registries));
        }
    }
}
