package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.client.ber.CookieJarBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.DinnerPlateBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.DiscRackBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.DisplayCaseBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.FancyArmorStandBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.LabelBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.PotionShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ShelfBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.SwordPedestalBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.TableBER;
import com.github.minecraftschurlimods.bibliocraft.client.ber.ToolRackBER;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseModel;
import com.github.minecraftschurlimods.bibliocraft.client.model.TableModel;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BCMenuScreens;
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
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.Objects;

public final class ClientHandler {
    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = BibliocraftApi.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent
        private static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(BCMenus.BOOKCASE.get(),          BCMenuScreens.Bookcase::new);
            event.register(BCMenus.COOKIE_JAR.get(),        BCMenuScreens.CookieJar::new);
            event.register(BCMenus.DISC_RACK.get(),         BCMenuScreens.DiscRack::new);
            event.register(BCMenus.FANCY_ARMOR_STAND.get(), BCMenuScreens.FancyArmorStand::new);
            event.register(BCMenus.LABEL.get(),             BCMenuScreens.Label::new);
            event.register(BCMenus.POTION_SHELF.get(),      BCMenuScreens.PotionShelf::new);
            event.register(BCMenus.SHELF.get(),             BCMenuScreens.Shelf::new);
            event.register(BCMenus.TOOL_RACK.get(),         BCMenuScreens.ToolRack::new);
        }

        @SubscribeEvent
        private static void registerAdditional(ModelEvent.RegisterAdditional event) {
            for (TableBlock.Type type : TableBlock.Type.values()) {
                for (DyeColor color : DyeColor.values()) {
                    event.register(BCUtil.modLoc("block/color/" + color.getSerializedName() + "/table_cloth_" + type.getSerializedName()));
                }
            }
        }

        @SubscribeEvent
        private static void bakingCompleted(ModelEvent.BakingCompleted event) {
            TableModel.rebuildClothModelCache();
        }

        @SubscribeEvent
        private static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register(BCUtil.modLoc("bookcase"), BookcaseModel.LOADER);
            event.register(BCUtil.modLoc("table"), TableModel.LOADER);
        }

        @SubscribeEvent
        private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(BCEntities.FANCY_ARMOR_STAND.get(), ArmorStandRenderer::new);
            event.registerEntityRenderer(BCEntities.SEAT.get(), EmptyEntityRenderer::new);
            event.registerBlockEntityRenderer(BCBlockEntities.COOKIE_JAR.get(),        $ -> new CookieJarBER());
            event.registerBlockEntityRenderer(BCBlockEntities.DINNER_PLATE.get(),      $ -> new DinnerPlateBER());
            event.registerBlockEntityRenderer(BCBlockEntities.DISPLAY_CASE.get(),      $ -> new DisplayCaseBER());
            event.registerBlockEntityRenderer(BCBlockEntities.DISC_RACK.get(),         $ -> new DiscRackBER());
            event.registerBlockEntityRenderer(BCBlockEntities.FANCY_ARMOR_STAND.get(), $ -> new FancyArmorStandBER());
            event.registerBlockEntityRenderer(BCBlockEntities.LABEL.get(),             $ -> new LabelBER());
            event.registerBlockEntityRenderer(BCBlockEntities.POTION_SHELF.get(),      $ -> new PotionShelfBER());
            event.registerBlockEntityRenderer(BCBlockEntities.SHELF.get(),             $ -> new ShelfBER());
            event.registerBlockEntityRenderer(BCBlockEntities.SWORD_PEDESTAL.get(),    $ -> new SwordPedestalBER());
            event.registerBlockEntityRenderer(BCBlockEntities.TABLE.get(),             $ -> new TableBER());
            event.registerBlockEntityRenderer(BCBlockEntities.TOOL_RACK.get(),         $ -> new ToolRackBER());
        }

        @SubscribeEvent
        private static void registerColorHandlersBlock(RegisterColorHandlersEvent.Block event) {
            event.register((state, level, pos, tintIndex) -> tintIndex == 0 && Objects.requireNonNull(level).getBlockEntity(Objects.requireNonNull(pos)) instanceof SwordPedestalBlockEntity spbe ? spbe.getColor() : -1, BCBlocks.SWORD_PEDESTAL.get());
        }

        @SubscribeEvent
        private static void registerColorHandlersItem(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> tintIndex == 0 ? stack.getOrDefault(DataComponents.DYED_COLOR, SwordPedestalBlock.DEFAULT_COLOR).rgb() : -1, BCItems.SWORD_PEDESTAL.get());
        }
    }
}
