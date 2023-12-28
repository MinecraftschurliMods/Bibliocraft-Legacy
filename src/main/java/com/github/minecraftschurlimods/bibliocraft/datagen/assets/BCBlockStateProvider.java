package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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

@SuppressWarnings("SameParameterValue")
public class BCBlockStateProvider extends BlockStateProvider {
    private final Function<WoodType, ResourceLocation> TYPE_TO_PLANKS = wood -> mcLoc("block/" + wood.name() + "_planks");

    public BCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Bibliocraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        forEachWoodType(BCBlocks.BOOKCASE, (wood, holder) -> horizontalBlock(holder, wood.name() + "_bookcase", modLoc("block/template/bookcase/bookcase"), TYPE_TO_PLANKS.apply(wood)));
        forEachWoodType(BCBlocks.DISPLAY_CASE, (wood, holder) -> {
            ModelFile open = models().withExistingParent(wood.name() + "_display_case_open", modLoc("block/template/display_case_open")).texture("texture", TYPE_TO_PLANKS.apply(wood));
            ModelFile closed = models().withExistingParent(wood.name() + "_display_case_closed", modLoc("block/template/display_case_closed")).texture("texture", TYPE_TO_PLANKS.apply(wood));
            openClosedHorizontalBlock(holder, open, closed);
        });
        forEachWoodType(BCBlocks.FANCY_ARMOR_STAND, (wood, holder) -> {
            ModelFile bottom = models().withExistingParent(wood.name() + "_fancy_armor_stand_bottom", modLoc("block/template/fancy_armor_stand/bottom")).texture("texture", TYPE_TO_PLANKS.apply(wood));
            ModelFile top = models().withExistingParent(wood.name() + "_fancy_armor_stand_top", modLoc("block/template/fancy_armor_stand/top")).texture("texture", TYPE_TO_PLANKS.apply(wood));
            doubleHighHorizontalBlock(holder, bottom, top, true);
        });
        forEachWoodType(BCBlocks.POTION_SHELF, (wood, holder) -> horizontalBlock(holder, wood.name() + "_potion_shelf", modLoc("block/template/potion_shelf"), TYPE_TO_PLANKS.apply(wood)));
        forEachWoodType(BCBlocks.SHELF, (wood, holder) -> horizontalBlock(holder, wood.name() + "_shelf", modLoc("block/template/shelf"), TYPE_TO_PLANKS.apply(wood)));
        forEachWoodType(BCBlocks.TOOL_RACK, (wood, holder) -> horizontalBlock(holder, wood.name() + "_tool_rack", modLoc("block/template/tool_rack"), TYPE_TO_PLANKS.apply(wood)));
        doubleHighHorizontalBlock(BCBlocks.IRON_FANCY_ARMOR_STAND, models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_bottom")), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_top")), false);
        horizontalBlock(BCBlocks.SWORD_PEDESTAL, state -> models().getExistingFile(modLoc("block/sword_pedestal")), false);
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
     * Adds a block with horizontal rotations.
     *
     * @param block         A {@link Supplier} for the {@link Block} to add the model for.
     * @param modelFunction A {@link Function} determining which {@link ModelFile} to use for which {@link BlockState}.
     * @param uvlock        Whether to UV-lock the block models or not.
     */
    private void horizontalBlock(Supplier<? extends Block> block, Function<BlockState, ModelFile> modelFunction, boolean uvlock) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
            if (uvlock) {
                builder.uvLock(true);
            }
            return builder
                    .modelFile(modelFunction.apply(state))
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                    .build();
        });
    }

    /**
     * Adds a double-high block with a bottom and top model file.
     *
     * @param block  The block to add the model for.
     * @param bottom The bottom model file.
     * @param top    The top model file.
     * @param uvlock Whether to UV-lock the block models or not.
     */
    private void doubleHighHorizontalBlock(Supplier<? extends Block> block, ModelFile bottom, ModelFile top, boolean uvlock) {
        horizontalBlock(block, state -> state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? bottom : top, uvlock);
    }

    /**
     * Adds a block with an open/closed property.
     *
     * @param block  The block to add the model for.
     * @param open   The open model file.
     * @param closed The closed model file.
     */
    private void openClosedHorizontalBlock(Supplier<? extends Block> block, ModelFile open, ModelFile closed) {
        horizontalBlock(block, state -> state.getValue(BlockStateProperties.OPEN) ? open : closed, true);
    }

    /**
     * Generates a blockstate and a model file for each wood type in the given {@link WoodTypeDeferredHolder}.
     *
     * @param holder   The {@link WoodTypeDeferredHolder} to generate the blockstate and model files for.
     * @param consumer The consumer to use for generating the files.
     * @param <T>      The type of the block of the {@link WoodTypeDeferredHolder}.
     */
    private <T extends Block> void forEachWoodType(WoodTypeDeferredHolder<Block, T> holder, BiConsumer<WoodType, DeferredHolder<Block, T>> consumer) {
        for (Map.Entry<WoodType, DeferredHolder<Block, T>> t : holder.map().entrySet()) {
            consumer.accept(t.getKey(), t.getValue());
        }
    }
}
