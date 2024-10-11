package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogContent;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogItemEntry;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogRequestListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSorting;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class StockroomCatalogScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/stockroom_catalog.png");
    private static final ResourceLocation BACKGROUND_BLANK = BCUtil.modLoc("textures/gui/stockroom_catalog_blank.png");
    private final ItemStack stack;
    private StockroomCatalogContent data;
    private boolean showContainerList = false;
    private int page = 0;
    private PageButton forwardButton;
    private PageButton backButton;
    private List<Pair<BlockPos, IItemHandler>> containers = List.of();
    private List<Pair<BlockPos, IItemHandler>> visibleContainers = List.of();
    private List<StockroomCatalogItemEntry> items = List.of();
    private List<StockroomCatalogItemEntry> visibleItems = List.of();
    private StockroomCatalogSorting.Container containerSorting = StockroomCatalogSorting.Container.ALPHABETICAL_ASC;
    private StockroomCatalogSorting.Item itemSorting = StockroomCatalogSorting.Item.ALPHABETICAL_ASC;

    public StockroomCatalogScreen(ItemStack stack) {
        super(stack.getHoverName());
        this.stack = stack;
        this.data = stack.getOrDefault(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT);
        requestPacket();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        int x = (width - 256) / 2;
        if (showContainerList) {

        } else {
            int i = 0;
            for (StockroomCatalogItemEntry entry : visibleItems) {
                graphics.renderItem(entry.item(), x + 33, i * 19 + 29);
                i++;
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        stack.set(BCDataComponents.STOCKROOM_CATALOG_CONTENT, data);
        PacketDistributor.sendToServer(new StockroomCatalogSyncPacket(data));
    }

    @Override
    protected void init() {
        int x = (width - 256) / 2;
        addRenderableWidget(Button.builder(Component.translatable(showContainerList ? Translations.STOCKROOM_CATALOG_SHOW_ITEMS : Translations.STOCKROOM_CATALOG_SHOW_CONTAINERS), $ -> toggleMode()).bounds(width / 2 - 100, 260, 98, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 + 2, 260, 98, 20).build());
        EditBox titleBox = addRenderableWidget(new EditBox(getMinecraft().font, x + 58, 15, 140, 8, Component.empty()));
        titleBox.setTextColor(0);
        titleBox.setBordered(false);
        titleBox.setTextShadow(false);
        titleBox.setValue(data.title());
        titleBox.setResponder(e -> data = data.setTitle(e));
        forwardButton = addRenderableWidget(new PageButton(x + 116, 159, true, $ -> {
            page++;
            updateContents();
        }, false));
        backButton = addRenderableWidget(new PageButton(x + 43, 159, false, $ -> {
            page--;
            updateContents();
        }, false));
        updateContents();
        if (showContainerList) {

        } else {

        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(showContainerList ? BACKGROUND_BLANK : BACKGROUND, (width - 256) / 2 + 18, 2, 0, 0, 256, 256);
    }

    public void setFromPacket(StockroomCatalogListPacket packet) {
        Level level = Objects.requireNonNull(Minecraft.getInstance().level);
        containers = packet.containers()
                .stream()
                .map(e -> Pair.of(e, level.getCapability(Capabilities.ItemHandler.BLOCK, e, null)))
                .filter(e -> e.getSecond() != null)
                .toList();
        items = packet.items();
        updateContents();
    }

    private void requestPacket() {
        PacketDistributor.sendToServer(new StockroomCatalogRequestListPacket(containerSorting, itemSorting));
    }

    private void toggleMode() {
        showContainerList = !showContainerList;
        page = 0;
        rebuildWidgets();
        updateContents();
    }

    private void updateContents() {
        buildVisibleCache();
        forwardButton.visible = page < (showContainerList ? containers.size() / 16 : items.size() / 16) - 1;
        backButton.visible = page > 0;
    }

    private void buildVisibleCache() {
        if (showContainerList) {
            buildVisibleContainersCache();
        } else {
            buildVisibleItemsCache();
        }
    }

    private void buildVisibleContainersCache() {
        int rowsPerPage = 16;
        visibleContainers = containers.stream()
                .skip((long) page * rowsPerPage)
                .limit(rowsPerPage)
                .toList();
    }

    private void buildVisibleItemsCache() {
        int rowsPerPage = 16;
        visibleItems = items.stream()
                .skip((long) page * rowsPerPage)
                .limit(rowsPerPage)
                .toList();
    }
}
