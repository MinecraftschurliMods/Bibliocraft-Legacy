package at.minecraftschurli.mods.bibliocraft.init;

import at.minecraftschurli.mods.bibliocraft.content.bookcase.BookcaseBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.clipboard.ClipboardBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.clock.ClockBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.cookiejar.CookieJarBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.dinnerplate.DinnerPlateBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.discrack.DiscRackBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.displaycase.DisplayCaseBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.fancyarmorstand.FancyArmorStandBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.fancycrafter.FancyCrafterBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.fancysign.FancySignBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.label.LabelBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.potionshelf.PotionShelfBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.printingtable.PrintingTableBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.shelf.ShelfBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.swordpedestal.SwordPedestalBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.table.TableBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.toolrack.ToolRackBlockEntity;
import at.minecraftschurli.mods.bibliocraft.content.typewriter.TypewriterBlockEntity;
import at.minecraftschurli.mods.bibliocraft.util.BCUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface BCBlockEntities {
    // @formatter:off
    Supplier<BlockEntityType<BookcaseBlockEntity>>        BOOKCASE          = register("bookcase",          BookcaseBlockEntity::new,        BCBlocks.BOOKCASE.holders());
    Supplier<BlockEntityType<ClipboardBlockEntity>>       CLIPBOARD         = register("clipboard",         ClipboardBlockEntity::new,       BCBlocks.CLIPBOARD);
    Supplier<BlockEntityType<ClockBlockEntity>>           CLOCK             = register("clock",             ClockBlockEntity::new,           BCUtil.mergeRaw(BCBlocks.FANCY_CLOCK.holders(), BCBlocks.WALL_FANCY_CLOCK.holders(), BCBlocks.GRANDFATHER_CLOCK.holders()));
    Supplier<BlockEntityType<CookieJarBlockEntity>>       COOKIE_JAR        = register("cookie_jar",        CookieJarBlockEntity::new,       BCBlocks.COOKIE_JAR);
    Supplier<BlockEntityType<DinnerPlateBlockEntity>>     DINNER_PLATE      = register("dinner_plate",      DinnerPlateBlockEntity::new,     BCBlocks.DINNER_PLATE);
    Supplier<BlockEntityType<DiscRackBlockEntity>>        DISC_RACK         = register("disc_rack",         DiscRackBlockEntity::new,        BCBlocks.DISC_RACK, BCBlocks.WALL_DISC_RACK);
    Supplier<BlockEntityType<DisplayCaseBlockEntity>>     DISPLAY_CASE      = register("display_case",      DisplayCaseBlockEntity::new,     BCUtil.mergeRaw(BCBlocks.DISPLAY_CASE.holders(), BCBlocks.WALL_DISPLAY_CASE.holders()));
    Supplier<BlockEntityType<FancyArmorStandBlockEntity>> FANCY_ARMOR_STAND = register("fancy_armor_stand", FancyArmorStandBlockEntity::new, BCUtil.merge(BCBlocks.FANCY_ARMOR_STAND.holders(), BCBlocks.IRON_FANCY_ARMOR_STAND));
    Supplier<BlockEntityType<FancyCrafterBlockEntity>>    FANCY_CRAFTER     = register("fancy_crafter",     FancyCrafterBlockEntity::new,    BCBlocks.FANCY_CRAFTER.holders());
    Supplier<BlockEntityType<FancySignBlockEntity>>       FANCY_SIGN        = register("fancy_sign",        FancySignBlockEntity::new,       BCUtil.mergeRaw(BCBlocks.FANCY_SIGN.holders(), BCBlocks.WALL_FANCY_SIGN.holders()));
    Supplier<BlockEntityType<LabelBlockEntity>>           LABEL             = register("label",             LabelBlockEntity::new,           BCBlocks.LABEL.holders());
    Supplier<BlockEntityType<PotionShelfBlockEntity>>     POTION_SHELF      = register("potion_shelf",      PotionShelfBlockEntity::new,     BCBlocks.POTION_SHELF.holders());
    Supplier<BlockEntityType<PrintingTableBlockEntity>>   PRINTING_TABLE    = register("printing_table",    PrintingTableBlockEntity::new,   BCBlocks.PRINTING_TABLE, BCBlocks.IRON_PRINTING_TABLE);
    Supplier<BlockEntityType<ShelfBlockEntity>>           SHELF             = register("shelf",             ShelfBlockEntity::new,           BCBlocks.SHELF.holders());
    Supplier<BlockEntityType<SwordPedestalBlockEntity>>   SWORD_PEDESTAL    = register("sword_pedestal",    SwordPedestalBlockEntity::new,   BCBlocks.SWORD_PEDESTAL);
    Supplier<BlockEntityType<TableBlockEntity>>           TABLE             = register("table",             TableBlockEntity::new,           BCBlocks.TABLE.holders());
    Supplier<BlockEntityType<ToolRackBlockEntity>>        TOOL_RACK         = register("tool_rack",         ToolRackBlockEntity::new,        BCBlocks.TOOL_RACK.holders());
    Supplier<BlockEntityType<TypewriterBlockEntity>>      TYPEWRITER        = register("typewriter",        TypewriterBlockEntity::new,      BCUtil.merge(BCBlocks.TYPEWRITER.holders(), BCBlocks.CLEAR_TYPEWRITER));
    // @formatter:on

    /// Registration helper method that takes a supplier list instead of a vararg parameter.
    ///
    /// @param name     The registry name to use.
    /// @param supplier The block entity supplier to use.
    /// @param blocks   A list of block suppliers that are associated with the block entity.
    /// @param <T>      The exact type of the block entity.
    /// @return A block entity type supplier.
    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<? extends Supplier<? extends Block>> blocks) {
        return BCRegistries.BLOCK_ENTITIES.register(name, () -> new BlockEntityType<>(supplier, blocks.stream().map(Supplier::get).collect(Collectors.toSet())));
    }

    /// Registration helper method that takes a supplier vararg parameter instead of a regular vararg parameter.
    ///
    /// @param name     The registry name to use.
    /// @param supplier The block entity supplier to use.
    /// @param blocks   A vararg of block suppliers that are associated with the block entity.
    /// @param <T>      The exact type of the block entity.
    /// @return A block entity type supplier.
    @SafeVarargs
    static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block>... blocks) {
        return register(name, supplier, List.of(blocks));
    }

    /// Empty method, called by [BCRegistries#init()] to classload this class.
    @ApiStatus.Internal
    static void init() {}
}
