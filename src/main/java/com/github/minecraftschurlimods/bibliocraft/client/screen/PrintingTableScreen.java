package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.client.widget.ExperienceBarButton;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableInputPacket;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMenu;
import com.github.minecraftschurlimods.bibliocraft.content.printingtable.PrintingTableMode;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class PrintingTableScreen extends BCScreenWithToggleableSlots<PrintingTableMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.bcLoc("textures/gui/printing_table.png");
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND = BCUtil.bcLoc("experience_bar_background");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS = BCUtil.bcLoc("experience_bar_progress");
    private static final ResourceLocation PROGRESS = BCUtil.bcLoc("printing_table_progress");
    private Button modeButton;
    private Button experienceBarButton;

    public PrintingTableScreen(PrintingTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
    }

    @Override
    protected void init() {
        leftPos = (width - 192) / 2;
        topPos = (height - 192) / 2;
        PrintingTableBlockEntity blockEntity = menu.getBlockEntity();
        modeButton = addRenderableWidget(Button.builder(Component.empty(), $ -> {
            blockEntity.setMode(switch (blockEntity.getMode()) {
                case BIND -> PrintingTableMode.CLONE;
                case CLONE -> PrintingTableMode.MERGE;
                case MERGE -> PrintingTableMode.BIND;
            });
            setModeButtonMessage();
            experienceBarButton.visible = blockEntity.getMode() == PrintingTableMode.CLONE;
            ClientPacketDistributor.sendToServer(new PrintingTableInputPacket(blockEntity.getBlockPos(), blockEntity.getMode()));
        }).bounds(leftPos + 81, topPos + 6, 82, 20).build());
        setModeButtonMessage();
        experienceBarButton = addRenderableWidget(new ExperienceBarButton(Translations.PRINTING_TABLE_ADD_EXPERIENCE, leftPos + 81, topPos + 65, 82, 5, EXPERIENCE_BAR_BACKGROUND, EXPERIENCE_BAR_PROGRESS,
                () -> BCUtil.getLevelForExperience(blockEntity.getExperience()),
                () -> {
                    if (blockEntity.getExperienceCost() <= 0) return 0f;
                    if (blockEntity.isExperienceFull()) return 1f;
                    int experience = blockEntity.getExperience();
                    int level = BCUtil.getLevelForExperience(experience);
                    float experienceForLevel = BCUtil.getExperienceForLevel(level);
                    float experienceForNextLevel = BCUtil.getExperienceForLevel(level + 1);
                    return Mth.clamp((experience - experienceForLevel) / (experienceForNextLevel - experienceForLevel), 0, 1);
                },
                $ -> {
                    if (blockEntity.isExperienceFull()) return;
                    int experienceCost = blockEntity.getExperienceCost();
                    LocalPlayer player = ClientUtil.getPlayer();
                    int experienceToGive = player.isCreative() ? experienceCost : Math.min(player.totalExperience, experienceCost);
                    if (experienceToGive > 0) {
                        blockEntity.addExperience(experienceToGive);
                        player.giveExperiencePoints(-experienceToGive);
                        ClientPacketDistributor.sendToServer(new PrintingTableInputPacket(blockEntity.getBlockPos(), experienceToGive));
                    }
                }
        ));
        experienceBarButton.visible = blockEntity.getMode() == PrintingTableMode.CLONE;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        float progress = menu.getBlockEntity().getProgress();
        int width = progress == 1f ? 0 : Mth.ceil(progress * 24);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS, 24, 16, 0, 0, leftPos + 110, topPos + 35, width, 16);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        int experienceCost = menu.getBlockEntity().getLevelCost();
        if (experienceCost > 0) {
            ClientUtil.renderXpText(experienceCost + "", graphics, leftPos + 122, topPos + 39);
        }
    }

    private void setModeButtonMessage() {
        modeButton.setMessage(Component.translatable(Translations.PRINTING_TABLE_MODE_KEY, Component.translatable(menu.getBlockEntity().getMode().getTranslationKey())));
    }
}
