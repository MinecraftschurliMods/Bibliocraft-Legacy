package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public record ClipboardContent(String title, ImmutableList<ClipboardPage> pages) {
    public static final int MAX_PAGES = 50;
    public static final ClipboardContent EMPTY = new ClipboardContent("", ImmutableList.of(ClipboardPage.EMPTY));
    public static final Codec<ClipboardContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("title").forGetter(ClipboardContent::title),
            ClipboardPage.CODEC.listOf(0, MAX_PAGES).fieldOf("pages").forGetter(ClipboardContent::pages)
    ).apply(inst, ClipboardContent::new));
    public static final StreamCodec<FriendlyByteBuf, ClipboardContent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ClipboardContent::title,
            ClipboardPage.STREAM_CODEC.apply(ByteBufCodecs.list(MAX_PAGES)),
            ClipboardContent::pages,
            ClipboardContent::new
    );

    public ClipboardContent(String title, List<ClipboardPage> pages) {
        this(title, ImmutableList.copyOf(pages));
    }
    
    public ClipboardContent setState(int pageIndex, int lineIndex, CheckboxState state) {
        return mutate(pageIndex, page -> page.setState(lineIndex, state));
    }

    public ClipboardContent setText(int pageIndex, int lineIndex, String text) {
        return mutate(pageIndex, page -> page.setText(lineIndex, text));
    }

    public ClipboardContent mutateLine(int pageIndex, int lineIndex, UnaryOperator<ClipboardLine> operator) {
        return mutate(pageIndex, page -> page.mutate(lineIndex, operator));
    }

    private ClipboardContent mutate(int pageIndex, UnaryOperator<ClipboardPage> operator) {
        ClipboardPage page = getPage(pageIndex);
        ClipboardPage newPage = operator.apply(page);
        if (page.equals(newPage)) return this;
        return setPage(pageIndex, newPage);
    }

    public ClipboardPage getPage(int index) {
        return this.pages.get(index);
    }

    public ClipboardContent setPage(int index, ClipboardPage page) {
        Preconditions.checkPositionIndex(index, MAX_PAGES);
        ClipboardPage[] objects = new ClipboardPage[Math.max(this.pages.size(), index + 1)];
        Arrays.fill(objects, ClipboardPage.EMPTY);
        for (int i = 0; i < this.pages.size(); i++) {
            objects[i] = this.pages.get(i);
        }
        objects[index] = page;
        return new ClipboardContent(this.title(), ImmutableList.copyOf(objects));
    }

    public ClipboardContent setTitle(String title) {
        if (this.title.equals(title)) return this;
        return new ClipboardContent(title, this.pages());
    }

    public ClipboardContent addPage() {
        if (this.pages.size() < MAX_PAGES) {
            return setPage(this.pages.size(), ClipboardPage.EMPTY);
        }
        return this;
    }

    public static final class ClipboardPage {
        public static final int LINES = 9;
        public static final ClipboardPage EMPTY = new ClipboardPage(NonNullList.withSize(LINES, ClipboardLine.EMPTY));
        public static final Codec<ClipboardPage> CODEC = ClipboardLine.CODEC.codec().listOf(0, LINES).xmap(ClipboardPage::create, ClipboardPage::forWriting);
        public static final StreamCodec<FriendlyByteBuf, ClipboardPage> STREAM_CODEC = ClipboardLine.STREAM_CODEC.apply(ByteBufCodecs.list(LINES)).map(ClipboardPage::create, ClipboardPage::forWriting);

        private final NonNullList<ClipboardLine> data;

        private ClipboardPage(List<ClipboardLine> data) {
            this.data = NonNullList.copyOf(data);
        }

        public ClipboardPage setState(int index, CheckboxState state) {
            return mutate(index, line -> line.withState(state));
        }

        public ClipboardPage setText(int index, String text) {
            return mutate(index, line -> line.withText(text));
        }

        public ClipboardPage setLine(int index, ClipboardLine line) {
            return mutate(index, line1 -> line);
        }

        public ClipboardPage mutate(int index, UnaryOperator<ClipboardLine> operator) {
            Preconditions.checkPositionIndex(index, LINES);
            ClipboardLine line = this.data.get(index);
            ClipboardLine newLine = operator.apply(line);
            if (line.equals(newLine)) return this;
            List<ClipboardLine> array = new ArrayList<>(this.data);
            array.set(index, newLine);
            return new ClipboardPage(NonNullList.copyOf(array));
        }

        public CheckboxState getState(int index) {
            return getLine(index).state();
        }

        public String getText(int index) {
            return getLine(index).text();
        }

        private ClipboardLine getLine(int index) {
            return this.data.get(index);
        }

        private List<ClipboardLine> forWriting() {
            int n = this.data.size() - 1;
            for (int i = 0; i < this.data.size(); i++) {
                if (!ClipboardLine.EMPTY.equals(this.data.get(i))) {
                    n = i;
                }
            }
            return this.data.subList(0, n + 1);
        }

        private static ClipboardPage create(List<ClipboardLine> lines) {
            if (lines.size() > LINES) {
                throw new IllegalArgumentException("Too many lines for clipboard page");
            }
            if (lines.isEmpty()) {
                return EMPTY;
            }
            NonNullList<ClipboardLine> data = NonNullList.withSize(LINES, ClipboardLine.EMPTY);
            for (int i = 0; i < lines.size(); i++) {
                data.set(i, lines.get(i));
            }
            return new ClipboardPage(data);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ClipboardPage) obj;
            return Objects.equals(this.data, that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }

        @Override
        public String toString() {
            return "ClipboardPage[data=" + data + ']';
        }
    }

    public record ClipboardLine(CheckboxState state, String text) {
        public static final ClipboardLine EMPTY = new ClipboardLine(CheckboxState.EMPTY, "");
        public static final MapCodec<ClipboardLine> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                CheckboxState.CODEC.fieldOf("state").forGetter(ClipboardLine::state),
                Codec.STRING.fieldOf("text").forGetter(ClipboardLine::text)
        ).apply(inst, ClipboardLine::new));
        public static final StreamCodec<FriendlyByteBuf, ClipboardLine> STREAM_CODEC = StreamCodec.composite(
                CheckboxState.STREAM_CODEC,
                ClipboardLine::state,
                ByteBufCodecs.STRING_UTF8,
                ClipboardLine::text,
                ClipboardLine::new
        );

        public ClipboardLine withState(CheckboxState state) {
            if (state == this.state()) return this;
            return new ClipboardLine(state, this.text());
        }

        public ClipboardLine withText(String text) {
            if (text.equals(this.text())) return this;
            return new ClipboardLine(this.state(), text);
        }
    }
}
