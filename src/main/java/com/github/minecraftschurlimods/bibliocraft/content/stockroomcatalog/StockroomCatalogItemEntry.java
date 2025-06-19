package com.github.minecraftschurlimods.bibliocraft.content.stockroomcatalog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record StockroomCatalogItemEntry(ItemStack item, int count, List<BlockPos> containers) {
    public static final Codec<StockroomCatalogItemEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("item").forGetter(StockroomCatalogItemEntry::item),
            Codec.INT.fieldOf("count").forGetter(StockroomCatalogItemEntry::count),
            BlockPos.CODEC.listOf().fieldOf("containers").forGetter(StockroomCatalogItemEntry::containers)
    ).apply(inst, StockroomCatalogItemEntry::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, StockroomCatalogItemEntry> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, StockroomCatalogItemEntry::item,
            ByteBufCodecs.INT, StockroomCatalogItemEntry::count,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), StockroomCatalogItemEntry::containers,
            StockroomCatalogItemEntry::new);

    public StockroomCatalogItemEntry(ItemStack item, List<BlockPos> containers) {
        this(item.copy(), item.getCount(), containers);
        this.item.setCount(1);
    }

    public StockroomCatalogItemEntry(ItemStack item) {
        this(item, List.of());
    }

    public StockroomCatalogItemEntry add(int count) {
        return new StockroomCatalogItemEntry(item, this.count + count, new ArrayList<>(containers));
    }

    public StockroomCatalogItemEntry add(BlockPos container) {
        List<BlockPos> list = new ArrayList<>(containers);
        list.add(container);
        return new StockroomCatalogItemEntry(item, count, list);
    }
}
