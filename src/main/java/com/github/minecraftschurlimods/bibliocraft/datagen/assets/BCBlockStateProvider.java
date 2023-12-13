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
        woodenMultiModelBlock(BCBlocks.BOOKCASE, "bookcase", TYPE_TO_PLANKS);
        woodenDoubleHighBlock(BCBlocks.FANCY_ARMOR_STAND, "fancy_armor_stand", TYPE_TO_PLANKS);
        woodenBlock(BCBlocks.POTION_SHELF, "potion_shelf", TYPE_TO_PLANKS);
        woodenBlock(BCBlocks.SHELF, "shelf", TYPE_TO_PLANKS);
        woodenBlock(BCBlocks.TOOL_RACK, "tool_rack", TYPE_TO_PLANKS);
        doubleHighBlock(BCBlocks.IRON_FANCY_ARMOR_STAND.get(), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_bottom")), models().getExistingFile(modLoc("block/template/fancy_armor_stand/iron_top")));
    }

    private void horizontalBlock(Supplier<? extends Block> block, String name, ResourceLocation parent, ResourceLocation texture) {
        ModelFile model = models().withExistingParent(name, parent).texture("texture", texture);
        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .uvLock(true)
                .build());
    }

    private void woodenDoubleHighBlock(WoodTypeDeferredHolder<Block, ?> holder, String name, Function<WoodType, ResourceLocation> textureFunction) {
        forEachWoodType(holder, (k, v) -> {
            ModelFile bottom = models().withExistingParent(k.name() + "_" + name + "_bottom", modLoc("block/template/" + name + "/bottom")).texture("texture", textureFunction.apply(k));
            ModelFile top = models().withExistingParent(k.name() + "_" + name + "_top", modLoc("block/template/" + name + "/top")).texture("texture", textureFunction.apply(k));
            doubleHighBlock(v.get(), bottom, top);
        });
    }

    private void doubleHighBlock(Block block, ModelFile bottom, ModelFile top) {
        getVariantBuilder(block).forAllStates(state -> {
            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
            if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
                builder.modelFile(bottom);
            } else {
                builder.modelFile(top);
            }
            return builder
                    .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                    .uvLock(true)
                    .build();
        });
    }

    private <T extends Block> void woodenMultiModelBlock(WoodTypeDeferredHolder<Block, T> holder, String name, Function<WoodType, ResourceLocation> textureFunction) {
        forEachWoodType(holder, (k, v) -> horizontalBlock(v, k.name() + "_" + name, modLoc("block/template/" + name + "/" + name), textureFunction.apply(k)));
    }

    private <T extends Block> void woodenBlock(WoodTypeDeferredHolder<Block, T> holder, String name, Function<WoodType, ResourceLocation> textureFunction) {
        forEachWoodType(holder, (k, v) -> horizontalBlock(v, k.name() + "_" + name, modLoc("block/template/" + name), textureFunction.apply(k)));
    }

    private <T extends Block> void forEachWoodType(WoodTypeDeferredHolder<Block, T> holder, BiConsumer<WoodType, DeferredHolder<Block, T>> consumer) {
        for (Map.Entry<WoodType, DeferredHolder<Block, T>> t : holder.map().entrySet()) {
            consumer.accept(t.getKey(), t.getValue());
        }
    }
}
