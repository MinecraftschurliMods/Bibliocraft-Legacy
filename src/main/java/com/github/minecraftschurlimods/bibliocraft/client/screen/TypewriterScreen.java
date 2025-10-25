package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterPage;
import com.github.minecraftschurlimods.bibliocraft.content.typewriter.TypewriterSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.init.BCSoundEvents;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class TypewriterScreen extends Screen {
    public static final int IMAGE_WIDTH = 100;
    public static final int IMAGE_HEIGHT = 144;
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/typewriter_page.png");
    private final RandomSource random = RandomSource.create(Util.getNanos());
    private final BlockPos pos;
    private TypewriterPage page;
    private int leftPos;
    private int topPos;
    private int row;
    private String currentLine;
    private int frameTick;
    private boolean hasPendingSound = false;

    public TypewriterScreen(BlockPos pos) {
        super(Translations.TYPEWRITER_TITLE);
        this.pos = pos;
        page = ClientUtil.getLevel().getBlockEntity(pos) instanceof TypewriterBlockEntity typewriter ? typewriter.getPage() : TypewriterPage.DEFAULT;
        row = page.line();
        if (row == TypewriterPage.MAX_LINES) {
            onClose();
        } else {
            currentLine = page.lines().get(row);
        }
    }

    @Override
    protected void init() {
        leftPos = (width - IMAGE_WIDTH) / 2;
        topPos = (height - IMAGE_HEIGHT) / 2;
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose())
                .bounds(width / 2 - 100, topPos + IMAGE_HEIGHT + 4, 200, 20)
                .build());
    }

    @Override
    public void tick() {
        super.tick();
        frameTick++;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        sync();
        super.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        Font font = ClientUtil.getFont();
        for (int i = 0; i < row; i++) {
            if (i >= page.lines().size()) continue;
            graphics.drawString(font, page.lines().get(i), leftPos + 2, topPos + 2 + i * 10, 0, false);
        }
        if (row < TypewriterPage.MAX_LINES) {
            int width = font.width(currentLine);
            graphics.drawString(font, currentLine, leftPos + 2, topPos + 2 + row * 10, 0, false);
            if (frameTick / 6 % 2 == 0) {
                graphics.drawString(font, "_", leftPos + 2 + width, topPos + 2 + row * 10, 0, false);
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, leftPos, topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (!event.isConfirmation()) {
            return super.keyPressed(event);
        }
        lineBreak();
        return true;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (!StringUtil.isAllowedChatCharacter(event.codepoint())) {
            return super.charTyped(event);
        }
        currentLine += event.codepointAsString();
        if (currentLine.length() >= TypewriterPage.MAX_LINE_LENGTH) {
            lineBreak();
        } else {
            ClientUtil.getPlayer().playSound(BCSoundEvents.TYPEWRITER_TYPE.value(), 0.9f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.2f);
        }
        return true;
    }

    private void sync() {
        page = page.copy().withLine(row);
        if (row < page.lines().size()) {
            page.lines().set(row, currentLine);
        }
        if (ClientUtil.getLevel().getBlockEntity(pos) instanceof TypewriterBlockEntity typewriter) {
            typewriter.setPage(page);
        }
        ClientPacketDistributor.sendToServer(new TypewriterSyncPacket(pos, page, hasPendingSound));
        if (hasPendingSound) {
            ClientUtil.getPlayer().playSound(BCSoundEvents.TYPEWRITER_CHIME.value());
            hasPendingSound = false;
        }
    }

    private void lineBreak() {
        page = page.copy();
        page.lines().set(row, currentLine);
        currentLine = "";
        row++;
        hasPendingSound = true;
        if (row >= TypewriterPage.MAX_LINES) {
            onClose();
        } else {
            sync();
        }
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }
}
