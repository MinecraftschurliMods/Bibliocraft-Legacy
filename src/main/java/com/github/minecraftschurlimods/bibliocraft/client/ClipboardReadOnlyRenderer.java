package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.CheckboxState;
import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

/**
 * Holds methods for rendering clipboard contents in a read-only manner. A lot of code in here is boiled-down code from GuiGraphics.
 */
@SuppressWarnings({"SameParameterValue"})
public final class ClipboardReadOnlyRenderer {

    public static void render(PoseStack pose, SubmitNodeCollector collector, Font font, ClipboardContent data, int width, int height) {
        /*pose.pushPose();
        blit(pose, collector, BACKGROUND, 0, 0, 0, 0, width, height, 256, 256);
        drawText(pose, collector, font, data.title(), 29, 2, 72, 8);
        ClipboardContent.Page page = data.pages().get(data.active());
        for (int i = 0; i < ClipboardContent.MAX_LINES; i++) {
            CheckboxState state = page.checkboxes().get(i);
            if (state == CheckboxState.CHECK) {
                blitSprite(pose, collector, CHECK_TEXTURE, 2, 15 * i + 14, 14, 14);
            } else if (state == CheckboxState.X) {
                blitSprite(pose, collector, X_TEXTURE, 2, 15 * i + 14, 14, 14);
            }
            drawText(pose, collector, font, page.lines().get(i), 17, 15 * i + 16, 109, 8);
        }
        pose.popPose();*/
    }
}
