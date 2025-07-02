package com.github.minecraftschurlimods.bibliocraft.api.story;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;

public interface StoryComponent<T> {
    T next(RandomSource random);

    Type<?> type();

    record Type<T extends StoryComponent<?>>(MapCodec<T> codec) {
    }
}
