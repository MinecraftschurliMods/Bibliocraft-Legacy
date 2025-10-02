package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPage;
import com.github.minecraftschurlimods.bibliocraft.init.BCDataComponents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TypewriterPageScreen extends Screen {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/typewriter_page.png");
    private final TypewriterPage page;
    private int leftPos;
    private int topPos;

    public TypewriterPageScreen(ItemStack stack) {
        super(stack.getHoverName());
        page = stack.getOrDefault(BCDataComponents.TYPEWRITER_PAGE, TypewriterPage.DEFAULT);
    }

    @Override
    protected void init() {
        leftPos = (width - TypewriterScreen.IMAGE_WIDTH) / 2;
        topPos = (height - TypewriterScreen.IMAGE_HEIGHT) / 2;
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose())
                .bounds(width / 2 - 100, topPos + TypewriterScreen.IMAGE_HEIGHT + 4, 200, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        Font font = ClientUtil.getFont();
        for (int i = 0; i < page.lines().size(); i++) {
            graphics.drawString(font, page.lines().get(i), leftPos + 2, topPos + 2 + i * 10, 0xff000000, false);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, leftPos, topPos, 0, 0, TypewriterScreen.IMAGE_WIDTH, TypewriterScreen.IMAGE_HEIGHT, 256, 256);
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }
}
