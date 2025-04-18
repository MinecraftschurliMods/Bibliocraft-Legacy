package com.github.minecraftschurlimods.bibliocraft.client.model;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.google.gson.JsonArray;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
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
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BookcaseModel extends DynamicBlockModel {
    private static final RandomSource RANDOM = RandomSource.create(Util.getNanos());
    public static final IGeometryLoader<BookcaseGeometry> LOADER = (jsonObject, context) -> {
        jsonObject.remove("loader");
        BlockModel base = context.deserialize(jsonObject, BlockModel.class);
        BlockModel[] books = new BlockModel[16];
        if (ClientUtil.isPride()) {
            JsonArray prideBooks = jsonObject.getAsJsonArray("pride_books");
            if (!prideBooks.isEmpty()) {
                jsonObject = prideBooks.get(RANDOM.nextInt(prideBooks.size())).getAsJsonObject();
            }
        }
        for (int i = 0; i < 16; i++) {
            books[i] = context.deserialize(GsonHelper.getAsJsonObject(jsonObject, "book_" + i), BlockModel.class);
        }
        return new BookcaseGeometry(base, books);
    };
    private final BakedModel base;
    private final BakedModel[] books;

    public BookcaseModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particle, BakedModel base, BakedModel[] books) {
        super(useAmbientOcclusion, isGui3d, usesBlockLight, particle);
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
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        return base.applyTransform(transformType, poseStack, applyLeftHandTransform);
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
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            BakedModel base = new ElementsModel(this.base.getElements()).bake(context, baker, spriteGetter, modelState, overrides);
            BakedModel[] books = new BakedModel[16];
            ModelState bookState = new SimpleModelState(modelState.getRotation(), modelState.isUvLocked());
            boolean useBlockLight = context.useBlockLight();
            for (int j = 0; j < books.length; j++) {
                books[j] = this.books[j].bake(baker, this.books[j], spriteGetter, bookState, useBlockLight);
            }
            return new BookcaseModel(context.useAmbientOcclusion(), context.isGui3d(), useBlockLight, spriteGetter.apply(context.getMaterial("particle")), base, books);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            base.resolveParents(modelGetter);
            for (BlockModel book : books) {
                book.resolveParents(modelGetter);
            }
        }
    }
}
