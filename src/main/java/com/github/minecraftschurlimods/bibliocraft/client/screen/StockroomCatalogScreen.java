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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StockroomCatalogScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/stockroom_catalog.png");
    private static final int ROWS_PER_PAGE = 11;
    private final ItemStack stack;
    private final StockroomCatalogContent data;
    private boolean showContainerList = false;
    private int page = 0;
    private String search = "";
    private PageButton forwardButton;
    private PageButton backButton;
    private List<BlockPos> containers = List.of();
    private List<BlockPos> visibleContainers = List.of();
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
        int i = 0;
        if (showContainerList) {
            for (BlockPos pos : visibleContainers) {
                ItemStack blockItem = new ItemStack(Objects.requireNonNull(Minecraft.getInstance().level).getBlockState(pos).getBlock().asItem());
                graphics.renderItem(blockItem, x + 34, i * 19 + 29);
                //TODO add Locate button
                //TODO add Remove button
                String distanceText = Component.translatable(Translations.STOCKROOM_CATALOG_DISTANCE, (int) Objects.requireNonNull(Minecraft.getInstance().player).position().distanceTo(BCUtil.toVec3(pos))).getString();
                int distanceWidth = font.width(distanceText);
                graphics.drawString(font, distanceText, x + 187 - distanceWidth, i * 19 + 33, 0, false);
                String itemText = blockItem.getHoverName().getString(137 - distanceWidth);
                graphics.drawString(font, itemText, x + 51, i * 19 + 33, 0, false);
                i++;
            }
        } else {
            for (StockroomCatalogItemEntry entry : visibleItems) {
                graphics.renderItem(entry.item(), x + 34, i * 19 + 29);
                graphics.renderItem(entry.item(), x + 206, i * 19 + 29); //TODO replace with Locate button
                String countText = Component.translatable(Translations.STOCKROOM_CATALOG_COUNT, entry.count()).getString();
                int countWidth = font.width(countText);
                graphics.drawString(font, countText, x + 205 - countWidth, i * 19 + 33, 0, false);
                String itemText = entry.item().getHoverName().getString(153 - countWidth);
                graphics.drawString(font, itemText, x + 51, i * 19 + 33, 0, false);
                i++;
            }
            if (mouseX >= x + 34 && mouseX < x + 50) {
                int y = mouseY - 29;
                if (y > 0 && y % 19 < 16 && y / 19 < visibleItems.size()) {
                    ItemStack item = visibleItems.get(y / 19).item();
                    graphics.renderTooltip(font, getTooltipFromItem(getMinecraft(), item), item.getTooltipImage(), item, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        stack.set(BCDataComponents.STOCKROOM_CATALOG_CONTENT, data);
        PacketDistributor.sendToServer(new StockroomCatalogSyncPacket(data));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        int x = (width - 256) / 2;
        addRenderableWidget(Button.builder(Component.translatable(showContainerList ? Translations.STOCKROOM_CATALOG_SHOW_ITEMS : Translations.STOCKROOM_CATALOG_SHOW_CONTAINERS), $ -> toggleMode()).bounds(width / 2 - 100, 260, 98, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 + 2, 260, 98, 20).build());
        EditBox searchBox = addRenderableWidget(new EditBox(getMinecraft().font, x + 33, 15, 140, 8, Component.empty()));
        searchBox.setTextColor(0);
        searchBox.setBordered(false);
        searchBox.setTextShadow(false);
        searchBox.setHint(Component.translatable(Translations.STOCKROOM_CATALOG_SEARCH));
        searchBox.setResponder(e -> {
            search = e.toLowerCase(Locale.ROOT);
            updateContents();
        });
        forwardButton = addRenderableWidget(new PageButton(x + 194, 236, true, $ -> {
            page++;
            updateContents();
        }, false));
        backButton = addRenderableWidget(new PageButton(x + 30, 236, false, $ -> {
            page--;
            updateContents();
        }, false));
        updateContents();
        if (showContainerList) {
            addRenderableWidget(new SortButton<>(StockroomCatalogSorting.Container.ALPHABETICAL_ASC, x + 172, 11, 52, 14, b -> {
                SortButton<StockroomCatalogSorting.Container> button = (SortButton<StockroomCatalogSorting.Container>) b;
                button.setValue(switch (button.getValue()) {
                    case ALPHABETICAL_ASC -> StockroomCatalogSorting.Container.ALPHABETICAL_DESC;
                    case ALPHABETICAL_DESC -> StockroomCatalogSorting.Container.DISTANCE_ASC;
                    case DISTANCE_ASC -> StockroomCatalogSorting.Container.DISTANCE_DESC;
                    case DISTANCE_DESC -> StockroomCatalogSorting.Container.ALPHABETICAL_ASC;
                });
                containerSorting = button.getValue();
                requestPacket();
                updateContents();
            }));
        } else {
            addRenderableWidget(new SortButton<>(StockroomCatalogSorting.Item.ALPHABETICAL_ASC, x + 172, 11, 52, 14, b -> {
                SortButton<StockroomCatalogSorting.Item> button = (SortButton<StockroomCatalogSorting.Item>) b;
                button.setValue(switch (button.getValue()) {
                    case ALPHABETICAL_ASC -> StockroomCatalogSorting.Item.ALPHABETICAL_DESC;
                    case ALPHABETICAL_DESC -> StockroomCatalogSorting.Item.COUNT_ASC;
                    case COUNT_ASC -> StockroomCatalogSorting.Item.COUNT_DESC;
                    case COUNT_DESC -> StockroomCatalogSorting.Item.ALPHABETICAL_ASC;
                });
                itemSorting = button.getValue();
                requestPacket();
                updateContents();
            }));
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BACKGROUND, (width - 256) / 2 + 18, 2, 0, 0, 256, 256);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY < 0 && forwardButton.visible) {
            forwardButton.onPress();
            return true;
        }
        if (scrollY > 0 && backButton.visible) {
            backButton.onPress();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) return true;
        return switch (keyCode) {
            case GLFW.GLFW_KEY_PAGE_UP -> {
                backButton.onPress();
                yield true;
            }
            case GLFW.GLFW_KEY_PAGE_DOWN -> {
                forwardButton.onPress();
                yield true;
            }
            default -> false;
        };
    }

    public void setFromPacket(StockroomCatalogListPacket packet) {
        Level level = Objects.requireNonNull(Minecraft.getInstance().level);
        containers = packet.containers()
                .stream()
                .filter(e -> level.getCapability(Capabilities.ItemHandler.BLOCK, e, null) != null)
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
        search = "";
        rebuildWidgets();
        updateContents();
    }

    private void updateContents() {
        buildVisibleCache();
        forwardButton.visible = page < (showContainerList ? containers.size() - 1 : items.size() - 1) / ROWS_PER_PAGE;
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
        visibleContainers = containers.stream()
                .filter(e -> BCUtil.getNameAtPos(Objects.requireNonNull(Minecraft.getInstance().level), e).getString().toLowerCase(Locale.ROOT).contains(search))
                .skip((long) page * ROWS_PER_PAGE)
                .limit(ROWS_PER_PAGE)
                .toList();
    }

    private void buildVisibleItemsCache() {
        visibleItems = items.stream()
                .filter(e -> e.item().getHoverName().getString().toLowerCase(Locale.ROOT).contains(search))
                .skip((long) page * ROWS_PER_PAGE)
                .limit(ROWS_PER_PAGE)
                .toList();
    }

    private static class SortButton<E extends Enum<E> & StockroomCatalogSorting> extends Button {
        private E value;

        public SortButton(E initialValue, int x, int y, int width, int height, OnPress onPress) {
            super(new Builder(message(initialValue), onPress).bounds(x, y, width, height));
            value = initialValue;
        }

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
            setMessage(message(value));
        }
        
        private static <T extends StockroomCatalogSorting> Component message(T t) {
            return Component.translatable(Translations.STOCKROOM_CATALOG_SORT, Component.translatable(t.getTranslationKey()));
        }
    }
}
