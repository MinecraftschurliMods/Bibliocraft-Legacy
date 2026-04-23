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
import com.github.minecraftschurlimods.bibliocraft.client.jei.BCJeiPlugin;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseBlockStateModel;
import com.github.minecraftschurlimods.bibliocraft.client.model.SwordPedestalTintSource;
import com.github.minecraftschurlimods.bibliocraft.client.model.TableBlockStateModel;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BCMenuScreens;
import com.github.minecraftschurlimods.bibliocraft.client.screen.FancyCrafterScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.PrintingTableScreen;
import com.github.minecraftschurlimods.bibliocraft.client.screen.SlottedBookScreen;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlockEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCEntities;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenus;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.List;

public final class BCClientEventHandler {
    @SubscribeEvent
    private static void registerMenuScreens(RegisterMenuScreensEvent event) {
        // @formatter:off
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
        // @formatter:on
    }

    @SubscribeEvent
    private static void registerGeometryLoaders(RegisterBlockStateModels event) {
        event.registerModel(BCUtil.bcLoc("bookcase"), BookcaseBlockStateModel.Unbaked.CODEC);
        event.registerModel(BCUtil.bcLoc("table"), TableBlockStateModel.Unbaked.CODEC);
    }

    @SubscribeEvent
    private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ClockBER.LOCATION, ClockBER::createLayerDefinition);
    }

    @SubscribeEvent
    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // @formatter:off
        event.registerEntityRenderer(BCEntities.FANCY_ARMOR_STAND.get(),           ArmorStandRenderer::new);
        event.registerEntityRenderer(BCEntities.SEAT.get(),                        NoopRenderer::new);
        event.registerBlockEntityRenderer(BCBlockEntities.CLOCK.get(),             ClockBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.CLIPBOARD.get(),         ClipboardBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.COOKIE_JAR.get(),        CookieJarBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.DINNER_PLATE.get(),      DinnerPlateBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.DISPLAY_CASE.get(),      DisplayCaseBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.DISC_RACK.get(),         DiscRackBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.FANCY_ARMOR_STAND.get(), FancyArmorStandBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.FANCY_CRAFTER.get(),     FancyCrafterBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.FANCY_SIGN.get(),        FancySignBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.LABEL.get(),             LabelBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.POTION_SHELF.get(),      PotionShelfBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.SHELF.get(),             ShelfBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.SWORD_PEDESTAL.get(),    SwordPedestalBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.TABLE.get(),             TableBER::new);
        event.registerBlockEntityRenderer(BCBlockEntities.TOOL_RACK.get(),         ToolRackBER::new);
        // @formatter:on
    }

    @SubscribeEvent
    private static void registerColorHandlersBlock(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(_ -> -1, SwordPedestalTintSource.INSTANCE), BCBlocks.SWORD_PEDESTAL.get());
    }

    @SubscribeEvent
    private static void recipesReceived(RecipesReceivedEvent event) {
        if (ModList.get().isLoaded("jei")) {
            BCJeiPlugin.setRecipeMap(event.getRecipeMap());
        }
    }
}
