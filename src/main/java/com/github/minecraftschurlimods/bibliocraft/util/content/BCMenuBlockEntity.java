package com.github.minecraftschurlimods.bibliocraft.util.content;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    public boolean canOpen(Player player) {
        return true;
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return this.canOpen(player) ? this.createMenu(id, inventory) : null;
    }

    @Override
    protected void loadTag(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadTag(tag, lookup);
        if (tag.contains(NAME_KEY, Tag.TAG_STRING)) {
            this.name = Component.Serializer.fromJson(tag.getString(NAME_KEY), lookup);
        }
    }

    @Override
    protected void saveTag(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveTag(tag, lookup);
        if (this.name != null) {
            tag.putString(NAME_KEY, Component.Serializer.toJson(this.name, lookup));
        }
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
        super.applyImplicitComponents(input);
        this.name = input.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(DataComponents.CUSTOM_NAME, this.name);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(NAME_KEY);
    }
}
