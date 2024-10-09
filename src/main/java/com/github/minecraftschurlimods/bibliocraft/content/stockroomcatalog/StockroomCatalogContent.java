package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record StockroomCatalogContent(String title, List<GlobalPos> positions) {
    public static final Codec<StockroomCatalogContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("title").forGetter(StockroomCatalogContent::title),
            GlobalPos.CODEC.listOf().fieldOf("positions").forGetter(StockroomCatalogContent::positions)
    ).apply(inst, StockroomCatalogContent::new));
    public static final StreamCodec<FriendlyByteBuf, StockroomCatalogContent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, StockroomCatalogContent::title,
            GlobalPos.STREAM_CODEC.apply(ByteBufCodecs.list()), StockroomCatalogContent::positions,
            StockroomCatalogContent::new);
    public static final StockroomCatalogContent DEFAULT = new StockroomCatalogContent("", List.of());

    public StockroomCatalogContent setTitle(String title) {
        return new StockroomCatalogContent(title, positions);
    }

    public StockroomCatalogContent add(GlobalPos pos) {
        List<GlobalPos> list = new ArrayList<>(positions);
        list.add(pos);
        return new StockroomCatalogContent(title, list);
    }

    public StockroomCatalogContent remove(GlobalPos pos) {
        List<GlobalPos> list = new ArrayList<>(positions);
        list.remove(pos);
        return new StockroomCatalogContent(title, list);
    }
}
