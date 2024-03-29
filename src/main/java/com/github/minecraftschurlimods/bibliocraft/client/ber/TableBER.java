package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;

import java.util.Objects;

public class TableBER implements BlockEntityRenderer<TableBlockEntity> {
    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));

    @SuppressWarnings("unused")
    public TableBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TableBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        ItemStack item = blockEntity.getItem(0);
        if (item.is(Items.FILLED_MAP)) {
            stack.translate(0.125f, 1.001, 0.125f);
            stack.scale(0.75f, 0.75f, 0.75f);
            stack.scale(0.0078125f, 0.0078125f, 0.0078125f);
            stack.mulPose(Axis.XP.rotationDegrees(90));
            Minecraft minecraft = Minecraft.getInstance();
            Integer mapId = MapItem.getMapId(item);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, Objects.requireNonNull(minecraft.level));
            VertexConsumer vc = buffer.getBuffer(mapData == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
            Matrix4f matrix4f = stack.last().pose();
            vc.vertex(matrix4f, -7f, 135f, 0f).color(255, 255, 255, 255).uv(0f, 1f).uv2(light).endVertex();
            vc.vertex(matrix4f, 135f, 135f, 0f).color(255, 255, 255, 255).uv(1f, 1f).uv2(light).endVertex();
            vc.vertex(matrix4f, 135f, -7f, 0f).color(255, 255, 255, 255).uv(1f, 0f).uv2(light).endVertex();
            vc.vertex(matrix4f, -7f, -7f, 0f).color(255, 255, 255, 255).uv(0f, 0f).uv2(light).endVertex();
            if (mapData != null) {
                minecraft.gameRenderer.getMapRenderer().render(stack, buffer, mapId, mapData, false, light);
            }
        } else {
            stack.translate(0.5, 1.03125, 0.5);
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.scale(0.5f, 0.5f, 0.5f);
            ClientUtil.renderFixedItem(item, stack, buffer, light, overlay);
        }
    }
}
