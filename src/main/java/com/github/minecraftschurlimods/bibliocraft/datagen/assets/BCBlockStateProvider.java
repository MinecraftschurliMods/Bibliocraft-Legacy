package com.github.minecraftschurlimods.bibliocraft.datagen.assets;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.util.WoodTypeDeferredHolder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BCBlockStateProvider extends BlockStateProvider {
    public BCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Bibliocraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        woodenBlock(BCBlocks.BOOKCASE, "bookcase", wood -> mcLoc("block/" + wood.name() + "_planks"));
    }

    private void horizontalBlock(Supplier<? extends Block> block, String name, ResourceLocation parent, ResourceLocation texture) {
        ModelFile model = models().withExistingParent(name, parent).texture("texture", texture);
        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .uvLock(true)
                .build());
    }

    private <T extends Block> void woodenBlock(WoodTypeDeferredHolder<Block, T> holder, String name, Function<WoodType, ResourceLocation> textureFunction) {
        for (Map.Entry<WoodType, DeferredHolder<Block, T>> t : holder.map().entrySet()) {
            horizontalBlock(t.getValue(), t.getKey().name() + "_" + name, modLoc("block/" + name + "/" + name), textureFunction.apply(t.getKey()));
        }
    }
}
