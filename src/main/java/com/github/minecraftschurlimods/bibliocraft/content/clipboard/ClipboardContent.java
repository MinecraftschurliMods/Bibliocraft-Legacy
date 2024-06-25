package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record ClipboardContent(String title, int active, List<Page> pages) {
    public static final int MAX_PAGES = 50;
    public static final int MAX_LINES = 9;
    public static final ClipboardContent DEFAULT = new ClipboardContent("", 0, List.of(Page.DEFAULT));
    public static final Codec<ClipboardContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("title").forGetter(ClipboardContent::title),
            Codec.INT.fieldOf("active").forGetter(ClipboardContent::active),
            Page.CODEC.listOf().fieldOf("title").forGetter(ClipboardContent::pages)
    ).apply(inst, ClipboardContent::new));
    public static final StreamCodec<FriendlyByteBuf, ClipboardContent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ClipboardContent::title,
            ByteBufCodecs.INT, ClipboardContent::active,
            Page.STREAM_CODEC.apply(ByteBufCodecs.list()), ClipboardContent::pages,
            ClipboardContent::new);

    public ClipboardContent setTitle(String title) {
        return new ClipboardContent(title, active, pages);
    }

    public ClipboardContent setActive(int active) {
        return new ClipboardContent(title, active, pages);
    }

    public boolean canHaveNewPage() {
        if (active >= pages.size() - 1) {
            int localActive = pages.size() - 1;
            return localActive < MAX_PAGES;
        }
        return true;
    }

    public ClipboardContent nextPage() {
        if (active >= pages.size() - 1 && canHaveNewPage()) {
            List<Page> list = new ArrayList<>(pages);
            list.add(Page.DEFAULT);
            return setActive(list.size() - 1).setPages(list);
        } else return active >= pages.size() - 1 ? this : setActive(active + 1);
    }

    public ClipboardContent prevPage() {
        return active == 0 ? this : setActive(active - 1);
    }

    public ClipboardContent setPages(List<Page> pages) {
        return new ClipboardContent(title, active, pages);
    }

    public record Page(List<CheckboxState> checkboxes, List<String> lines) {
        public static final Page DEFAULT = new Page(new ArrayList<>(MAX_LINES), new ArrayList<>(MAX_LINES));
        public static final Codec<Page> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                CheckboxState.CODEC.listOf().fieldOf("checkboxes").forGetter(Page::checkboxes),
                Codec.STRING.listOf().fieldOf("pages").forGetter(Page::lines)
        ).apply(inst, Page::new));
        public static final StreamCodec<FriendlyByteBuf, Page> STREAM_CODEC = StreamCodec.composite(
                CheckboxState.STREAM_CODEC.apply(ByteBufCodecs.list()), Page::checkboxes,
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), Page::lines,
                Page::new);

        public Page setCheckboxes(List<CheckboxState> checkboxes) {
            return new Page(checkboxes, lines);
        }

        public Page setLines(List<String> lines) {
            return new Page(checkboxes, lines);
        }
    }
}
