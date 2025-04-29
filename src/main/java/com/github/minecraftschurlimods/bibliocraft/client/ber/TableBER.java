package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class TableBER implements BlockEntityRenderer<TableBlockEntity> {
    private static final RenderType MAP_BACKGROUND = RenderType.text(BCUtil.mcLoc("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(BCUtil.mcLoc("textures/map/map_background_checkerboard.png"));
    private static final Map<TableBlock.Type, Map<DyeColor, BakedModel>> CLOTH_MAP = new HashMap<>();
    private static final RandomSource RANDOM = RandomSource.create();

    public static void rebuildClothModelCache() {
        CLOTH_MAP.clear();
        ModelManager models = Minecraft.getInstance().getModelManager();
        for (TableBlock.Type type : TableBlock.Type.values()) {
            Map<DyeColor, BakedModel> map = new HashMap<>();
            for (DyeColor color : DyeColor.values()) {
                map.put(color, models.getModel(ModelResourceLocation.standalone(BCUtil.bcLoc("block/color/" + color.getSerializedName() + "/table_cloth_" + type.getSerializedName()))));
            }
            CLOTH_MAP.put(type, map);
        }
    }

    @Override
    public void render(TableBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        ItemStack item = blockEntity.getItem(0);
        stack.pushPose();
        if (item.is(Items.FILLED_MAP)) {
            stack.translate(0.125f, 1.001, 0.125f);
            stack.scale(0.75f, 0.75f, 0.75f);
            stack.scale(0.0078125f, 0.0078125f, 0.0078125f);
            stack.mulPose(Axis.XP.rotationDegrees(90));
            Minecraft minecraft = Minecraft.getInstance();
            MapId mapId = item.get(DataComponents.MAP_ID);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, ClientUtil.getLevel());
            VertexConsumer vc = buffer.getBuffer(mapData == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
            Matrix4f matrix4f = stack.last().pose();
            vc.addVertex(matrix4f, -7f, 135f, 0f).setColor(-1).setUv(0f, 1f).setLight(light);
            vc.addVertex(matrix4f, 135f, 135f, 0f).setColor(-1).setUv(1f, 1f).setLight(light);
            vc.addVertex(matrix4f, 135f, -7f, 0f).setColor(-1).setUv(1f, 0f).setLight(light);
            vc.addVertex(matrix4f, -7f, -7f, 0f).setColor(-1).setUv(0f, 0f).setLight(light);
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
        stack.popPose();
        ItemStack carpet = blockEntity.getItem(1);
        if (carpet.isEmpty() || !(carpet.getItem() instanceof BlockItem bi && bi.getBlock() instanceof WoolCarpetBlock carpetBlock))
            return;
        BlockState state = blockEntity.getBlockState();
        stack.pushPose();
        stack.translate(0.5, 0, 0.5);
        stack.mulPose(Axis.YP.rotationDegrees(switch (state.getValue(TableBlock.FACING)) {
            case SOUTH -> 180;
            case EAST -> 270;
            default -> 0;
            case WEST -> 90;
        }));
        stack.translate(-0.5, 0, -0.5);
        ClientUtil.renderBakedModel(CLOTH_MAP.get(state.getValue(TableBlock.TYPE)).get(carpetBlock.getColor()), stack, buffer, blockEntity.getLevel(), blockEntity.getBlockPos(), state, RANDOM, ModelData.EMPTY);
        stack.popPose();
    }
}
