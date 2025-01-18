package com.github.minecraftschurlimods.bibliocraft.client.model;

import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TableModel extends DynamicBlockModel {
    public static final IGeometryLoader<Geometry> LOADER = (jsonObject, context) -> {
        Map<TableBlock.Type, BlockModel> map = new HashMap<>();
        for (TableBlock.Type type : TableBlock.Type.values()) {
            map.put(type, context.deserialize(GsonHelper.getAsJsonObject(jsonObject, type.getSerializedName()), BlockModel.class));
        }
        return new Geometry(map);
    };
    private final Map<TableBlock.Type, BakedModel> baseMap;

    public TableModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particle, Map<TableBlock.Type, BakedModel> baseMap) {
        super(useAmbientOcclusion, isGui3d, usesBlockLight, particle);
        this.baseMap = baseMap;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        TableBlock.Type type = TableBlock.Type.NONE;
        if (state != null && state.hasProperty(TableBlock.TYPE)) {
            type = state.getValue(TableBlock.TYPE);
        }
        return baseMap.get(type).getQuads(state, side, rand, extraData, renderType);
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        return baseMap.get(TableBlock.Type.NONE).applyTransform(transformType, poseStack, applyLeftHandTransform);
    }

    public static class Geometry implements IUnbakedGeometry<Geometry> {
        private final Map<TableBlock.Type, BlockModel> baseMap;

        public Geometry(Map<TableBlock.Type, BlockModel> baseMap) {
            this.baseMap = baseMap;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            ModelState simpleState = new SimpleModelState(modelState.getRotation(), modelState.isUvLocked());
            boolean useBlockLight = context.useBlockLight();
            Map<TableBlock.Type, BakedModel> newBaseMap = new HashMap<>();
            baseMap.forEach((k, v) -> newBaseMap.put(k, v.bake(baker, v, spriteGetter, simpleState, useBlockLight)));
            return new TableModel(context.useAmbientOcclusion(), context.isGui3d(), useBlockLight, spriteGetter.apply(context.getMaterial("particle")), newBaseMap);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            baseMap.forEach((k, v) -> v.resolveParents(modelGetter));
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder extends CustomLoaderBuilder<BlockModelBuilder> {
        private final Map<TableBlock.Type, JsonObject> modelMap = new HashMap<>();
        private ResourceLocation particle;

        public Builder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(BCUtil.modLoc("table"), parent, existingFileHelper, false);
        }

        public Builder withModelForType(TableBlock.Type type, JsonObject model) {
            modelMap.put(type, model);
            return this;
        }

        public Builder withParticle(ResourceLocation particle) {
            this.particle = particle;
            return this;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            if (particle == null) throw new IllegalStateException("Block particle was not specified!");
            JsonObject textures = new JsonObject();
            textures.addProperty("particle", particle.toString());
            json.add("textures", textures);
            modelMap.forEach((k, v) -> json.add(k.getSerializedName(), v));
            return super.toJson(json);
        }
    }
}
