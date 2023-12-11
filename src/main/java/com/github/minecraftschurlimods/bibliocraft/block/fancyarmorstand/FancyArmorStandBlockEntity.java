package com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand;

import com.github.minecraftschurlimods.bibliocraft.block.BCBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfMenu;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FancyArmorStandBlockEntity extends BCBlockEntity implements MenuProvider {
    public FancyArmorStandBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.FANCY_ARMOR_STAND.get(), 4, title("fancy_armor_stand"), pos, state);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FancyArmorStandMenu(id, inventory, this);
    }
}
