package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogContent;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class StockroomCatalogScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/stockroom_catalog.png");
    private static final ResourceLocation BACKGROUND_BLANK = BCUtil.modLoc("textures/gui/stockroom_catalog_blank.png");
    private final ItemStack stack;
    private StockroomCatalogContent data;
    private boolean showContainerList = false;

    public StockroomCatalogScreen(ItemStack stack) {
        super(stack.getHoverName());
        this.stack = stack;
        this.data = stack.getOrDefault(BCDataComponents.STOCKROOM_CATALOG_CONTENT, StockroomCatalogContent.DEFAULT);
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
        if (showContainerList) {

        } else {
            
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (showContainerList) {

        } else {

        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(showContainerList ? BACKGROUND_BLANK : BACKGROUND, (width - 256) / 2 + 18, 2, 0, 0, 256, 256);
    }

    private void toggleMode() {
        showContainerList = !showContainerList;
        rebuildWidgets();
    }

    @Override
    public void onClose() {
        super.onClose();
        stack.set(BCDataComponents.STOCKROOM_CATALOG_CONTENT, data);
        PacketDistributor.sendToServer(new StockroomCatalogSyncPacket(data));
    }
}
