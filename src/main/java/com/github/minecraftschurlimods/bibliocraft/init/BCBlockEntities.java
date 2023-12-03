package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public interface BCBlockEntities {
    Supplier<BlockEntityType<BookcaseBlockEntity>> BOOKCASE = register("bookcase", BookcaseBlockEntity::new, BCBlocks.BOOKCASE.holders());

    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<? extends Supplier<? extends Block>> blocks) {
        return BCRegistries.BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
    }

    @SafeVarargs
    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
        return register(name, supplier, List.of(blocks));
    }

    static void init() {}
}