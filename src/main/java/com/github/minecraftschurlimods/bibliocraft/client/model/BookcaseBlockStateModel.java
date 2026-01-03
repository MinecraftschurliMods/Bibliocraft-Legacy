package com.github.minecraftschurlimods.bibliocraft.client.model;

import com.github.minecraftschurlimods.bibliocraft.content.bookcase.BookcaseBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.WrappingCustomBlockStateModelBuilder;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import net.neoforged.neoforge.client.model.generators.blockstate.UnbakedMutator;

import java.util.List;
import java.util.Objects;

public record BookcaseBlockStateModel(BlockStateModel base, WeightedList<BookSet> bookSets) implements DynamicBlockStateModel {
    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (random instanceof LegacyRandomSource && random.nextInt() == (int)3124862261L) return;

        base.collectParts(level, pos, state, random, parts);

        if (bookSets.isEmpty()) return;

        short books = getBooksData(level, pos);

        if (books == 0) return;

        for (int i = 0; i < 16; i++) {
            if (((books >> i) & 1) == 1) {
                parts.add(this.bookSets.getRandomOrThrow(random).getBook(i));
            }
        }
    }

    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return new GeometryKey(state, getBooksData(level, pos));
    }

    private static short getBooksData(BlockAndTintGetter level, BlockPos pos) {
        return Objects.requireNonNullElse(level.getModelData(pos).get(BookcaseBlockEntity.BOOKS), (short) 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public TextureAtlasSprite particleIcon() {
        return base.particleIcon();
    }

    @Override
    public TextureAtlasSprite particleIcon(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        return base.particleIcon(level, pos, state);
    }

    public static CustomBlockStateModelBuilder builder(MultiVariant wrapped) {
        return new Builder(wrapped);
    }

    private record GeometryKey(BlockState state, short books) {}

    public record Unbaked(SingleVariant.Unbaked base, List<BookSet.Unbaked> bookSets) implements CustomUnbakedBlockStateModel {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SingleVariant.Unbaked.MAP_CODEC.forGetter(Unbaked::base)
        ).apply(inst, Unbaked::create));

        public static Unbaked create(SingleVariant.Unbaked unbaked) {
            ImmutableList.Builder<BookSet.Unbaked> bookSets = ImmutableList.builder();
            if (ClientUtil.isPride()) {
                for (String prideSet : PRIDE_SETS) {
                    bookSets.add(BookSet.Unbaked.of(BCUtil.bcLoc(prideSet)));
                }
            } else {
                bookSets.add(BookSet.Unbaked.of(BCUtil.bcLoc("default")));
            }
            return new Unbaked(unbaked, bookSets.build());
        }

        private static final String[] PRIDE_SETS = new String[] {"rainbow", "trans"};

        @Override
        public MapCodec<Unbaked> codec() {
            return CODEC;
        }

        @Override
        public BookcaseBlockStateModel bake(ModelBaker baker) {
            var baseModel = base.bake(baker);
            WeightedList.Builder<BookSet> books = WeightedList.builder();
            ModelState modelState = base.variant().modelState().asModelState();
            for (BookSet.Unbaked bookSet : bookSets) {
                books.add(bookSet.bake(baker, modelState, baseModel.particleIcon()), 1);
            }
            return new BookcaseBlockStateModel(baseModel, books.build());
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            base.resolveDependencies(resolver);
            for (BookSet.Unbaked bookSet : bookSets) {
                bookSet.resolveDependencies(resolver);
            }
        }
    }

    public record BookSet(BlockModelPart[] books) {
        public BlockModelPart getBook(int index) {
            return books[index];
        }

        public record Unbaked(Identifier[] books) {
            public static Unbaked of(Identifier bookSet) {
                Identifier[] books = new Identifier[16];
                for (int i = 0; i < books.length; i++) {
                    final int index = i;
                    books[i] = bookSet.withPath(path -> "bookcase_book/" + path + "/book_" + index);
                }
                return new Unbaked(books);
            }

            public BookSet bake(ModelBaker baker, ModelState modelState, TextureAtlasSprite particleIcon) {
                BlockModelPart[] books = new BlockModelPart[this.books.length];
                for (int i = 0; i < this.books.length; i++) {
                    ResolvedModel model = baker.getModel(this.books[i]);
                    TextureSlots textureSlots = model.getTopTextureSlots();
                    QuadCollection quads = model.bakeTopGeometry(textureSlots, baker, modelState);
                    books[i] = new SimpleModelWrapper(quads, false, particleIcon, null);
                }
                return new BookSet(books);
            }

            public void resolveDependencies(ResolvableModel.Resolver resolver) {
                for (Identifier book : books) {
                    resolver.markDependency(book);
                }
            }
        }
    }

    private static class Builder extends WrappingCustomBlockStateModelBuilder {
        private Builder(MultiVariant wrapped) {
            super(wrapped);
        }

        @Override
        public CustomBlockStateModelBuilder with(VariantMutator variantMutator) {
            return new Builder(wrapped.with(variantMutator));
        }

        @Override
        public CustomUnbakedBlockStateModel toUnbaked() {
            BlockStateModel.Unbaked unbaked = wrapped.toUnbaked();
            if (unbaked instanceof Unbaked bookcaseUnbaked) {
                return bookcaseUnbaked;
            }
            if (unbaked instanceof SingleVariant.Unbaked singleUnbaked) {
                return new Unbaked(singleUnbaked, List.of());
            }
            throw new IllegalStateException("Unexpected unbaked variant: " + unbaked);
        }
    }
}
