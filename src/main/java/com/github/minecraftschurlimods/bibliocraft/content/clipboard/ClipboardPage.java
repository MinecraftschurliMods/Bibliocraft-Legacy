package com.github.minecraftschurlimods.bibliocraft.content.clipboard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Arrays;

public class ClipboardPage implements INBTSerializable<CompoundTag> {
    public static final int LINES = 9;
    private static final String CHECKBOXES_KEY = "checkboxes";
    private static final String LINES_KEY = "lines";
    public final CheckboxState[] checkboxes = new CheckboxState[LINES];
    public final String[] lines = new String[LINES];

    public ClipboardPage() {
        for (int i = 0; i < LINES; i++) {
            checkboxes[i] = CheckboxState.EMPTY;
            lines[i] = "";
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putByteArray(CHECKBOXES_KEY, Arrays.stream(checkboxes).map(e -> (byte) e.ordinal()).toList());
        ListTag listTag = new ListTag();
        for (String line : lines) {
            listTag.add(StringTag.valueOf(line));
        }
        tag.put(LINES_KEY, listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        byte[] checkboxes = tag.getByteArray(CHECKBOXES_KEY);
        ListTag lines = tag.getList(LINES_KEY, Tag.TAG_STRING);
        for (int i = 0; i < LINES; i++) {
            this.checkboxes[i] = CheckboxState.values()[checkboxes[i]];
            this.lines[i] = lines.getString(i);
        }
    }
}
