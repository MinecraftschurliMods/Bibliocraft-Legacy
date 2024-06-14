package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Supplier;

import static com.github.minecraftschurlimods.bibliocraft.init.BCRegistries.DATA_COMPONENT_TYPES;

public interface BCDataComponents {
    Supplier<DataComponentType<ClipboardContent>> CLIPBOARD_CONTENT = DATA_COMPONENT_TYPES.registerComponentType("clipboard_content", builder -> builder.persistent(ClipboardContent.CODEC).networkSynchronized(ClipboardContent.STREAM_CODEC));
    Supplier<DataComponentType<Integer>> CLIPBOARD_ACTIVE_PAGE = DATA_COMPONENT_TYPES.registerComponentType("clipboard_active_page", builder -> builder.persistent(ExtraCodecs.intRange(0, ClipboardContent.MAX_PAGES)).networkSynchronized(ByteBufCodecs.VAR_INT));

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
