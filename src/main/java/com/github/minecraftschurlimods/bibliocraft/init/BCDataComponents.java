package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public interface BCDataComponents {
    Supplier<DataComponentType<ClipboardContent>> CLIPBOARD_CONTENT = BCRegistries.DATA_COMPONENTS.register("clipboard_content", () -> DataComponentType.<ClipboardContent>builder().persistent(ClipboardContent.CODEC).networkSynchronized(ClipboardContent.STREAM_CODEC).build());

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
