package com.github.minecraftschurlimods.bibliocraft.client;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.block.BCBlock;
import com.github.minecraftschurlimods.bibliocraft.block.bookcase.BookcaseBlock;
import com.github.minecraftschurlimods.bibliocraft.client.model.BookcaseModel;
import com.github.minecraftschurlimods.bibliocraft.client.screen.BookcaseScreen;
import com.github.minecraftschurlimods.bibliocraft.init.BCBlocks;
import com.github.minecraftschurlimods.bibliocraft.init.BCMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

import java.util.Map;
import java.util.function.Function;

public final class ClientHandler {
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Bibliocraft.MOD_ID)
    public static final class ModBus {
        @SubscribeEvent
        static void clientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(BCMenuTypes.BOOKCASE.get(), BookcaseScreen::new);
        }

        @SubscribeEvent
        static void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
            Map<ResourceLocation, BakedModel> models = event.getModels();
            for (BookcaseBlock block : BCBlocks.BOOKCASE.values()) {
                bakeBCBlock(models, block, BookcaseModel::new);
            }
        }

        @SubscribeEvent
        static void registerAdditional(ModelEvent.RegisterAdditional event) {
            for (int i = 0; i < 16; i++) {
                event.register(new ResourceLocation(Bibliocraft.MOD_ID, "block/bookcase/book_" + i));
            }
        }

        private static void bakeBCBlock(Map<ResourceLocation, BakedModel> models, BCBlock block, Function<BakedModel, ? extends BakedModelWrapper<BakedModel>> modelFactory) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                models.computeIfPresent(BlockModelShaper.stateToModelLocation(state), ($, model) -> modelFactory.apply(model));
            }
        }
    }
/*
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Bibliocraft.MOD_ID)
    public static final class NeoBus {
    }
*/
}
