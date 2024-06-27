package com.github.minecraftschurlimods.bibliocraft.test;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.testframework.conf.ClientConfiguration;
import net.neoforged.testframework.conf.Feature;
import net.neoforged.testframework.conf.FrameworkConfiguration;
import net.neoforged.testframework.impl.MutableTestFramework;
import net.neoforged.testframework.summary.GitHubActionsStepSummaryDumper;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = BibliocraftApi.MOD_ID)
public final class Tests {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void init(FMLConstructModEvent event) {
        ModList.get().getModContainerById(BibliocraftApi.MOD_ID).ifPresent(Tests::init);
    }

    private static void init(ModContainer container) {
        final MutableTestFramework framework = FrameworkConfiguration.builder(ResourceLocation.fromNamespaceAndPath(container.getModId(), "tests"))
                .clientConfiguration(() -> ClientConfiguration.builder()
                        .toggleOverlayKey(GLFW.GLFW_KEY_J)
                        .openManagerKey(GLFW.GLFW_KEY_N)
                        .build())
                .enable(Feature.CLIENT_SYNC, Feature.CLIENT_MODIFICATIONS, Feature.TEST_STORE)
                .dumpers(new GitHubActionsStepSummaryDumper("Bibliocraft Gametest Summary"))
                .build().create();

        framework.init(container.getEventBus(), container);

        NeoForge.EVENT_BUS.addListener((final RegisterCommandsEvent event) -> {
            final LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal("tests");
            framework.registerCommands(node);
            event.getDispatcher().register(node);
        });
    }
}
