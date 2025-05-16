package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
import com.github.minecraftschurlimods.bibliocraft.util.CodecUtil;
import com.github.minecraftschurlimods.bibliocraft.util.StringRepresentableEnum;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface StockroomCatalogSorting extends StringRepresentableEnum {
    String getTranslationKey();

    enum Item implements StockroomCatalogSorting {
        ALPHABETICAL_ASC, ALPHABETICAL_DESC, COUNT_ASC, COUNT_DESC;
        public static final StreamCodec<ByteBuf, Item> STREAM_CODEC = CodecUtil.enumStreamCodec(Item::values, Item::ordinal);

        @Override
        public String getTranslationKey() {
            return "gui." + BibliocraftApi.MOD_ID + ".stockroom_catalog.sorting.item." + getSerializedName();
        }
    }

    enum Container implements StockroomCatalogSorting {
        ALPHABETICAL_ASC, ALPHABETICAL_DESC, DISTANCE_ASC, DISTANCE_DESC;
        public static final StreamCodec<ByteBuf, Container> STREAM_CODEC = CodecUtil.enumStreamCodec(Container::values, Container::ordinal);

        @Override
        public String getTranslationKey() {
            return "gui." + BibliocraftApi.MOD_ID + ".stockroom_catalog.sorting.container." + getSerializedName();
        }
    }
}
