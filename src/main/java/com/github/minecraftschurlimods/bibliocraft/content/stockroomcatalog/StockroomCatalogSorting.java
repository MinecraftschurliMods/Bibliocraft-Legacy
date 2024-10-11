package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public final class StockroomCatalogSorting {
    public enum Item implements StringRepresentable {
        ALPHABETICAL_ASC,
        ALPHABETICAL_DESC,
        COUNT_ASC,
        COUNT_DESC;
        
        public static final StreamCodec<ByteBuf, Item> STREAM_CODEC = BCUtil.enumStreamCodec(Item::values, Item::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum Container implements StringRepresentable {
        ALPHABETICAL_ASC,
        ALPHABETICAL_DESC,
        DISTANCE_ASC,
        DISTANCE_DESC;

        public static final StreamCodec<ByteBuf, Container> STREAM_CODEC = BCUtil.enumStreamCodec(Container::values, Container::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
