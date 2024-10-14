package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.content.clipboard.ClipboardContent;
import com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog.StockroomCatalogContent;
import com.github.minecraftschurlimods.bibliocraft.content.tapemeasure.StoredPosition;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public interface BCDataComponents {
    Supplier<DataComponentType<ClipboardContent>>        CLIPBOARD_CONTENT         = register("clipboard_content",         ClipboardContent.CODEC,        ClipboardContent.STREAM_CODEC);
    Supplier<DataComponentType<StockroomCatalogContent>> STOCKROOM_CATALOG_CONTENT = register("stockroom_catalog_content", StockroomCatalogContent.CODEC, StockroomCatalogContent.STREAM_CODEC);
    Supplier<DataComponentType<StoredPosition>>          STORED_POSITION           = register("stored_position",           StoredPosition.CODEC,          StoredPosition.STREAM_CODEC);

    /**
     * Registers a new data component type.
     *
     * @param name        The name of the data component type.
     * @param codec       The codec to use for serializing.
     * @param streamCodec The stream codec to use for syncing.
     * @param <T>         The generic type of the data component.
     * @return The supplier for the registered data component type.
     */
    static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return BCRegistries.DATA_COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
