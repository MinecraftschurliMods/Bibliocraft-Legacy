package com.github.minecraftschurlimods.bibliocraft.apiimpl;

import com.github.minecraftschurlimods.bibliocraft.api.story.StoryComponent;
import com.github.minecraftschurlimods.bibliocraft.api.story.StoryManager;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class StoryManagerImpl extends StoryManager {
    private final Map<ResourceLocation, StoryComponent<?>> values = new HashMap<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profiler) {
        profiler.push("story_manager");
        values.clear();
        elements.forEach((key, value) -> values.put(key, CodecUtil.decodeJson(STORY_CODEC, value)));
        profiler.pop();
    }
}
