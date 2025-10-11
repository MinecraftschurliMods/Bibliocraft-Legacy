package com.github.minecraftschurlimods.bibliocraft.client.screen;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCMenu;
import com.github.minecraftschurlimods.bibliocraft.util.slot.HasToggleableSlots;
import com.github.minecraftschurlimods.bibliocraft.util.slot.ToggleableSlot;
import com.github.minecraftschurlimods.bibliocraft.util.slot.ToggleableSlotSyncPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class BCScreenWithToggleableSlots<T extends BCMenu<?> & HasToggleableSlots> extends BCMenuScreen<T> {
    private static final ResourceLocation DISABLED_SLOT = BCUtil.mcLoc("container/crafter/disabled_slot");
    private final Player player;

    public BCScreenWithToggleableSlots(T menu, Inventory inventory, Component title, ResourceLocation background) {
        super(menu, inventory, title, background);
        player = inventory.player;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (hoveredSlot instanceof ToggleableSlot && !menu.isSlotDisabled(hoveredSlot.index) && menu.getCarried().isEmpty() && !hoveredSlot.hasItem() && !player.isSpectator()) {
            graphics.renderTooltip(font, ClientUtil.forTooltip(Translations.VANILLA_TOGGLABLE_SLOT), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }

    @Override
    protected void renderSlot(GuiGraphics graphics, Slot slot) {
        if (slot instanceof ToggleableSlot && menu.isSlotDisabled(slot.index)) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISABLED_SLOT, slot.x - 1, slot.y - 1, 18, 18);
            return;
        }
        super.renderSlot(graphics, slot);
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
        if (slot instanceof ToggleableSlot<?> && !slot.hasItem() && !player.isSpectator()) {
            switch (type) {
                case PICKUP -> {
                    if (menu.isSlotDisabled(slotId)) {
                        setSlotDisabled(slotId, false);
                    } else if (menu.getCarried().isEmpty()) {
                        setSlotDisabled(slotId, true);
                    }
                }
                case SWAP -> {
                    ItemStack stack = player.getInventory().getItem(mouseButton);
                    if (menu.isSlotDisabled(slotId) && !stack.isEmpty()) {
                        setSlotDisabled(slotId, false);
                    }
                }
            }
        }
        super.slotClicked(slot, slotId, mouseButton, type);
    }

    protected void setSlotDisabled(int slot, boolean disabled) {
        menu.setSlotDisabled(slot, disabled);
        ClientPacketDistributor.sendToServer(new ToggleableSlotSyncPacket(menu.getBlockEntity().getBlockPos(), slot, disabled));
        player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, disabled ? 0.75F : 1F);
    }
}
