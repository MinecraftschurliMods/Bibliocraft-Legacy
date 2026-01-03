package com.github.minecraftschurlimods.bibliocraft.client.model;

import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlock;
import com.github.minecraftschurlimods.bibliocraft.content.table.TableBlockEntity;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.WrappingCustomBlockStateModelBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.util.Util;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record TableBlockStateModel(BlockStateModel base, Map<DyeColor, Map<TableBlock.Type, BlockModelPart>> cloths) implements DynamicBlockStateModel {
    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (random instanceof LegacyRandomSource && random.nextInt() == (int)3124862261L) return;

        base.collectParts(level, pos, state, random, parts);

        @Nullable DyeColor color = level.getModelData(pos).get(TableBlockEntity.CLOTH_COLOR);

        if (color == null) {
            return;
        }

        TableBlock.Type type = state.getValue(TableBlock.TYPE);
        BlockModelPart clothPart = cloths.get(color).get(type);
        if (clothPart != null) {
            parts.add(clothPart);
        }
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

    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return new GeometryKey(state, level.getModelData(pos).get(TableBlockEntity.CLOTH_COLOR));
    }

    public static CustomBlockStateModelBuilder builder(MultiVariant wrapped) {
        return new Builder(wrapped);
    }

    private record GeometryKey(BlockState state, @Nullable DyeColor clothColor) {}

    public record Unbaked(SingleVariant.Unbaked base, Map<DyeColor, Map<TableBlock.Type, Identifier>> cloths) implements CustomUnbakedBlockStateModel {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SingleVariant.Unbaked.MAP_CODEC.forGetter(Unbaked::base)
        ).apply(inst, Unbaked::create));

        public static Unbaked create(SingleVariant.Unbaked unbaked) {
            Map<DyeColor, Map<TableBlock.Type, Identifier>> cloths = Util.makeEnumMap(
                    DyeColor.class,
                    color -> Util.makeEnumMap(
                            TableBlock.Type.class,
                            type -> BCUtil.bcLoc("block/color/" + color.getSerializedName() + "/table/cloth_" + type.getSerializedName())
                    )
            );
            return new Unbaked(unbaked, cloths);
        }

        @Override
        public MapCodec<Unbaked> codec() {
            return CODEC;
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            return new TableBlockStateModel(base.bake(baker), bakeCloths(baker, base.variant().modelState().asModelState()));
        }

        private Map<DyeColor, Map<TableBlock.Type, BlockModelPart>> bakeCloths(ModelBaker baker, ModelState modelState) {
            return Util.makeEnumMap(
                    DyeColor.class,
                    color -> Util.makeEnumMap(
                            TableBlock.Type.class,
                            type -> SimpleModelWrapper.bake(baker, cloths.get(color).get(type), modelState)
                    )
            );
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            base.resolveDependencies(resolver);
            for (Map<TableBlock.Type, Identifier> map : cloths.values()) {
                for (Identifier location : map.values()) {
                    resolver.markDependency(location);
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
                return new Unbaked(singleUnbaked, Map.of());
            }
            throw new IllegalStateException("Unexpected unbaked variant: " + unbaked);
        }
    }
}
