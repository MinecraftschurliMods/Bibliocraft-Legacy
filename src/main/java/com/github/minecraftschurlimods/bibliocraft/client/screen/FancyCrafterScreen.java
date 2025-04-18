package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterMenu;
import com.github.minecraftschurlimods.bibliocraft.content.fancycrafter.FancyCrafterSlot;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class FancyCrafterScreen extends BCMenuScreen<FancyCrafterMenu> {
    private static final ResourceLocation BACKGROUND = BCUtil.modLoc("textures/gui/fancy_crafter.png");
    private static final ResourceLocation DISABLED_SLOT = BCUtil.mcLoc("container/crafter/disabled_slot");
    private final Player player;

    public FancyCrafterScreen(FancyCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
        player = inventory.player;
        imageHeight = 192;
        inventoryLabelY = 99;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (hoveredSlot instanceof FancyCrafterSlot && !menu.isSlotDisabled(hoveredSlot.index) && menu.getCarried().isEmpty() && !hoveredSlot.hasItem() && !player.isSpectator()) {
            guiGraphics.renderTooltip(font, Translations.VANILLA_TOGGLABLE_SLOT, mouseX, mouseY);
        }
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        if (slot instanceof FancyCrafterSlot && menu.isSlotDisabled(slot.index)) {
            guiGraphics.blitSprite(DISABLED_SLOT, slot.x - 1, slot.y - 1, 18, 18);
        }
        super.renderSlot(guiGraphics, slot);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        if (slot instanceof FancyCrafterSlot && !slot.hasItem() && !player.isSpectator()) {
            switch (type) {
                case PICKUP -> {
                    if (menu.isSlotDisabled(slotId)) {
                        updateSlotState(slotId, false);
                    } else if (menu.getCarried().isEmpty()) {
                        updateSlotState(slotId, true);
                    }
                }
                case SWAP -> {
                    ItemStack stack = player.getInventory().getItem(mouseButton);
                    if (menu.isSlotDisabled(slotId) && !stack.isEmpty()) {
                        updateSlotState(slotId, false);
                    }
                }
            }
        }
        super.slotClicked(slot, slotId, mouseButton, type);
    }

    private void updateSlotState(int slot, boolean disabled) {
        menu.setSlotDisabled(slot, disabled);
        handleSlotStateChanged(slot, menu.containerId, disabled);
        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, disabled ? 0.75F : 1.0F);
    }
}
