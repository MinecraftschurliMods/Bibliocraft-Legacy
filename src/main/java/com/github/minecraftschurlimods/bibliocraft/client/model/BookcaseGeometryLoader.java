package com.github.minecraftschurlimods.bibliocraft.client.model;

import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlock;
import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlockEntity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BookcaseGeometryLoader implements IGeometryLoader<BookcaseGeometryLoader.BookcaseGeometry> {
    public static final BookcaseGeometryLoader INSTANCE = new BookcaseGeometryLoader();

    private BookcaseGeometryLoader() {
    }

    @Override
    public BookcaseGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        jsonObject.remove("loader");
        BlockModel base = context.deserialize(jsonObject, BlockModel.class);
        BlockModel[] books = new BlockModel[16];
        for (int i = 0; i < 16; i++) {
            String book = "book_" + i;
            books[i] = context.deserialize(GsonHelper.getAsJsonObject(jsonObject, book), BlockModel.class);
        }
        return new BookcaseGeometry(base, books);
    }

    public static class BookcaseGeometry implements IUnbakedGeometry<BookcaseGeometry> {
        private final BlockModel base;
        private final BlockModel[] books;

        public BookcaseGeometry(BlockModel base, BlockModel[] books) {
            this.base = base;
            this.books = books;
        }

        @SuppressWarnings("deprecation")
        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            BakedModel base = new ElementsModel(this.base.getElements()).bake(context, baker, spriteGetter, modelState, overrides, modelLocation);
            BakedModel[] books = new BakedModel[16];
            ModelState bookState = new SimpleModelState(modelState.getRotation(), false);
            boolean useBlockLight = context.useBlockLight();
            for (int j = 0; j < books.length; j++) {
                books[j] = this.books[j].bake(baker, this.books[j], spriteGetter, bookState, modelLocation, useBlockLight);
            }
            return new BookcaseDynamicModel(context.useAmbientOcclusion(), context.isGui3d(), useBlockLight, spriteGetter.apply(context.getMaterial("particle")), base, books);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            base.resolveParents(modelGetter);
            for (BlockModel book : books) {
                book.resolveParents(modelGetter);
            }
        }
    }

    public static class BookcaseDynamicModel implements IDynamicBakedModel {
        private final boolean useAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean usesBlockLight;
        private final TextureAtlasSprite particle;
        private final BakedModel base;
        private final BakedModel[] books;

        public BookcaseDynamicModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particle, BakedModel base, BakedModel[] books) {
            this.useAmbientOcclusion = useAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.usesBlockLight = usesBlockLight;
            this.particle = particle;
            this.base = base;
            this.books = books;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
            List<BakedQuad> quads = new ArrayList<>(base.getQuads(state, side, rand, extraData, renderType));
            for (int i = 0; i < books.length; i++) {
                Boolean book = extraData.get(BookcaseBlockEntity.MODEL_PROPERTIES.get(i));
                if (book == null || !book) continue;
                quads.addAll(books[i].getQuads(state, side, rand, extraData, renderType));
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
            return base.applyTransform(transformType, poseStack, applyLeftHandTransform);
        }

        @Override
        public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
            if (!(state.getBlock() instanceof BookcaseBlock)) return modelData;
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof BookcaseBlockEntity be)) return modelData;
            ModelData.Builder builder = ModelData.builder();
            for (int i = 0; i < BookcaseBlockEntity.MODEL_PROPERTIES.size(); i++) {
                builder.with(BookcaseBlockEntity.MODEL_PROPERTIES.get(i), !be.getItem(i).isEmpty());
            }
            return builder.build();
        }
    }
}
