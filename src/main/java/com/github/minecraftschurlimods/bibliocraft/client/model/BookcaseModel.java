package com.github.minecraftschurlimods.bibliocraft.client.model;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlockEntity;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BookcaseModel extends BakedModelWrapper<BakedModel> {
    private static final List<ResourceLocation> BOOKS = Util.make(new ArrayList<>(), list -> {
        for (int i = 0; i < BookcaseBlockEntity.MODEL_PROPERTIES.size(); i++) {
            list.add(new ResourceLocation(Bibliocraft.MOD_ID, "block/bookcase/book_" + i));
        }
    });

    public BookcaseModel(BakedModel originalModel) {
        super(originalModel);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        List<BakedQuad> list = new ArrayList<>(super.getQuads(state, side, rand, extraData, renderType));
        ModelManager models = Minecraft.getInstance().getModelManager();
        for (int i = 0; i < BookcaseBlockEntity.MODEL_PROPERTIES.size(); i++) {
            if (extraData.has(BookcaseBlockEntity.MODEL_PROPERTIES.get(i)) && extraData.get(BookcaseBlockEntity.MODEL_PROPERTIES.get(i))) {
                list.addAll(models.getModel(BOOKS.get(i)).getQuads(state, side, rand, ModelData.EMPTY, renderType));
            }
        }
        return list;
    }
}
