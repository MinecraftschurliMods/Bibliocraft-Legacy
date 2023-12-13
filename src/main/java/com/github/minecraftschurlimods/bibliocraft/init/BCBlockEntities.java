package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.block.fancyarmorstand.FancyArmorStandBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.block.potionshelf.PotionShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.block.shelf.ShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.block.toolrack.ToolRackBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public interface BCBlockEntities {
    Supplier<BlockEntityType<BookcaseBlockEntity>>        BOOKCASE          = register("bookcase",          BookcaseBlockEntity::new,        BCBlocks.BOOKCASE.holders());
    Supplier<BlockEntityType<FancyArmorStandBlockEntity>> FANCY_ARMOR_STAND = register("fancy_armor_stand", FancyArmorStandBlockEntity::new, BCUtil.merge(BCBlocks.FANCY_ARMOR_STAND.holders(), BCBlocks.IRON_FANCY_ARMOR_STAND));
    Supplier<BlockEntityType<ShelfBlockEntity>>           SHELF             = register("shelf",             ShelfBlockEntity::new,           BCBlocks.SHELF.holders());
    Supplier<BlockEntityType<PotionShelfBlockEntity>>     POTION_SHELF      = register("potion_shelf",      PotionShelfBlockEntity::new,     BCBlocks.POTION_SHELF.holders());
    Supplier<BlockEntityType<ToolRackBlockEntity>>        TOOL_RACK         = register("tool_rack",         ToolRackBlockEntity::new,        BCBlocks.TOOL_RACK.holders());

    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<? extends Supplier<? extends Block>> blocks) {
        return BCRegistries.BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
    }

    @SafeVarargs
    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
        return register(name, supplier, List.of(blocks));
    }

    static void init() {}
}
