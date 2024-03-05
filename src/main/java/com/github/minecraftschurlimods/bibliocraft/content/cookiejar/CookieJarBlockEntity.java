package com.github.minecraftschurlimods.bibliocraft.content.cookiejar;

import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.util.content.BCMenuBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CookieJarBlockEntity extends BCMenuBlockEntity {
    public CookieJarBlockEntity(BlockPos pos, BlockState state) {
        super(BCBlockEntities.COOKIE_JAR.get(), 8, defaultName("cookie_jar"), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return null;
    }
}
