package com.github.minecraftschurlimods.bibliocraft.content.cookiejar;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class CookieJarBlockEntity extends BCMenuBlockEntity {
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            CookieJarBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            CookieJarBlockEntity.this.updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof CookieJarMenu) {
                Container container = ((CookieJarMenu) player.containerMenu).getBlockEntity();
                return container == CookieJarBlockEntity.this;
            } else return false;
        }
    };

    public CookieJarBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.COOKIE_JAR.get(), 8, defaultName("cookie_jar"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new CookieJarMenu(id, inventory, this);
    }

    private void updateBlockState(BlockState pState, boolean pOpen) {
        Objects.requireNonNull(level).setBlock(getBlockPos(), pState.setValue(BarrelBlock.OPEN, pOpen), 3);
    }

    @Override
    public void startOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, Objects.requireNonNull(level), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player pPlayer) {
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, Objects.requireNonNull(level), this.getBlockPos(), this.getBlockState());
        }
    }
}
