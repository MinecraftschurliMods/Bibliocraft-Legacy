package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.api.woodtype.BibliocraftWoodType;
import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clock.FancyClockBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clock.GrandfatherClockBlock;
import com.github.minecraftschurlimods.bibliocraft.content.clock.WallFancyClockBlock;
import com.github.minecraftschurlimods.bibliocraft.content.cookiejar.CookieJarBlock;
import com.github.minecraftschurlimods.bibliocraft.content.deskbell.DeskBellBlock;
import com.github.minecraftschurlimods.bibliocraft.content.dinnerplate.DinnerPlateBlock;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.DiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.discrack.WallDiscRackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.DisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.displaycase.WallDisplayCaseBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancyarmorstand.FancyArmorStandBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.AbstractFancyLightBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLampBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancylight.FancyLanternBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.FancySignBlock;
import com.github.minecraftschurlimods.bibliocraft.content.fancysign.WallFancySignBlock;
import com.github.minecraftschurlimods.bibliocraft.content.label.LabelBlock;
import com.github.minecraftschurlimods.bibliocraft.content.potionshelf.PotionShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.seat.SeatBlock;
import com.github.minecraftschurlimods.bibliocraft.content.shelf.ShelfBlock;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.toolrack.ToolRackBlock;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlock;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.holder.GroupedHolder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface BCBlocks {
    List<GroupedHolder<BibliocraftWoodType, Block, ? extends Block>> WOODEN = new ArrayList<>();
    List<GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, ? extends Block>> WOODEN_COLORED = new ArrayList<>();
    List<GroupedHolder<DyeColor, Block, ? extends Block>> COLORED = new ArrayList<>();
    List<DeferredBlock<?>> OTHER = new ArrayList<>();

    // @formatter:off
    GroupedHolder<BibliocraftWoodType, Block, BookcaseBlock>         BOOKCASE          = woodenBlock("bookcase",          BookcaseBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, FancyArmorStandBlock>  FANCY_ARMOR_STAND = woodenBlock("fancy_armor_stand", FancyArmorStandBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, FancyClockBlock>       FANCY_CLOCK       = woodenBlock("fancy_clock",       FancyClockBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, WallFancyClockBlock>   WALL_FANCY_CLOCK  = woodenBlock("wall_fancy_clock",  WallFancyClockBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, FancyCrafterBlock>     FANCY_CRAFTER     = woodenBlock("fancy_crafter",     properties -> new FancyCrafterBlock(properties.requiredFeatures(BCFeatureFlags.WIP)));
    GroupedHolder<BibliocraftWoodType, Block, FancySignBlock>        FANCY_SIGN        = woodenBlock("fancy_sign",        FancySignBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, WallFancySignBlock>    WALL_FANCY_SIGN   = woodenBlock("wall_fancy_sign",   WallFancySignBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, GrandfatherClockBlock> GRANDFATHER_CLOCK = woodenBlock("grandfather_clock", GrandfatherClockBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, LabelBlock>            LABEL             = woodenBlock("label",             LabelBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, PotionShelfBlock>      POTION_SHELF      = woodenBlock("potion_shelf",      PotionShelfBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, ShelfBlock>            SHELF             = woodenBlock("shelf",             ShelfBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, TableBlock>            TABLE             = woodenBlock("table",             TableBlock::new);
    GroupedHolder<BibliocraftWoodType, Block, ToolRackBlock>         TOOL_RACK         = woodenBlock("tool_rack",         ToolRackBlock::new);
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, DisplayCaseBlock>     DISPLAY_CASE      = coloredWoodenBlock("display_case",      DisplayCaseBlock::new);
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, WallDisplayCaseBlock> WALL_DISPLAY_CASE = coloredWoodenBlock("wall_display_case", WallDisplayCaseBlock::new);
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, SeatBlock>            SEAT              = coloredWoodenBlock("seat",              SeatBlock::new);
    GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, SeatBackBlock>        SEAT_BACK         = coloredWoodenBlock("seat_back",         SeatBackBlock::new);
    DeferredBlock<FancyLampBlock>            CLEAR_FANCY_GOLD_LAMP = basicBlock("fancy_gold_lamp",   FancyLampBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    GroupedHolder<DyeColor, Block, FancyLampBlock> FANCY_GOLD_LAMP = coloredBlock("fancy_gold_lamp", FancyLampBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    DeferredBlock<FancyLampBlock>            CLEAR_FANCY_IRON_LAMP = basicBlock("fancy_iron_lamp",   FancyLampBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    GroupedHolder<DyeColor, Block, FancyLampBlock> FANCY_IRON_LAMP = coloredBlock("fancy_iron_lamp", FancyLampBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    DeferredBlock<FancyLanternBlock>            CLEAR_FANCY_GOLD_LANTERN = basicBlock("fancy_gold_lantern",   FancyLanternBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    GroupedHolder<DyeColor, Block, FancyLanternBlock> FANCY_GOLD_LANTERN = coloredBlock("fancy_gold_lantern", FancyLanternBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    DeferredBlock<FancyLanternBlock>            CLEAR_FANCY_IRON_LANTERN = basicBlock("fancy_iron_lantern",   FancyLanternBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    GroupedHolder<DyeColor, Block, FancyLanternBlock> FANCY_IRON_LANTERN = coloredBlock("fancy_iron_lantern", FancyLanternBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 15 : 0).noOcclusion());
    DeferredBlock<FancyLanternBlock>             SOUL_FANCY_GOLD_LANTERN = basicBlock("soul_fancy_gold_lantern", properties -> new FancyLanternBlock(properties, BCUtil.modLoc("buzzier_bees", "small_soul_fire_flame")), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 10 : 0).noOcclusion());
    DeferredBlock<FancyLanternBlock>             SOUL_FANCY_IRON_LANTERN = basicBlock("soul_fancy_iron_lantern", properties -> new FancyLanternBlock(properties, BCUtil.modLoc("buzzier_bees", "small_soul_fire_flame")), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(AbstractFancyLightBlock.LIT) ? 10 : 0).noOcclusion());
    DeferredBlock<TypewriterBlock>            CLEAR_TYPEWRITER = basicBlock("typewriter",   TypewriterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).noOcclusion());
    GroupedHolder<DyeColor, Block, TypewriterBlock> TYPEWRITER = coloredBlock("typewriter", TypewriterBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).noOcclusion());
    DeferredBlock<CookieJarBlock>       COOKIE_JAR             = basicBlock("cookie_jar",             CookieJarBlock::new,       () -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
    DeferredBlock<ClipboardBlock>       CLIPBOARD              = basicBlock("clipboard",              ClipboardBlock::new,       p -> p.instabreak().sound(SoundType.WOOD).ignitedByLava());
    DeferredBlock<DeskBellBlock>        DESK_BELL              = basicBlock("desk_bell",              DeskBellBlock::new,        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    DeferredBlock<DinnerPlateBlock>     DINNER_PLATE           = basicBlock("dinner_plate",           DinnerPlateBlock::new,     () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ).noOcclusion());
    DeferredBlock<DiscRackBlock>        DISC_RACK              = basicBlock("disc_rack",              DiscRackBlock::new,        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX).noOcclusion());
    DeferredBlock<WallDiscRackBlock>    WALL_DISC_RACK         = basicBlock("wall_disc_rack",         WallDiscRackBlock::new,    () -> BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX).noOcclusion());
    DeferredBlock<FancyArmorStandBlock> IRON_FANCY_ARMOR_STAND = basicBlock("iron_fancy_armor_stand", FancyArmorStandBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    DeferredBlock<ChainBlock>           GOLD_CHAIN             = basicBlock("gold_chain",             ChainBlock::new,           () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_CHAIN));
    DeferredBlock<LanternBlock>         GOLD_LANTERN           = basicBlock("gold_lantern",           LanternBlock::new,         () -> BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN));
    DeferredBlock<LanternBlock>         GOLD_SOUL_LANTERN      = basicBlock("gold_soul_lantern",      LanternBlock::new,         () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_LANTERN));
    DeferredBlock<PrintingTableBlock>   PRINTING_TABLE         = basicBlock("printing_table",         PrintingTableBlock::new,   () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion().requiredFeatures(BCFeatureFlags.WIP));
    DeferredBlock<PrintingTableBlock>   IRON_PRINTING_TABLE    = basicBlock("iron_printing_table",    PrintingTableBlock::new,   () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion().requiredFeatures(BCFeatureFlags.WIP));
    DeferredBlock<SwordPedestalBlock>   SWORD_PEDESTAL         = basicBlock("sword_pedestal",         SwordPedestalBlock::new,   () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE).noOcclusion());
    //TODO Map Frame
    //TODO Painting Frame
    //TODO Painting Press
    //TODO Writing Desk
    // @formatter:on

    static <T extends Block> GroupedHolder<BibliocraftWoodType, Block, T> woodenBlock(String name, Function<BlockBehaviour.Properties, T> creator) {
        GroupedHolder<BibliocraftWoodType, Block, T> holder = GroupedHolder.forBlock(BCRegistries.BLOCKS, wood -> wood.getRegistrationPrefix() + "_", name, ($, p) -> creator.apply(p), wood -> p -> wood.properties().get().noOcclusion(), BibliocraftApi.getWoodTypeRegistry().getAll());
        WOODEN.add(holder);
        return holder;
    }

    static <T extends Block> GroupedHolder<DyeColor, Block, T> coloredBlock(String suffix, Function<BlockBehaviour.Properties, T> creator, Supplier<BlockBehaviour.Properties> properties) {
        GroupedHolder<DyeColor, Block, T> holder = GroupedHolder.forBlock(BCRegistries.BLOCKS, color -> color.getName() + "_", suffix, ($, p) -> creator.apply(p), $ -> p -> properties.get(), List.of(DyeColor.values()));
        COLORED.add(holder);
        return holder;
    }

    static <T extends Block> GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, T> coloredWoodenBlock(String name, Function<BlockBehaviour.Properties, T> creator) {
        GroupedHolder.Nested<BibliocraftWoodType, DyeColor, Block, T> holder = GroupedHolder.forBlockNested(BCRegistries.BLOCKS, (wood, color) -> color.getName() + "_" + wood.getRegistrationPrefix() + "_", name, ($1, $2, properties) -> creator.apply(properties), (wood, $) -> p -> wood.properties().get().noOcclusion(), BibliocraftApi.getWoodTypeRegistry().getAll(), List.of(DyeColor.values()));
        WOODEN_COLORED.add(holder);
        return holder;
    }

    static <T extends Block> DeferredBlock<T> basicBlock(String name, Function<BlockBehaviour.Properties, T> factory, Supplier<BlockBehaviour.Properties> props) {
        DeferredBlock<T> holder = BCRegistries.BLOCKS.registerBlock(name, factory, props);
        OTHER.add(holder);
        return holder;
    }

    static <T extends Block> DeferredBlock<T> basicBlock(String name, Function<BlockBehaviour.Properties, T> factory, UnaryOperator<BlockBehaviour.Properties> props) {
        DeferredBlock<T> holder = BCRegistries.BLOCKS.registerBlock(name, factory, props);
        OTHER.add(holder);
        return holder;
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
