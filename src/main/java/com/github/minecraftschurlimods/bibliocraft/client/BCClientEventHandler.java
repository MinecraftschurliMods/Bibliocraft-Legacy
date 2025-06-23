package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.client.ber.ClipboardBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ClockBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.CookieJarBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.DinnerPlateBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.DiscRackBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.DisplayCaseBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.FancyArmorStandBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.FancyCrafterBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.FancySignBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.LabelBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.PotionShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.SwordPedestalBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.TableBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ToolRackBER;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseModel;
import com.github.minecraftschurlimods.bibliocraft.client.model.TableModel;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BCMenuScreens;
import com.github.minecraftschurlimods.bibliocraft.client.screen.FancyCrafterScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.PrintingTableScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.SlottedBookScreen;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlock;
import com.github.minecraftschurlimods.bibliocraft.content.swordpedestal.SwordPedestalBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.jetbrains.annotations.ApiStatus;

public final class BCClientEventHandler {
    // @formatter:off
    @ApiStatus.Internal
    public static void init(IEventBus modBus) {
        modBus.addListener(RegisterMenuScreensEvent.class,                      BCClientEventHandler::registerMenuScreens);
        modBus.addListener(ModelEvent.RegisterAdditional.class,                 BCClientEventHandler::registerAdditional);
        modBus.addListener(ModelEvent.BakingCompleted.class,                    BCClientEventHandler::bakingCompleted);
        modBus.addListener(ModelEvent.RegisterGeometryLoaders.class,            BCClientEventHandler::registerGeometryLoaders);
        modBus.addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, BCClientEventHandler::registerLayerDefinitions);
        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class,        BCClientEventHandler::registerRenderers);
        modBus.addListener(RegisterColorHandlersEvent.Block.class,              BCClientEventHandler::registerColorHandlersBlock);
        modBus.addListener(RegisterColorHandlersEvent.Item.class,               BCClientEventHandler::registerColorHandlersItem);
    }

    private static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(BCMenus.BOOKCASE.get(),          BCMenuScreens.Bookcase::new);
        event.register(BCMenus.COOKIE_JAR.get(),        BCMenuScreens.CookieJar::new);
        event.register(BCMenus.DISC_RACK.get(),         BCMenuScreens.DiscRack::new);
        event.register(BCMenus.FANCY_ARMOR_STAND.get(), BCMenuScreens.FancyArmorStand::new);
        event.register(BCMenus.LABEL.get(),             BCMenuScreens.Label::new);
        event.register(BCMenus.POTION_SHELF.get(),      BCMenuScreens.PotionShelf::new);
        event.register(BCMenus.PRINTING_TABLE.get(),    PrintingTableScreen::new);
        event.register(BCMenus.SHELF.get(),             BCMenuScreens.Shelf::new);
        event.register(BCMenus.TOOL_RACK.get(),         BCMenuScreens.ToolRack::new);
        event.register(BCMenus.FANCY_CRAFTER.get(),     FancyCrafterScreen::new);
        event.register(BCMenus.SLOTTED_BOOK.get(),      SlottedBookScreen::new);
    }
    // @formatter:on

    private static void registerAdditional(ModelEvent.RegisterAdditional event) {
        for (TableBlock.Type type : TableBlock.Type.values()) {
            for (DyeColor color : DyeColor.values()) {
                event.register(ModelResourceLocation.standalone(BCUtil.bcLoc("block/color/" + color.getSerializedName() + "/table_cloth_" + type.getSerializedName())));
            }
        }
    }

    private static void bakingCompleted(ModelEvent.BakingCompleted event) {
        TableBER.rebuildClothModelCache();
    }

    private static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(BCUtil.bcLoc("bookcase"), BookcaseModel.LOADER);
        event.register(BCUtil.bcLoc("table"), TableModel.LOADER);
    }

    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ClockBER.LOCATION, ClockBER::createLayerDefinition);
    }

    // @formatter:off
    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BCEntities.FANCY_ARMOR_STAND.get(), ArmorStandRenderer::new);
        event.registerEntityRenderer(BCEntities.SEAT.get(),              EmptyEntityRenderer::new);
        event.registerBlockEntityRenderer(BCBlockEntities.CLOCK.get(),       ClockBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.CLIPBOARD.get(),         $ -> new ClipboardBER());
        event.registerBlockEntityRenderer(BCBlockEntities.COOKIE_JAR.get(),        $ -> new CookieJarBER());
        event.registerBlockEntityRenderer(BCBlockEntities.DINNER_PLATE.get(),      $ -> new DinnerPlateBER());
        event.registerBlockEntityRenderer(BCBlockEntities.DISPLAY_CASE.get(),      $ -> new DisplayCaseBER());
        event.registerBlockEntityRenderer(BCBlockEntities.DISC_RACK.get(),         $ -> new DiscRackBER());
        event.registerBlockEntityRenderer(BCBlockEntities.FANCY_ARMOR_STAND.get(), $ -> new FancyArmorStandBER());
        event.registerBlockEntityRenderer(BCBlockEntities.FANCY_CRAFTER.get(),     $ -> new FancyCrafterBER());
        event.registerBlockEntityRenderer(BCBlockEntities.FANCY_SIGN.get(),        $ -> new FancySignBER());
        event.registerBlockEntityRenderer(BCBlockEntities.LABEL.get(),             $ -> new LabelBER());
        event.registerBlockEntityRenderer(BCBlockEntities.POTION_SHELF.get(),      $ -> new PotionShelfBER());
        event.registerBlockEntityRenderer(BCBlockEntities.SHELF.get(),             $ -> new ShelfBER());
        event.registerBlockEntityRenderer(BCBlockEntities.SWORD_PEDESTAL.get(),    $ -> new SwordPedestalBER());
        event.registerBlockEntityRenderer(BCBlockEntities.TABLE.get(),             $ -> new TableBER());
        event.registerBlockEntityRenderer(BCBlockEntities.TOOL_RACK.get(),         $ -> new ToolRackBER());
    }
    // @formatter:on

    private static void registerColorHandlersBlock(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 && level != null && pos != null && level.getBlockEntity(pos) instanceof SwordPedestalBlockEntity spbe ? spbe.getColor().rgb() : -1, BCBlocks.SWORD_PEDESTAL.get());
    }

    private static void registerColorHandlersItem(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? stack.getOrDefault(DataComponents.DYED_COLOR, SwordPedestalBlock.DEFAULT_COLOR).rgb() : -1, BCItems.SWORD_PEDESTAL.get());
    }
}
