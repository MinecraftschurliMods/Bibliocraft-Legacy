package com.github.minecraftschurlimods.bibliocraft.client.model;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlockEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;

import java.util.List;
import java.util.Objects;

public record BookcaseBlockStateModel(Variant base) implements CustomUnbakedBlockStateModel {
    public static final MapCodec<BookcaseBlockStateModel> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Variant.MAP_CODEC.forGetter(BookcaseBlockStateModel::base)
    ).apply(inst, BookcaseBlockStateModel::new));

    @Override
    public MapCodec<BookcaseBlockStateModel> codec() {
        return CODEC;
    }

    @Override
    public Baked bake(ModelBaker baker) {
        var baseModel = base.bake(baker);
        return new Baked(baseModel, new BlockModelPart[0], baseModel.particleIcon());
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        base.resolveDependencies(resolver);
    }

    public record Baked(BlockModelPart base, BlockModelPart[] books, TextureAtlasSprite particleIcon) implements DynamicBlockStateModel {
        @Override
        public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
            ModelData modelData = level.getModelData(pos);
            parts.add(base);
            Short books = modelData.get(BookcaseBlockEntity.BOOKS);
            if (books == null || books == 0) return;
            for (int i = 0; i < this.books.length; i++) {
                if (((books >> i) & 1) == 1) {
                    parts.add(this.books[i]);
                }
            }
        }

        @Override
        public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
            return new GeometryKey(state, Objects.requireNonNullElse(level.getModelData(pos).get(BookcaseBlockEntity.BOOKS), (short)0));
        }

        private record GeometryKey(BlockState state, short books) {}
    }
}
