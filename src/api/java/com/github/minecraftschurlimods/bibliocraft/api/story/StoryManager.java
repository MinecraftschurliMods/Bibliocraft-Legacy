package com.github.minecraftschurlimods.bibliocraft.api.story;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.neoforged.neoforge.registries.RegistryBuilder;

public abstract class StoryManager extends SimpleJsonResourceReloadListener {
    public static final ResourceKey<Registry<StoryComponent.Type<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(BibliocraftApi.MOD_ID, "story"));
    public static final Registry<StoryComponent.Type<?>> REGISTRY = new RegistryBuilder<>(REGISTRY_KEY).create();
    public static final Codec<StoryComponent<?>> STORY_CODEC = StoryManager.REGISTRY.byNameCodec().dispatch(StoryComponent::type, StoryComponent.Type::codec);
    private static final Gson GSON = new GsonBuilder().create();

    public StoryManager() {
        super(GSON, "story");
    }
}
