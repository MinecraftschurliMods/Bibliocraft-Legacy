package com.github.minecraftschurlimods.bibliocraft.client.geometry;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlockEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TableGeometryLoader implements IGeometryLoader<TableGeometryLoader.TableGeometry> {
    public static final TableGeometryLoader INSTANCE = new TableGeometryLoader();

    private TableGeometryLoader() {
    }

    @Override
    public TableGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        Map<TableBlock.Type, BlockModel> map = new HashMap<>();
        for (TableBlock.Type type : TableBlock.Type.values()) {
            map.put(type, context.deserialize(GsonHelper.getAsJsonObject(jsonObject, type.getSerializedName()), BlockModel.class));
        }
        return new TableGeometry(map);
    }

    public static class TableGeometry implements IUnbakedGeometry<TableGeometry> {
        private final Map<TableBlock.Type, BlockModel> baseMap;

        public TableGeometry(Map<TableBlock.Type, BlockModel> baseMap) {
            this.baseMap = baseMap;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            ModelState simpleState = new SimpleModelState(modelState.getRotation(), false);
            boolean useBlockLight = context.useBlockLight();
            Map<TableBlock.Type, BakedModel> newBaseMap = new HashMap<>();
            baseMap.forEach((k, v) -> newBaseMap.put(k, v.bake(baker, v, spriteGetter, simpleState, modelLocation, useBlockLight)));
            return new TableDynamicModel(context.useAmbientOcclusion(), context.isGui3d(), useBlockLight, spriteGetter.apply(context.getMaterial("particle")), newBaseMap);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            baseMap.forEach((k, v) -> v.resolveParents(modelGetter));
        }
    }

    public static class TableDynamicModel implements IDynamicBakedModel {
        private static final Map<TableBlock.Type, Map<DyeColor, BakedModel>> CLOTH_MAP = new HashMap<>();
        private final boolean useAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean usesBlockLight;
        private final TextureAtlasSprite particle;
        private final Map<TableBlock.Type, BakedModel> baseMap;

        public TableDynamicModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particle, Map<TableBlock.Type, BakedModel> baseMap) {
            this.useAmbientOcclusion = useAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.usesBlockLight = usesBlockLight;
            this.particle = particle;
            this.baseMap = baseMap;
        }

        public static void rebuildClothModelCache() {
            CLOTH_MAP.clear();
            ModelManager models = Minecraft.getInstance().getModelManager();
            for (TableBlock.Type type : TableBlock.Type.values()) {
                Map<DyeColor, BakedModel> map = new HashMap<>();
                for (DyeColor color : DyeColor.values()) {
                    map.put(color, models.getModel(new ResourceLocation(Bibliocraft.MOD_ID, "block/table_cloth_" + type.getSerializedName() + "_" + color.getSerializedName())));
                }
                CLOTH_MAP.put(type, map);
            }
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
            TableBlock.Type type = extraData.get(TableBlockEntity.TYPE_PROPERTY);
            List<BakedQuad> quads = new ArrayList<>(baseMap.get(type).getQuads(state, side, rand, extraData, renderType));
            if (extraData.has(TableBlockEntity.COLOR_PROPERTY)) {
                quads.addAll(CLOTH_MAP.get(type).get(extraData.get(TableBlockEntity.COLOR_PROPERTY)).getQuads(state, side, rand, extraData, renderType));
            }
            return quads;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return useAmbientOcclusion;
        }

        @Override
        public boolean isGui3d() {
            return isGui3d;
        }

        @Override
        public boolean usesBlockLight() {
            return usesBlockLight;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return particle;
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }

        @Override
        public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
            return baseMap.get(TableBlock.Type.NONE).applyTransform(transformType, poseStack, applyLeftHandTransform);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class LoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
        private final Map<TableBlock.Type, JsonObject> modelMap = new HashMap<>();
        private ResourceLocation particle;

        public LoaderBuilder(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(new ResourceLocation(Bibliocraft.MOD_ID, "table"), parent, existingFileHelper, false);
        }

        public LoaderBuilder withModelForType(TableBlock.Type type, JsonObject model) {
            modelMap.put(type, model);
            return this;
        }

        public LoaderBuilder withParticle(ResourceLocation particle) {
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
