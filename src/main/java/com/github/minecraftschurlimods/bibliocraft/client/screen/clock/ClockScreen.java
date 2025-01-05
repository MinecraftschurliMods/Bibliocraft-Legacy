package com.github.minecraftschurlimods.bibliocraft.client.screen.clock;

import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockSyncPacket;
import com.github.minecraftschurlimods.bibliocraft.content.clock.ClockTrigger;
import com.github.minecraftschurlimods.bibliocraft.util.Translations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

public class ClockScreen extends Screen {
    private static final Component TITLE = Component.translatable(Translations.CLOCK_TITLE);
    private final BlockPos pos;
    private final List<ClockTrigger> triggers;

    public ClockScreen(BlockPos pos) {
        super(TITLE);
        this.pos = pos;
        this.triggers = ((ClockBlockEntity) Objects.requireNonNull(Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(pos))).getTriggers();
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, $ -> onClose()).bounds(width / 2 - 100, 196, 200, 20).build());
    }

    @Override
    public void onClose() {
        PacketDistributor.sendToServer(new ClockSyncPacket(pos, triggers));
        super.onClose();
    }
}
