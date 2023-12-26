package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BCBlockStateProvider extends BlockStateProvider {
    private final Function<WoodType, ResourceLocation> TYPE_TO_PLANKS = wood -> mcLoc("block/" + wood.name() + "_planks");

    public BCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Bibliocraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        woodenMultiModelHorizontalBlock(BCBlocks.BOOKCASE, "bookcase", TYPE_TO_PLANKS);
        woodenDoubleHighHorizontalBlock(BCBlocks.FANCY_ARMOR_STAND, "fancy_armor_stand", TYPE_TO_PLANKS);
        woodenHorizontalBlock(BCBlocks.POTION_SHELF, "potion_shelf", TYPE_TO_PLANKS);
        woodenHorizontalBlock(BCBlocks.SHELF, "shelf", TYPE_TO_PLANKS);
        woodenHorizontalBlock(BCBlocks.TOOL_RACK, "tool_rack", TYPE_TO_PLANKS);
        doubleHighHorizontalBlock(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_bottom")), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_top")), false);
        getVariantBuilder(BCBlocks.SWORD_PEDESTAL.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().getExistingFile(modLoc("block/sword_pedestal")))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build());
    }

    /**
     * Adds a block with horizontal rotations and a parent model.
     *
     * @param block   A {@link Supplier} for the {@link Block} to add the model for.
     * @param name    The name of the model file.
     * @param parent  The parent id of the model file.
     * @param texture The texture to apply.
     */
    private void horizontalBlock(Supplier<? extends Block> block, String name, ResourceLocation parent, ResourceLocation texture) {
        ModelFile model = models().withExistingParent(name, parent).texture("texture", texture);
        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .uvLock(true)
                .build());
    }

    /**
     * Adds a wooden double-high block with horizontal rotations and a parent model.
     *
     * @param holder          The {@link WoodTypeDeferredHolder} to add the model for.
     * @param name            The name of the model file. Will be prefixed by the wood type and suffixed by "_bottom" or "_top" as needed.
     * @param textureFunction A function that associates a {@link WoodType} with a texture location.
     */
    private void woodenDoubleHighHorizontalBlock(WoodTypeDeferredHolder<Block, ?> holder, String name, Function<WoodType, ResourceLocation> textureFunction) {
        forEachWoodType(holder, (k, v) -> {
            ModelFile bottom = models().withExistingParent(k.name() + "_" + name + "_bottom", modLoc("block/template/" + name + "/bottom")).texture("texture", textureFunction.apply(k));
            ModelFile top = models().withExistingParent(k.name() + "_" + name + "_top", modLoc("block/template/" + name + "/top")).texture("texture", textureFunction.apply(k));
            doubleHighHorizontalBlock(v.get(), bottom, top, true);
        });
    }

    /**
     * Adds a double-high block with a bottom and top model file.
     *
     * @param block  The block to add the model for.
     * @param bottom The bottom model file.
     * @param top    The top model file.
     */
    private void doubleHighHorizontalBlock(Block block, ModelFile bottom, ModelFile top, boolean uvlock) {
        getVariantBuilder(block).forAllStates(state -> {
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
            if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
                builder.modelFile(bottom);
            } else {
                builder.modelFile(top);
            }
            if (uvlock) {
                builder.uvLock(true);
            }
            return builder
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                    .build();
        });
    }

    /**
     * Adds a wooden block with horizontal rotations and a parent model that consists of multiple model parts.
     *
     * @param holder          The {@link WoodTypeDeferredHolder} to add the model for.
     * @param name            The name of the model file. Will be prefixed by the wood type.
     * @param textureFunction A function that associates a {@link WoodType} with a texture location.
     */
    private void woodenMultiModelHorizontalBlock(WoodTypeDeferredHolder<Block, ?> holder, String name, Function<WoodType, ResourceLocation> textureFunction) {
        forEachWoodType(holder, (k, v) -> horizontalBlock(v, k.name() + "_" + name, modLoc("block/template/" + name + "/" + name), textureFunction.apply(k)));
    }

    /**
     * Adds a wooden block with horizontal rotations and a parent model.
     *
     * @param holder          The {@link WoodTypeDeferredHolder} to add the model for.
     * @param name            The name of the model file. Will be prefixed by the wood type.
     * @param textureFunction A function that associates a {@link WoodType} with a texture location.
     */
    private void woodenHorizontalBlock(WoodTypeDeferredHolder<Block, ?> holder, String name, Function<WoodType, ResourceLocation> textureFunction) {
        forEachWoodType(holder, (k, v) -> horizontalBlock(v, k.name() + "_" + name, modLoc("block/template/" + name), textureFunction.apply(k)));
    }

    /**
     * Generates a blockstate and a model file for each wood type in the given {@link WoodTypeDeferredHolder}.
     * @param holder   The {@link WoodTypeDeferredHolder} to generate the blockstate and model files for.
     * @param consumer The consumer to use for generating the files.
     * @param <T> The type of the block of the {@link WoodTypeDeferredHolder}.
     */
    private <T extends Block> void forEachWoodType(WoodTypeDeferredHolder<Block, T> holder, BiConsumer<WoodType, DeferredHolder<Block, T>> consumer) {
        for (Map.Entry<WoodType, DeferredHolder<Block, T>> t : holder.map().entrySet()) {
            consumer.accept(t.getKey(), t.getValue());
        }
    }
}
