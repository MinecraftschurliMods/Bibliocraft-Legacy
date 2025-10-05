package com.github.minecraftschurlimods.bibliocraft.client.ber;

import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.block.BCBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class TableBER implements BlockEntityRenderer<TableBlockEntity, TableBER.TableRenderState> {
    private static final RenderType MAP_BACKGROUND = RenderType.text(BCUtil.mcLoc("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(BCUtil.mcLoc("textures/map/map_background_checkerboard.png"));
    //private static final Map<TableBlock.Type, Map<DyeColor, BakedModel>> CLOTH_MAP = new HashMap<>();
    private final MapRenderer mapRenderer;
    private final ItemModelResolver itemModelResolver;

    public TableBER(BlockEntityRendererProvider.Context context) {
        Minecraft minecraft = ClientUtil.getMc();
        mapRenderer = minecraft.getMapRenderer();
        itemModelResolver = context.itemModelResolver();
    }

    public static void rebuildClothModelCache() {
        /*CLOTH_MAP.clear();
        ModelManager models = ClientUtil.getMc().getModelManager();
        for (TableBlock.Type type : TableBlock.Type.values()) {
            Map<DyeColor, BakedModel> map = new HashMap<>();
            for (DyeColor color : DyeColor.values()) {
                ResourceLocation loc = BCUtil.bcLoc("block/color/" + color.getSerializedName() + "/table_cloth_" + type.getSerializedName());
                map.put(color, models.getModel(ModelResourceLocation.standalone(loc)));
            }
            CLOTH_MAP.put(type, map);
        }*/
    }

    @Override
    public TableRenderState createRenderState() {
        return new TableRenderState();
    }

    @Override
    public void submit(TableRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        stack.pushPose();
        if (state.isMap) {
            stack.translate(0.125f, 1.001, 0.125f);
            stack.scale(0.75f, 0.75f, 0.75f);
            stack.scale(0.0078125f, 0.0078125f, 0.0078125f);
            stack.mulPose(Axis.XP.rotationDegrees(90));
            final int light = state.lightCoords;
            collector.submitCustomGeometry(stack, state.map == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD, (pose, vc) -> {
                Matrix4f matrix4f = pose.pose();
                vc.addVertex(matrix4f, -7f, 135f, 0f).setColor(-1).setUv(0f, 1f).setLight(light);
                vc.addVertex(matrix4f, 135f, 135f, 0f).setColor(-1).setUv(1f, 1f).setLight(light);
                vc.addVertex(matrix4f, 135f, -7f, 0f).setColor(-1).setUv(1f, 0f).setLight(light);
                vc.addVertex(matrix4f, -7f, -7f, 0f).setColor(-1).setUv(0f, 0f).setLight(light);
            });
            if (state.map != null) {
                mapRenderer.render(state.map, stack, collector, false, state.lightCoords);
            }
        } else if (state.itemStackState != null) {
            stack.translate(0.5, 1.03125, 0.5);
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.scale(0.5f, 0.5f, 0.5f);
            state.itemStackState.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }
        stack.popPose();
        if (state.clothColor == null)
            return;
        stack.pushPose();
        stack.translate(0.5, 0, 0.5);
        stack.mulPose(Axis.YP.rotationDegrees(switch (state.blockState.getValue(TableBlock.FACING)) {
            case EAST -> 270;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        }));
        stack.translate(-0.5, 0, -0.5);
        // BakedModel clothModel = CLOTH_MAP.get(state.blockState.getValue(TableBlock.TYPE)).get(state.clothColor);
        // TODO: How do we render the cloth model now?
        stack.popPose();
    }

    @Override
    public void extractRenderState(TableBlockEntity blockEntity, TableRenderState state, float partialTick, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, p_445788_, p_446944_);
        state.fill(blockEntity, ItemDisplayContext.FIXED, itemModelResolver, mapRenderer);
    }

    public static class TableRenderState extends SingleItemContainerRenderState {
        private @Nullable MapRenderState map;
        private boolean isMap;
        private @Nullable DyeColor clothColor;

        public void fill(BCBlockEntity blockEntity, ItemDisplayContext displayContext, ItemModelResolver itemModelResolver, MapRenderer mapRenderer) {
            ItemStack item = blockEntity.getItem(0);
            if (!item.is(Items.FILLED_MAP)) {
                super.fill(blockEntity, displayContext, itemModelResolver);
                return;
            }
            isMap = true;
            MapId mapId = item.get(DataComponents.MAP_ID);
            MapItemSavedData mapData = MapItem.getSavedData(mapId, blockEntity.level());
            if (mapData != null) {
                map = new MapRenderState();
                mapRenderer.extractRenderState(mapId, mapData, map);
            }
            ItemStack carpet = blockEntity.getItem(1);
            if (!carpet.isEmpty() && carpet.getItem() instanceof BlockItem bi && bi.getBlock() instanceof WoolCarpetBlock carpetBlock) {
                clothColor = carpetBlock.getColor();
            }
        }
    }
}
