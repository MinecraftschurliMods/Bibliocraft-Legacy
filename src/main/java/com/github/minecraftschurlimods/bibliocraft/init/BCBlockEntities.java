package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clock.FancyClockBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.dinnerplate.DinnerPlateBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.label.LabelBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.toolrack.ToolRackBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public interface BCBlockEntities {
    Supplier<BlockEntityType<BookcaseBlockEntity>>        BOOKCASE          = register("bookcase",          BookcaseBlockEntity::new,        BCBlocks.BOOKCASE.holders());
    Supplier<BlockEntityType<ClipboardBlockEntity>>       CLIPBOARD         = register("clipboard",         ClipboardBlockEntity::new,       BCBlocks.CLIPBOARD);
    Supplier<BlockEntityType<CookieJarBlockEntity>>       COOKIE_JAR        = register("cookie_jar",        CookieJarBlockEntity::new,       BCBlocks.COOKIE_JAR);
    Supplier<BlockEntityType<DinnerPlateBlockEntity>>     DINNER_PLATE      = register("dinner_plate",      DinnerPlateBlockEntity::new,     BCBlocks.DINNER_PLATE);
    Supplier<BlockEntityType<DiscRackBlockEntity>>        DISC_RACK         = register("disc_rack",         DiscRackBlockEntity::new,        BCBlocks.DISC_RACK, BCBlocks.WALL_DISC_RACK);
    Supplier<BlockEntityType<DisplayCaseBlockEntity>>     DISPLAY_CASE      = register("display_case",      DisplayCaseBlockEntity::new,     BCUtil.mergeRaw(BCBlocks.DISPLAY_CASE.holders(), BCBlocks.WALL_DISPLAY_CASE.holders()));
    Supplier<BlockEntityType<FancyArmorStandBlockEntity>> FANCY_ARMOR_STAND = register("fancy_armor_stand", FancyArmorStandBlockEntity::new, BCUtil.merge(BCBlocks.FANCY_ARMOR_STAND.holders(), BCBlocks.IRON_FANCY_ARMOR_STAND));
    Supplier<BlockEntityType<FancyClockBlockEntity>>      FANCY_CLOCK       = register("fancy_clock",       FancyClockBlockEntity::new,      BCBlocks.FANCY_CLOCK.holders());
    Supplier<BlockEntityType<FancyCrafterBlockEntity>>    FANCY_CRAFTER     = register("fancy_crafter",     FancyCrafterBlockEntity::new,    BCBlocks.FANCY_CRAFTER.holders());
    Supplier<BlockEntityType<LabelBlockEntity>>           LABEL             = register("label",             LabelBlockEntity::new,           BCBlocks.LABEL.holders());
    Supplier<BlockEntityType<PotionShelfBlockEntity>>     POTION_SHELF      = register("potion_shelf",      PotionShelfBlockEntity::new,     BCBlocks.POTION_SHELF.holders());
    Supplier<BlockEntityType<ShelfBlockEntity>>           SHELF             = register("shelf",             ShelfBlockEntity::new,           BCBlocks.SHELF.holders());
    Supplier<BlockEntityType<SwordPedestalBlockEntity>>   SWORD_PEDESTAL    = register("sword_pedestal",    SwordPedestalBlockEntity::new,   BCBlocks.SWORD_PEDESTAL);
    Supplier<BlockEntityType<TableBlockEntity>>           TABLE             = register("table",             TableBlockEntity::new,           BCBlocks.TABLE.holders());
    Supplier<BlockEntityType<ToolRackBlockEntity>>        TOOL_RACK         = register("tool_rack",         ToolRackBlockEntity::new,        BCBlocks.TOOL_RACK.holders());

    /**
     * Registration helper method that takes a supplier list instead of a vararg parameter.
     *
     * @param name     The registry name to use.
     * @param supplier The block entity supplier to use.
     * @param blocks   A list of block suppliers that are associated with the block entity.
     * @param <T>      The exact type of the block entity.
     * @return A block entity type supplier.
     */
    @SuppressWarnings("DataFlowIssue")
    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<? extends Supplier<? extends Block>> blocks) {
        return BCRegistries.BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.stream().map(Supplier::get).toList().toArray(new Block[0])).build(null));
    }

    /**
     * Registration helper method that takes a supplier vararg parameter instead of a regular vararg parameter.
     *
     * @param name     The registry name to use.
     * @param supplier The block entity supplier to use.
     * @param blocks   A vararg of block suppliers that are associated with the block entity.
     * @param <T>      The exact type of the block entity.
     * @return A block entity type supplier.
     */
    @SafeVarargs
    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
        return register(name, supplier, List.of(blocks));
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
