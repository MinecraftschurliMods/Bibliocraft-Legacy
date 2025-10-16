package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.SpriteButton;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogContent;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogItemEntry;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogRequestListPacket;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSorting;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.LecternUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.TakeLecternBookPacket;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class StockroomCatalogScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/stockroom_catalog.png");
    private static final ResourceLocation LOCATE_ICON = BCUtil.bcLoc("locate");
    private static final ResourceLocation LOCATE_ICON_HIGHLIGHTED = BCUtil.bcLoc("locate_highlighted");
    private static final ResourceLocation REMOVE_ICON = BCUtil.bcLoc("remove");
    private static final ResourceLocation REMOVE_ICON_HIGHLIGHTED = BCUtil.bcLoc("remove_highlighted");
    private static final int ROWS_PER_PAGE = 11;
    private static final int PARTICLE_COUNT = 16;
    private final ItemStack stack;
    private final Player player;
    private final InteractionHand hand;
    private final BlockPos lectern;
    private final RandomSource random = RandomSource.create();
    private final List<Button> removeButtons = new ArrayList<>();
    private final List<Button> locateButtons = new ArrayList<>();
    private StockroomCatalogContent data;
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

    public StockroomCatalogScreen(ItemStack stack, Player player, InteractionHand hand) {
        this(stack, player, hand, null);
    }

    public StockroomCatalogScreen(ItemStack stack, Player player, BlockPos lectern) {
        this(stack, player, null, lectern);
    }

    private StockroomCatalogScreen(ItemStack stack, Player player, InteractionHand hand, BlockPos lectern) {
        super(stack.getHoverName());
        this.stack = stack;
        this.player = player;
        this.hand = hand;
        this.lectern = lectern;
        this.data = stack.getOrDefault(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT);
        requestPacket();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        int x = (width - 256) / 2;
        int y = mouseY - 29;
        int i = 0;
        if (showContainerList) {
            for (BlockPos pos : visibleContainers) {
                ItemStack blockItem = new ItemStack(ClientUtil.getLevel().getBlockState(pos).getBlock().asItem());
                graphics.renderItem(blockItem, x + 34, i * 19 + 29);
                graphics.renderItemDecorations(font, blockItem, x + 34, i * 19 + 29);
                String itemText = blockItem.getHoverName().getString(137);
                graphics.drawString(font, itemText, x + 51, i * 19 + 33, 0, false);
                i++;
            }
            if (mouseX >= x + 34 && mouseX < x + 50) {
                if (y > 0 && y % 19 < 16 && y / 19 < visibleContainers.size()) {
                    BlockPos container = visibleContainers.get(y / 19);
                    int distance = (int) (lectern != null ? Math.sqrt(lectern.distSqr(container)) : ClientUtil.getPlayer().position().distanceTo(BCUtil.toVec3(container)));
                    graphics.renderTooltip(font, ClientUtil.forTooltip(Component.translatable(Translations.STOCKROOM_CATALOG_DISTANCE_KEY, distance)), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
                }
            }
            if (mouseX >= x + 189 && mouseX < x + 205) {
                if (y > 0 && y % 19 < 16 && y / 19 < visibleContainers.size()) {
                    graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.STOCKROOM_CATALOG_REMOVE), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
                }
            }
            if (mouseX >= x + 206 && mouseX < x + 222) {
                if (y > 0 && y % 19 < 16 && y / 19 < visibleContainers.size()) {
                    graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.STOCKROOM_CATALOG_LOCATE), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
                }
            }
        } else {
            for (StockroomCatalogItemEntry entry : visibleItems) {
                graphics.renderItem(entry.item(), x + 34, i * 19 + 29);
                graphics.renderItemDecorations(font, entry.item(), x + 34, i * 19 + 29);
                String countText = Component.translatable(Translations.STOCKROOM_CATALOG_COUNT_KEY, entry.count()).getString();
                int countWidth = font.width(countText);
                graphics.drawString(font, countText, x + 205 - countWidth, i * 19 + 33, 0, false);
                String itemText = entry.item().getHoverName().getString(153 - countWidth);
                graphics.drawString(font, itemText, x + 51, i * 19 + 33, 0, false);
                i++;
            }
            if (mouseX >= x + 34 && mouseX < x + 50) {
                if (y > 0 && y % 19 < 16 && y / 19 < visibleItems.size()) {
                    ItemStack item = visibleItems.get(y / 19).item();
                    graphics.setTooltipForNextFrame(font, item, mouseX, mouseY);
                }
            }
            if (mouseX >= x + 206 && mouseX < x + 222) {
                if (y > 0 && y % 19 < 16 && y / 19 < visibleItems.size()) {
                    graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.STOCKROOM_CATALOG_LOCATE), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return lectern == null;
    }

    @Override
    public void onClose() {
        super.onClose();
        setDataOnStack();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        locateButtons.clear();
        removeButtons.clear();
        int x = (width - 256) / 2;
        Component switchComponent = showContainerList ? Translations.STOCKROOM_CATALOG_SHOW_ITEMS : Translations.STOCKROOM_CATALOG_SHOW_CONTAINERS;
        if (lectern != null) {
            addRenderableWidget(Button.builder(Translations.VANILLA_TAKE_BOOK, $ -> {
                LecternUtil.takeLecternBook(player, player.level(), lectern);
                ClientPacketDistributor.sendToServer(new TakeLecternBookPacket(lectern));
                onClose();
            }).bounds(width / 2 - 151, 260, 98, 20).build());
            addRenderableWidget(Button.builder(switchComponent, $ -> toggleMode()).bounds(width / 2 - 49, 260, 98, 20).build());
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 + 53, 260, 98, 20).build());
        } else {
            addRenderableWidget(Button.builder(switchComponent, $ -> toggleMode()).bounds(width / 2 - 100, 260, 98, 20).build());
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 + 2, 260, 98, 20).build());
        }
        EditBox searchBox = addRenderableWidget(new EditBox(getMinecraft().font, x + 33, 15, 140, 8, Component.empty()));
        searchBox.setTextColor(0);
        searchBox.setBordered(false);
        searchBox.setTextShadow(false);
        searchBox.setHint(Translations.STOCKROOM_CATALOG_SEARCH);
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
            for (int i = 0; i < ROWS_PER_PAGE; i++) {
                final int j = i; // I love Java
                removeButtons.add(addRenderableWidget(new SpriteButton.RegularAndHighlightSprite(REMOVE_ICON, REMOVE_ICON_HIGHLIGHTED, x + 189, i * 19 + 29, 16, 16, p -> {
                    updateData(data -> data.remove(new GlobalPos(ClientUtil.getLevel().dimension(), visibleContainers.get(j))));
                    setDataOnStack();
                    requestPacket();
                    updateContents();
                })));
                locateButtons.add(addRenderableWidget(new SpriteButton.RegularAndHighlightSprite(LOCATE_ICON, LOCATE_ICON_HIGHLIGHTED, x + 206, i * 19 + 29, 16, 16, p -> addParticles(visibleContainers.get(j)))));
            }
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
            for (int i = 0; i < ROWS_PER_PAGE; i++) {
                final int j = i; // I love Java
                locateButtons.add(addRenderableWidget(new SpriteButton.RegularAndHighlightSprite(LOCATE_ICON, LOCATE_ICON_HIGHLIGHTED, x + 206, i * 19 + 29, 16, 16, p -> addParticles(visibleItems.get(j)))));
            }
        }
        updateContents();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, (width - 256) / 2 + 18, 2, 0, 0, 256, 256, 256, 256);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY < 0 && forwardButton.visible) {
            forwardButton.onPress(new MouseButtonInfo(0, 0));
            return true;
        }
        if (scrollY > 0 && backButton.visible) {
            backButton.onPress(new MouseButtonInfo(0, 0));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event)) return true;
        return switch (event.key()) {
            case GLFW.GLFW_KEY_PAGE_UP -> {
                backButton.onPress(event);
                yield true;
            }
            case GLFW.GLFW_KEY_PAGE_DOWN -> {
                forwardButton.onPress(event);
                yield true;
            }
            default -> false;
        };
    }

    public void setFromPacket(StockroomCatalogListPacket packet) {
        Level level = ClientUtil.getLevel();
        containers = packet.containers()
                .stream()
                .filter(e -> level.getCapability(Capabilities.Item.BLOCK, e, null) != null)
                .toList();
        items = packet.items();
        updateContents();
    }

    private void requestPacket() {
        if (hand != null) {
            ClientPacketDistributor.sendToServer(new StockroomCatalogRequestListPacket(containerSorting, itemSorting, Either.left(hand)));
        } else if (lectern != null) {
            ClientPacketDistributor.sendToServer(new StockroomCatalogRequestListPacket(containerSorting, itemSorting, Either.right(lectern)));
        }
    }

    private void setDataOnStack() {
        stack.set(BCDataComponents.STOCKROOM_CATALOG_CONTENT, data);
        if (hand != null) {
            ClientPacketDistributor.sendToServer(new StockroomCatalogSyncPacket(data, Either.left(hand)));
        } else if (lectern != null) {
            ClientPacketDistributor.sendToServer(new StockroomCatalogSyncPacket(data, Either.right(lectern)));
        }
    }

    private void toggleMode() {
        showContainerList = !showContainerList;
        page = 0;
        search = "";
        rebuildWidgets();
        updateContents();
    }

    private void updateData(UnaryOperator<StockroomCatalogContent> operator) {
        data = operator.apply(data);
    }

    private void updateContents() {
        buildVisibleCache();
        forwardButton.visible = page < (showContainerList ? containers.size() - 1 : items.size() - 1) / ROWS_PER_PAGE;
        backButton.visible = page > 0;
        removeButtons.forEach(e -> e.visible = true);
        locateButtons.forEach(e -> e.visible = true);
        if (showContainerList && visibleContainers.size() < ROWS_PER_PAGE) {
            IntStream.range(visibleContainers.size(), ROWS_PER_PAGE).forEach(i -> removeButtons.get(i).visible = false);
            IntStream.range(visibleContainers.size(), ROWS_PER_PAGE).forEach(i -> locateButtons.get(i).visible = false);
        }
        if (!showContainerList && visibleItems.size() < ROWS_PER_PAGE) {
            IntStream.range(visibleItems.size(), ROWS_PER_PAGE).forEach(i -> locateButtons.get(i).visible = false);
        }
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
                .filter(e -> BCUtil.getNameAtPos(ClientUtil.getLevel(), e).getString().toLowerCase(Locale.ROOT).contains(search))
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

    private void addParticles(StockroomCatalogItemEntry entry) {
        for (BlockPos pos : entry.containers()) {
            addParticles(pos);
        }
    }

    private void addParticles(BlockPos pos) {
        ParticleEngine particles = ClientUtil.getMc().particleEngine;
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            Particle particle = particles.createParticle(ParticleTypes.POOF, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0, 0);
            if (particle != null) {
                particle.setLifetime((int) (random.nextDouble() * 20) + 60);
            }
        }
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
            return Component.translatable(Translations.STOCKROOM_CATALOG_SORT_KEY, Component.translatable(t.getTranslationKey()));
        }
    }
}
